package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;
import top.stillmisty.shopback.enums.TransactionStatus;
import top.stillmisty.shopback.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions")
@Data
@NoArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue
    @Schema(description = "交易 ID")
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Column(nullable = false)
    @Schema(description = "交易类型")
    private TransactionType type;

    @Column(nullable = false)
    @Schema(description = "交易状态")
    private TransactionStatus status;

    @Schema(description = "交易描述")
    private String description;

    // 用于支付时关联的订单ID
    @Schema(description = "可能的订单 ID")
    private UUID orderId;

    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    @Schema(description = "交易时间")
    private Instant createdAt;

    public WalletTransaction(Users user, BigDecimal amount, TransactionType type, String description) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.status = TransactionStatus.COMPLETED;
        this.createdAt = Instant.now();
    }
}