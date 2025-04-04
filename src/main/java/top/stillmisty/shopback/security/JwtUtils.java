package top.stillmisty.shopback.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {
    final private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.expirationMs}")
    final private static int jwtExpirationMs = 86400000; // 1天
    private static SecretKey secret;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String createToken(UUID id, boolean isAdmin) {
        long now = System.currentTimeMillis();
        Map<String, Boolean> claims = Map.of(
                "isAdmin", isAdmin
        );

        return Jwts.builder()
                .id(id.toString())
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpirationMs))
                .signWith(secret)
                .compact();
    }

    public TokenClaims parseJwt(String jwt) throws JwtException {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            // 判断JWT是否过期
            if (claims.getExpiration().before(new Date())) {
                logger.error("JWT已过期");
                throw new JwtException("JWT已过期");
            }
            // 获取用户ID
            String id = claims.getId();
            if (id == null || id.isEmpty()) {
                logger.error("JWT中缺少ID信息");
                throw new JwtException("JWT中缺少ID信息");
            }
            boolean isAdmin = claims.get("isAdmin", Boolean.class);

            return new TokenClaims(UUID.fromString(id), isAdmin);
        } catch (Exception e) {
            throw new JwtException("无效的JWT令牌", e);
        }
    }

    public record TokenClaims(UUID userId, boolean isAdmin) {
    }
}