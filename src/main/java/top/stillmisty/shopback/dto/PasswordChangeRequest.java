package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "修改密码请求")
public record PasswordChangeRequest(
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "密码只能包含字母、数字和下划线")
        String password
) {
}