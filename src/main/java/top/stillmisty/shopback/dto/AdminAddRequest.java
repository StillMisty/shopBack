package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "添加管理员请求")
public record AdminAddRequest(
        @Schema(description = "添加管理员的密钥", example = "123456")
        String secret,
        @Schema(description = "用户账号", example = "114514")
        @Size(min = 6, max = 20, message = "账号长度必须在6-20之间")
        String username
) {
}
