package top.stillmisty.shopback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue
    private UUID ProductId;
    // 商品名称
    private String ProductName;
    // 商品图片 URL
    private String ProductImage;
    // 商品分类
    private String ProductCategory;
    // 商品商家
    private String ProductMerchant;
    // 商品描述
    private String ProductDescription;
    // 商品价格
    private String ProductPrice;
    // 商品折扣
    private String ProductDiscount;
    // 商品销量
    private String ProductSoldCount;
    // 商品库存
    private String ProductStock;

    public Product(String productName, String productImage, String productCategory, String productMerchant, String productDescription, String productPrice, String productDiscount, String productSoldCount, String productStock) {
        ProductName = productName;
        ProductImage = productImage;
        ProductCategory = productCategory;
        ProductMerchant = productMerchant;
        ProductDescription = productDescription;
        ProductPrice = productPrice;
        ProductDiscount = productDiscount;
        ProductSoldCount = productSoldCount;
        ProductStock = productStock;
    }

    public Product() {
    }
}
