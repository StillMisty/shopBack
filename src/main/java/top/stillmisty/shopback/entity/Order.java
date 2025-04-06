package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "订单 ID")
    private UUID orderId;

    // 用户 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;


    @Schema(description = "收货地址")
    private String address;

    @Schema(description = "收件人姓名")
    private String name;

    @Schema(description = "收件人电话")
    private String phone;

    @Schema(description = "订单状态")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "订单商品列表")
    private List<OrderItem> orderItems = new ArrayList<>();

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

    public Order() {

    }
}

