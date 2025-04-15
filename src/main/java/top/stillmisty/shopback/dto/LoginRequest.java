package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "登录请求")
public record LoginRequest(
        @Schema(description = "用户名", example = "114514")
        @Size(min = 6, max = 20, message = "用户名长度必须在6-20之间")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
        String username,
        @Schema(description = "密码", example = "password")
        @Pattern(regexp = "^^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$", message = "密码必须包含至少一个大写字母、一个小写字母和一个数字")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
}