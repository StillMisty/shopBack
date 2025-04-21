package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.CartUpdateRequest;
import top.stillmisty.shopback.entity.CartItem;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.CartItemRepository;
import top.stillmisty.shopback.repository.ProductRepository;
import top.stillmisty.shopback.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository, WalletService walletService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * 获取用户购物车中项目
     *
     * @param userId 用户ID
     * @return 购物车
     */
    public List<CartItem> getCartByUserId(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 获取用户的购物车
        return cartItemRepository.getCartItemsByUser(user);

    }

    /**
     * 添加或更新购物车中的商品数量
     *
     * @param userId            用户ID
     * @param cartUpdateRequest 购物车更新请求
     * @return 更新后的购物车项
     */
    public CartItem changeCartCount(UUID userId, CartUpdateRequest cartUpdateRequest) {
        // 先获取产品信息
        Product product = productRepository.findById(cartUpdateRequest.productId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 根据用户和商品查找购物车项
        CartItem cartItem = cartItemRepository.findCartItemsByUser(user).stream()
                .filter(item -> item.getProduct().getProductId().equals(cartUpdateRequest.productId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            // 如果购物车中没有该商品，则添加
            cartItem = new CartItem(user, product, cartUpdateRequest.quantity());
        } else {
            // 如果购物车中已经有该商品，则更新数量
            if (cartUpdateRequest.increment()) {
                // 增量模式：增加数量
                cartItem.setQuantity(cartItem.getQuantity() + cartUpdateRequest.quantity());
            } else {
                // 设置模式：直接设置数量
                cartItem.setQuantity(cartUpdateRequest.quantity());
            }
        }

        return cartItemRepository.save(cartItem);
    }

    /**
     * 清空购物车中的商品
     *
     * @param userId 用户ID
     */
    public void clearCart(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 清空购物车中的商品
        cartItemRepository.deleteAll(cartItemRepository.findCartItemsByUser(user));
    }

    public void deleteCartItem(UUID userId, UUID cartItemId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 删除购物车中的商品
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));
        if (cartItem.getUser().equals(user)) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("无法删除其他用户的购物车项");
        }
    }

    public CartItem updateCartItemSelection(UUID userId, UUID cartItemId, boolean selected) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 更新购物车项的选中状态
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));
        if (cartItem.getUser().equals(user)) {
            cartItem.setChecked(selected);
            return cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("无法更新其他用户的购物车项");
        }
    }
}
