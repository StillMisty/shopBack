package top.stillmisty.shopback;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

@SpringBootTest
public class JwtTest {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    void generateSecret() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Secret" + secretString);
    }

    @Test
    void genereateJwt() {
        // 这里的 jwtSecret 应该是从 application.properties 中读取的
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
        String jwt = Jwts.builder()
                .id("114514")
                .signWith(key)
                .compact();
        System.out.println("Jwt:" + jwt);
    }

    @Test
    void verifyJwt() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
        String jws = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNzQzMzQ3NTI5LCJleHAiOjE3NDM0MzM5Mjl9.Xz_S43ED6k8O_D7NJNW4GGOcFCiP3UFbCoMQiAzethY";
        String id = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getId();
        System.out.println("Id:" + id);
    }
}
