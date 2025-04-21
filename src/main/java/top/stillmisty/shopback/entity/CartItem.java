package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CartItem {
    @Column(nullable = false)
    @Schema(description = "是否选中")
    boolean checked;
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @Schema(description = "购物车商品 ID")
    private UUID cartItemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "商品")
    private Product product;

    @Schema(description = "商品数量")
    @Column(nullable = false)
    private Integer quantity;

    public CartItem(Users user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CartItem cartItem = (CartItem) o;
        return getCartItemId() != null && Objects.equals(getCartItemId(), cartItem.getCartItemId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
