package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "购物车结算请求")
public record CheckoutRequest(
        @Schema(description = "地址 ID", example = "1")
        Long addressId
) {
}
