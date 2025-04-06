package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue
    @Schema(description = "商品 ID")
    private UUID productId;

    @Column(nullable = false, length = 100)
    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片 URL")
    private String productImage;

    @Schema(description = "商品分类")
    @Column(nullable = false, length = 50)
    private String productCategory;

    @Schema(description = "商品商家")
    @Column(nullable = false, length = 50)
    private String productMerchant;

    @Schema(description = "商品描述")
    @Column(nullable = false, length = 500)
    private String productDescription;

    @Schema(description = "商品价格")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Schema(description = "商品折扣(0-1)")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal productDiscount;

    @Schema(description = "商品销量")
    @Column(nullable = false)
    private int productSoldCount;

    @Schema(description = "商品库存")
    @Column(nullable = false)
    private int productStock;

    @Schema(description = "是否下架")
    @Column(nullable = false)
    private boolean productIsOffShelf;

    @Schema(description = "上架时间")
    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    private Instant productOnShelfTime;

    public Product(
            String productName, String productCategory, String productMerchant,
            String productDescription, BigDecimal productPrice, BigDecimal productDiscount,
            int productStock
    ) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productMerchant = productMerchant;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productStock = productStock;
        productSoldCount = 0;
        productIsOffShelf = false;
        productOnShelfTime = Instant.now();
    }

    public Product() {
    }
}