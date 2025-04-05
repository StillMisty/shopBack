package top.stillmisty.shopback.service;

import org.springframework.data.domain.Page;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.entity.WalletTransaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    Users recharge(UUID userId, BigDecimal amount, String description);

    Users pay(UUID userId, BigDecimal amount, UUID orderId);

    Users refund(UUID userId, BigDecimal amount, UUID orderId);

    Page<WalletTransaction> getTransactionHistory(UUID userId, int page, int size);
}