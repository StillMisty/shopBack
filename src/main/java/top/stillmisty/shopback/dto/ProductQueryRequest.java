package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Schema(description = "商品高级搜索请求")
public record ProductQueryRequest(
        @Schema(description = "查询关键字", example = "小米手机")
        @Size(max = 50, message = "关键字长度必须在0-50之间")
        String keyword,

        @Schema(description = "查询分类列表", example = "手机 电子产品")
        String category,

        @Schema(description = "查询商家", example = "米哈游株式会社")
        String merchant,

        @Schema(description = "最低价格", example = "100.00")
        @Min(value = 0, message = "最低价格不能小于0")
        @Max(value = 99999999, message = "商品价格不能大于99999999")
        BigDecimal minPrice,

        @Schema(description = "最高价格", example = "999.99")
        @Min(value = 0, message = "最高价格不能小于0")
        @Max(value = 99999999, message = "商品价格不能大于99999999")
        BigDecimal maxPrice,

        @Schema(description = "是否包含下架商品", example = "false")
        Boolean includeOffShelf,

        @Schema(description = "页码", example = "0")
        @Min(value = 0, message = "页码不能小于0")
        Integer page,

        @Schema(description = "每页大小", example = "10")
        @Min(value = 1, message = "每页大小不能小于1")
        @Max(value = 100, message = "每页大小不能大于100")
        Integer size,

        @Schema(description = "排序字段", example = "productOnShelfTime")
        String sortBy,

        @Schema(description = "排序方向", example = "DESC")
        Sort.Direction direction
) {
    public ProductQueryRequest {
        if (includeOffShelf == null) includeOffShelf = false;
        if (keyword == null) keyword = "";
        if (category == null) category = "";
        if (merchant == null) merchant = "";
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortBy == null) sortBy = "productOnShelfTime";
        if (direction == null) direction = Sort.Direction.DESC;
    }
}