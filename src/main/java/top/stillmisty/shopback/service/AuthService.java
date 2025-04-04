package top.stillmisty.shopback.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.AdminAddRequest;
import top.stillmisty.shopback.entity.Admin;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.AdminRepository;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.security.JwtUtils;

import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        return getJwt(username, password);
    }

    public String register(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new Users(username, encodedPassword));
        return getJwt(username, password);
    }

    public String adminLogin(String username, String password) {
        return getAdminJwt(username, password);
    }

    public String adminRegister(AdminAddRequest adminAddRequest) {
        String encodedPassword = passwordEncoder.encode(adminAddRequest.password());
        adminRepository.save(new Admin(adminAddRequest.username(), encodedPassword, adminAddRequest.nickname(), adminAddRequest.email(), adminAddRequest.phone()));
        return getAdminJwt(adminAddRequest.username(), adminAddRequest.password());
    }

    // 普通用户登录
    private String getJwt(String username, String password) {
        System.out.println("开始认证...");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        System.out.println("认证完成: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"))
                .getUserId();
        System.out.println("User ID: " + userId);
        return jwtUtils.createToken(userId, false);
    }

    // 管理员登录
    private String getAdminJwt(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UUID adminId = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员不存在"))
                .getAdminId();
        return jwtUtils.createToken(adminId, true);
    }
}