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
            // 验证令牌
            if (StringUtils.hasText(jwt)) {
                try {
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
                } catch (Exception e) {
                    log.error("令牌验证失败", e);
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "无效的令牌: " + e.getMessage());
                    return;
                }
            } else {
                log.debug("未找到JWT令牌");
                // 继续过滤器链
            }
        } catch (Exception e) {
            log.error("认证过程中发生错误", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器错误");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("Authorization头: " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}