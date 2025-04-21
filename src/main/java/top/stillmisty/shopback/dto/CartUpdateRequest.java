package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.util.UUID;

@Schema(description = "购物车商品数量更新请求")
public record CartUpdateRequest(
        @Schema(description = "商品 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID productId,

        @Schema(description = "要修改到的数量", example = "2")
        @Min(value = 1, message = "数量必须大于等于 1")
        int quantity,

        @Schema(description = "是否增量模式(true:增加数量,false:设置数量,默认为false)", example = "false")
        boolean increment
) {
    public CartUpdateRequest(UUID productId, int quantity) {
        this(productId, quantity, false);
    }
}
