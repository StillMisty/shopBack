package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "登录请求")
public record LoginRequest(
        @Schema(description = "用户名", example = "114514")
        @Size(min = 6, max = 20, message = "用户名长度必须在1-20之间")
        String username,
        @Schema(description = "密码", example = "password")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
}