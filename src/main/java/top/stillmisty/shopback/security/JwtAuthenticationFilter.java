package top.stillmisty.shopback.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.stillmisty.shopback.config.SecurityConstants;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.enums.UserStatus;
import top.stillmisty.shopback.exception.UserNotFoundException;
import top.stillmisty.shopback.exception.UserStatusException;
import top.stillmisty.shopback.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    private static Authentication getAuthentication(Users user) {
        List<SimpleGrantedAuthority> authorities;
        if (user.isAdmin()) {
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        } else {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.isAdmin()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
        return authentication;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取当前请求路径
        String path = request.getServletPath();

        // 使用统一的白名单判断
        if (SecurityConstants.isUrlInWhiteList(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取JWT令牌
        String jwt = getJwtFromRequest(request);
        // 验证令牌，没有令牌则不进行验证，其也不会被设置身份
        if (StringUtils.hasText(jwt)) {
            // 获取用户信息
            UUID userId = jwtUtils.parseJwt(jwt);

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("用户不存在"));

            if (user.getUserStatus() == UserStatus.FROZEN) {
                throw new UserStatusException("您的账户已被冻结，请联系管理员");
            }
            if (user.getUserStatus() == UserStatus.BLACKLISTED) {
                throw new UserStatusException("您的账户已被列入黑名单，无法访问系统");
            }


            log.info("JWT认证通过, 用户ID: {}, 是否为管理员{}", userId, user.isAdmin());
            Authentication authentication = getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}