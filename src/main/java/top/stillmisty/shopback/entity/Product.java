package top.stillmisty.shopback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue
    private UUID productId;
    // 商品名称
    private String productName;
    // 商品图片 URL
    private String productImage;
    // 商品分类
    private String productCategory;
    // 商品商家
    private String productMerchant;
    // 商品描述
    private String productDescription;
    // 商品价格
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;
    // 商品折扣(0-1)
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal productDiscount;
    // 商品销量
    private int productSoldCount;
    // 商品库存
    private int productStock;

    public Product(
            String productName, String productImage, String productCategory, String productMerchant,
            String productDescription, BigDecimal productPrice, BigDecimal productDiscount,
            int productSoldCount, int productStock
    ) {
        this.productName = productName;
        this.productImage = productImage;
        this.productCategory = productCategory;
        this.productMerchant = productMerchant;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productSoldCount = productSoldCount;
        this.productStock = productStock;
    }

    public Product() {
    }
}