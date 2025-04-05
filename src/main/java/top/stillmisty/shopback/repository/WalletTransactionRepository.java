package top.stillmisty.shopback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.stillmisty.shopback.entity.WalletTransaction;
import top.stillmisty.shopback.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    Page<WalletTransaction> findByUserUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<WalletTransaction> findByUserUserIdAndTypeOrderByCreatedAtDesc(UUID userId, TransactionType type);
}