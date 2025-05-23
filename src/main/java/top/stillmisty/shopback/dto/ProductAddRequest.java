package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Schema(description = "添加商品请求体")
public record ProductAddRequest(
        @Schema(description = "商品名称", example = "商品名称")
        String productName,
        @Schema(description = "类别ID列表")
        Set<UUID> productCategories,
        @Schema(description = "商品生产商", example = "小米")
        String productMerchant,
        @Schema(description = "商品描述", example = "商品描述")
        String productDescription,
        @Schema(description = "商品价格", example = "100.00")
        @Min(value = 0, message = "商品价格不能小于0")
        @Max(value = 99999999, message = "商品价格不能大于99999999")
        BigDecimal productPrice,
        @Schema(description = "商品折扣", example = "0.8")
        @Min(value = 0, message = "商品折扣不能小于0")
        @Max(value = 1, message = "商品折扣不能大于1")
        BigDecimal productDiscount,
        @Schema(description = "商品库存", example = "100")
        @Min(value = 0, message = "商品库存不能小于0")
        @Max(value = 10000, message = "商品库存不能大于100000")
        int productStock
) {
}
