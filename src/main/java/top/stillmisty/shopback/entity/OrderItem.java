package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    @Schema(description = "订单商品 ID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    @Schema(description = "订单")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "商品")
    private Product product;

    @Schema(description = "商品数量")
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "下单时商品单价")
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 3, scale = 2)
    @Schema(description = "下单时商品折扣(0-1)")
    private BigDecimal unitDiscount;

    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(description = "小计")
    private BigDecimal subtotal;

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal unitDiscount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitDiscount = unitDiscount;
        this.subtotal = unitPrice.multiply(new BigDecimal(quantity)).multiply(unitDiscount);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderItem orderItem = (OrderItem) o;
        return getId() != null && Objects.equals(getId(), orderItem.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}