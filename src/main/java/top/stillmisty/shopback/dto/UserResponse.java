package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "获取其他用户信息的响应")
public record UserResponse(
        UUID userId,
        String nickname,
        String avatar,
        int userStatus
) {
}
