package top.stillmisty.shopback.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.stillmisty.shopback.exception.UserNotFoundException;
import top.stillmisty.shopback.exception.UserStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UserStatusException ex) {
            // 自定义用户状态异常处理
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> errorResponse = createErrorResponse(ex.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (UserNotFoundException ex) {
            // 用户不存在异常处理
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> errorResponse = createErrorResponse(ex.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (JwtException ex) {
            // JWT解析异常处理
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> errorResponse = createErrorResponse("无效的JWT令牌");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (RuntimeException ex) {
            // 其他运行时异常处理
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> errorResponse = createErrorResponse(ex.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message != null ? message : "未知错误");
        errorResponse.put("data", null);
        return errorResponse;
    }
}