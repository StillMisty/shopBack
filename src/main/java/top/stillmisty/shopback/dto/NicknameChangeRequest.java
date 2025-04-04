package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "修改昵称请求")
public record NicknameChangeRequest(
        @Schema(description = "新的昵称", example = "张光睿")
        @Size(min = 1, max = 20, message = "昵称长度必须在1-20之间")
        String nickname
) {
}
