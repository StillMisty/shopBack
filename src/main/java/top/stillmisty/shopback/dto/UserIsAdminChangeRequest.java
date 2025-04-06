package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户是否为管理员修改请求")
public record UserIsAdminChangeRequest(
        @Schema(description = "是否为管理员")
        Boolean isAdmin
) {
}
