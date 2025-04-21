package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.CartSelectionRequest;
import top.stillmisty.shopback.dto.CartUpdateRequest;
import top.stillmisty.shopback.entity.CartItem;
import top.stillmisty.shopback.service.CartService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "购物车", description = "购物车相关接口")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "获取购物车列表")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCarts() {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        List<CartItem> cartItemList = cartService.getCartByUserId(userId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItemList));
    }

    @PostMapping
    @Operation(summary = "添加或修改购物车商品", description = "可以选择设置商品数量或增加商品数量")
    public ResponseEntity<ApiResponse<CartItem>> changeCart(
            @Valid @RequestBody
            CartUpdateRequest cartUpdateRequest
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        CartItem cartItem = cartService.changeCartCount(userId, cartUpdateRequest);
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItem));
    }

    @PatchMapping("/{cartItemId}/checked")
    @Operation(summary = "修改购物车项是否选中")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItemSelection(
            @PathVariable UUID cartItemId,
            @RequestBody CartSelectionRequest selectionRequest
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        CartItem cartItem = cartService.updateCartItemSelection(userId, cartItemId, selectionRequest.checked());
        return ResponseEntity.ok()
                .body(ApiResponse.success(cartItem));
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "删除购物车项")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable UUID cartItemId) {
        UUID userId = AuthUtils.getCurrentUserId();
        cartService.deleteCartItem(userId, cartItemId);
    }

    @DeleteMapping("/all")
    @Operation(summary = "删除购物车中所有商品")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart() {
        UUID userId = AuthUtils.getCurrentUserId();
        cartService.clearCart(userId);
    }
}
