package top.stillmisty.shopback.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.entity.WalletTransaction;
import top.stillmisty.shopback.enums.TransactionType;
import top.stillmisty.shopback.exception.InsufficientBalanceException;
import top.stillmisty.shopback.exception.UserNotFoundException;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.repository.WalletTransactionRepository;
import top.stillmisty.shopback.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletTransactionRepository transactionRepository;

    public WalletServiceImpl(UserRepository userRepository, WalletTransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Users recharge(UUID userId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        user.setWallet(user.getWallet().add(amount));
        WalletTransaction transaction = new WalletTransaction(user, amount, TransactionType.RECHARGE, description);

        transactionRepository.save(transaction);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Users pay(UUID userId, BigDecimal amount, UUID orderId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        if (user.getWallet().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("余额不足");
        }

        user.setWallet(user.getWallet().subtract(amount));
        WalletTransaction transaction = new WalletTransaction(user, amount, TransactionType.PAYMENT, "订单支付");
        transaction.setOrderId(orderId);

        transactionRepository.save(transaction);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Users refund(UUID userId, BigDecimal amount, UUID orderId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("退款金额必须大于0");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        user.setWallet(user.getWallet().add(amount));
        WalletTransaction transaction = new WalletTransaction(user, amount, TransactionType.REFUND, "订单退款");
        transaction.setOrderId(orderId);

        transactionRepository.save(transaction);
        return userRepository.save(user);
    }

    @Override
    public Page<WalletTransaction> getTransactionHistory(UUID userId, int page, int size) {
        return transactionRepository.findByUserUserIdOrderByCreatedAtDesc(
                userId,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }
}