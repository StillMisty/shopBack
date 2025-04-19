package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Schema(description = "商品所属类别")
    @ToString.Exclude
    @JsonIgnore
    private Set<Category> productCategories = new HashSet<>();

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
            String productName, Set<Category> productCategories, String productMerchant,
            String productDescription, BigDecimal productPrice, BigDecimal productDiscount,
            int productStock
    ) {
        this.productName = productName;
        this.productCategories = productCategories;
        this.productMerchant = productMerchant;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productStock = productStock;
        productSoldCount = 0;
        productIsOffShelf = false;
        productOnShelfTime = Instant.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getProductId() != null && Objects.equals(getProductId(), product.getProductId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}