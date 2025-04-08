package top.stillmisty.shopback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.AdminAddRequest;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.security.JwtUtils;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.secret}")
    private String adminSecret;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT token
     */
    public String login(String username, String password) {
        return getJwt(username, password);
    }

    // 判断用户是否存在
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT token
     */
    public String register(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new Users(username, encodedPassword));
        return getJwt(username, password);
    }

    private String getJwt(String username, String password) {
        System.out.println("开始认证...");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        System.out.println("认证完成: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 更新最后登录时间
        users.setLastLoginTime(Instant.now());
        userRepository.save(users);

        UUID userId = users.getUserId();
        System.out.println("User ID: " + userId);
        return jwtUtils.createToken(userId);
    }

    /**
     * 将用户设置为管理员
     */
    public void adminRegister(AdminAddRequest adminAddRequest) {
        if (!adminAddRequest.secret().equals(adminSecret)) {
            throw new RuntimeException("管理员密钥错误");
        }
        Users user = userRepository.findByUsername(adminAddRequest.username())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!user.isAdmin()) {
            user.setAdmin(true);
            userRepository.save(user);
        }
    }
}