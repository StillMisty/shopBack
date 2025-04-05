package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private TransactionStatus status;

    private String description;

    // 用于支付时关联的订单ID
    private UUID orderId;

    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
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