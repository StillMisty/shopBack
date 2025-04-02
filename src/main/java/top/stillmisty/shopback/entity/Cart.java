package top.stillmisty.shopback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue
    private UUID cartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // 添加购物车项的便捷方法
    public void addCartItem(Product product, Integer quantity) {
        CartItem cartItem = new CartItem(product, this, quantity);
        cartItems.add(cartItem);
    }
}
