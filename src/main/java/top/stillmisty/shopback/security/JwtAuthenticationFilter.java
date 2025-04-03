package top.stillmisty.shopback.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;

import java.io.IOException;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 获取JWT令牌
            String jwt = getJwtFromRequest(request);
            // 验证令牌，没有令牌则不进行验证，其也不会被设置身份
            if (StringUtils.hasText(jwt)) {
                // 获取用户信息
                UUID userId = jwtUtils.parseJwt(jwt);
                // 创建认证对象
                Users user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("用户不存在"));
                CustomUserDetails userDetails = new CustomUserDetails(userId, user.getUserName(), user.getPassword());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("为用户ID: {} 设置安全上下文", userId);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            logger.error("JWT认证失败: {}", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 无效授权
            return;
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