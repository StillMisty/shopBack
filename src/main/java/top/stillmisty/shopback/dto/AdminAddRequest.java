package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "添加管理员请求")
public record AdminAddRequest(
        @Schema(description = "添加管理员的密钥", example = "123456")
        String secret,
        @Schema(description = "管理员账号", example = "admin")
        @Size(min = 6, max = 20, message = "账号长度必须在6-20之间")
        String username,
        @Schema(description = "管理员密码", example = "password")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password,
        @Schema(description = "管理员昵称", example = "张光睿")
        @Size(min = 1, max = 20, message = "昵称长度必须在1-20之间")
        String nickname,
        String email,
        String phone
) {
}
