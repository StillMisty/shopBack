package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import top.stillmisty.shopback.enums.ProductSearchType;

import java.util.List;

@Schema(description = "商品查询请求")
public record ProductQueryRequest(
        @Schema(description = "查询关键字", example = "小米苏琪")
        @Size(min = 1, max = 50, message = "关键字长度必须在1-20之间")
        String keyword,

        @Schema(description = "查询分类", example = "手机")
        @Size(min = 1, max = 20, message = "分类长度必须在1-20之间")
        List<String> category,

        @Schema(description = "查询的是商家还是商品", example = "商家")
        ProductSearchType type
) {
}
