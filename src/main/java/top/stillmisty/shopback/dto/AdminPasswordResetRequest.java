package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record AdminPasswordResetRequest(
        @Schema(description = "新密码", example = "Qw1234")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,20}$",
                message = "密码长度必须在6-20之间，且必须包含至少一个大写字母、一个小写字母和一个数字")
        String newPassword
) {
}