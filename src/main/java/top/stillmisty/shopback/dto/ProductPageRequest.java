package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "商品分页列表请求")
public record ProductPageRequest(
        @Schema(description = "页码", example = "1")
        int page,

        @Schema(description = "每页大小", example = "10")
        @Size(min = 1, max = 100, message = "每页大小必须在1-100之间")
        int size
) {
}
