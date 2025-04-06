package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import top.stillmisty.shopback.enums.UserStatus;

/**
 * 用户状态修改请求
 *
 * @param status 用户状态
 */
@Schema(description = "用户状态修改请求")
public record UserStatusChangeRequest(
        UserStatus status
) {
}
