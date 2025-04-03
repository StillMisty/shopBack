package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
    @Id
    @GeneratedValue
    private UUID cartItemId;

    // 用户 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // 商品数量
    private Integer quantity;

    public CartItem(Users user, Product product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem() {

    }
}
