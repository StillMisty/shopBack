package top.stillmisty.shopback;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.util.Base64;

@SpringBootTest
class ShopBackApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        KeyPair keyPair = Jwts.SIG.EdDSA.keyPair().build();
        System.out.println("Public key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("Private key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }
}
