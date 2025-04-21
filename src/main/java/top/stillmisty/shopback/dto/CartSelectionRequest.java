package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "购物车项选中状态更新请求")
public record CartSelectionRequest(
        @Schema(description = "是否选中", example = "true")
        boolean checked
) {
}