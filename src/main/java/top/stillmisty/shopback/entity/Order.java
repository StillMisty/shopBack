package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;
import top.stillmisty.shopback.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "订单商品列表")
    @ToString.Exclude
    private final List<OrderItem> orderItems = new ArrayList<>();
    @Id
    @GeneratedValue
    @Schema(description = "订单 ID")
    private UUID orderId;
    // 用户 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private Users user;
    @Schema(description = "收货地址")
    private String address;
    @Schema(description = "收件人姓名")
    private String name;
    @Schema(description = "收件人电话")
    private String phone;
    @Schema(description = "订单状态")
    private OrderStatus orderStatus;
    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    @Schema(description = "下单时间")
    private Instant orderTime;

    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @JsonSerialize(using = InstantToTimestampSerializer.class)
    @Schema(description = "支付时间")
    private Instant payTime;

    public Order(Users user, String address, String name, String phone) {
        this.user = user;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.orderTime = Instant.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getOrderId() != null && Objects.equals(getOrderId(), order.getOrderId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems.clear();
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                addOrderItem(orderItem);
            }
        }
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}

