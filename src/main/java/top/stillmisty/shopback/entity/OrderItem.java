package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
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

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal unitDiscount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitDiscount = unitDiscount;
        this.subtotal = unitPrice.multiply(new BigDecimal(quantity)).multiply(unitDiscount);
    }
}