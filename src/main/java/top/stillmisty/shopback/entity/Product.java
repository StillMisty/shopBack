package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private UUID productId;
    // 商品名称
    @Column(nullable = false, length = 100)
    private String productName;
    // 商品图片 URL
    private String productImage;
    // 商品分类
    @Column(nullable = false, length = 50)
    private String productCategory;
    // 商品商家
    @Column(nullable = false, length = 50)
    private String productMerchant;
    // 商品描述
    @Column(nullable = false, length = 500)
    private String productDescription;
    // 商品价格
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;
    // 商品折扣(0-1)
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal productDiscount;
    // 商品销量
    @Column(nullable = false)
    private int productSoldCount;
    // 商品库存
    @Column(nullable = false)
    private int productStock;
    // 是否下架
    @Column(nullable = false)
    private boolean productIsOffShelf;
    // 上架时间
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