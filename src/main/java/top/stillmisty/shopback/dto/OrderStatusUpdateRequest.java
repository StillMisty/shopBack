package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import top.stillmisty.shopback.enums.OrderStatus;

@Schema(description = "订单状态更新请求")
public record OrderStatusUpdateRequest(
        @Schema(description = "订单状态", example = "DELIVERED")
        OrderStatus status
) {
}
