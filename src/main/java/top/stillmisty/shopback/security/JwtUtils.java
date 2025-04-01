package top.stillmisty.shopback.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    final private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    final private SecretKey secret;
    final private long jwtExpirationMs;

    public JwtUtils(Environment environment) {
        // 从 application.properties 中读取
        String jwtSecret = environment.getProperty("jwt.secret");
        this.jwtExpirationMs = 86400000;
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String createToken(UUID id) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .id(id.toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpirationMs))
                .signWith(this.secret)
                .compact();
    }

    public UUID parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.secret)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
            // 判断JWT是否过期
            if (claims.getExpiration().before(new Date())) {
                logger.error("JWT已过期");
                throw new JwtException("JWT已过期");
            }

            String id = claims.getId();
            if (id == null || id.isEmpty()) {
                logger.error("JWT中缺少ID信息");
                throw new JwtException("JWT中缺少ID信息");
            }

            return UUID.fromString(id);
        } catch (JwtException e) {
            // 保留原始异常作为原因
            logger.error("JWT验证失败: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            // 专门处理UUID格式异常
            logger.error("JWT中的ID格式无效: {}", e.getMessage());
            throw new JwtException("JWT中的ID格式无效", e);
        } catch (Exception e) {
            // 捕获所有其他可能的异常
            logger.error("JWT解析失败: {}", e.getMessage());
            throw new JwtException("无效的JWT令牌", e);
        }
    }
}