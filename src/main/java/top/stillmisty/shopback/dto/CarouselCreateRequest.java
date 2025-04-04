package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "轮播图修改请求")
public record CarouselCreateRequest(
        @Schema(description = "跳转链接")
        String linkUrl,

        @Schema(description = "标题")
        String title,

        @Schema(description = "排序号")
        Integer sortOrder,

        @Schema(description = "是否启用")
        Boolean enabled
) {
}
