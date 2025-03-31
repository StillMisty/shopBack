package top.stillmisty.shopback.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.security.JwtUtils;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
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

    private String getJwt(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"))
                .getUserId();
        return jwtUtils.createToken(userId);
    }
}