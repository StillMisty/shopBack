package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.WalletRechargeRequest;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.entity.WalletTransaction;
import top.stillmisty.shopback.service.WalletService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "钱包管理", description = "钱包充值与交易记录")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PatchMapping("/recharge")
    @Operation(summary = "充值钱包余额")
    public ResponseEntity<ApiResponse<Users>> rechargeWallet(
            @RequestBody WalletRechargeRequest request
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        Users users = walletService.recharge(userId, request.amount(), "用户充值");
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/transactions")
    @Operation(summary = "获取交易记录")
    public ResponseEntity<ApiResponse<Page<WalletTransaction>>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        Page<WalletTransaction> transactions = walletService.getTransactionHistory(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}