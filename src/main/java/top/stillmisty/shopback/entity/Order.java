package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;
import top.stillmisty.shopback.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;

    // 用户 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    // 地址
    private String address;
    private String name;
    private String phone;

    // 订单状态
    private OrderStatus orderStatus;

    // 订单商品列表
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    // 下单时间
    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    private Instant orderTime;
    // 订单总金额
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    // 订单支付时间
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    private Instant payTime;

    public Order(Users user, String address, String name, String phone) {
        this.user = user;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.orderTime = Instant.now();
    }

    public Order() {

    }
}

