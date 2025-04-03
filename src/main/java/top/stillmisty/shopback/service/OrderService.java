package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.entity.*;
import top.stillmisty.shopback.enums.OrderStatus;
import top.stillmisty.shopback.repository.AddressRepository;
import top.stillmisty.shopback.repository.CartItemRepository;
import top.stillmisty.shopback.repository.OrderRepository;
import top.stillmisty.shopback.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    // 计算订单总金额
    public BigDecimal calculateTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            BigDecimal price = orderItem.getProduct().getProductPrice();
            BigDecimal discount = orderItem.getProduct().getProductDiscount();
            BigDecimal quantity = new BigDecimal(orderItem.getQuantity());
            BigDecimal itemTotal = price.multiply(discount).multiply(quantity);
            total = total.add(itemTotal);
        }
        return total;
    }

    // 更新订单状态
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    // 获取订单列表
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    // 创建订单
    public Order createOrderFromCart(UUID userId, Long addressId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Address address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("地址不存在"));

        Order order = new Order(user, address.getAddress(), address.getName(), address.getPhone());
        // 根据用户ID获取购物车中的商品
        List<CartItem> cartItemsByUser = cartItemRepository.getCartItemsByUser(user);
        if (cartItemsByUser.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }
        // 遍历购物车商品，创建订单商品列表
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItemsByUser) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    product.getProductPrice(),
                    product.getProductDiscount()
            );
            orderItems.add(orderItem);
        }
        // 清空购物车
        cartItemRepository.deleteAll(cartItemsByUser);
        // 设置订单商品列表
        order.setOrderItems(orderItems);
        // 设置订单状态为待支付
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order.setOrderTime(LocalDateTime.now());
        BigDecimal total = calculateTotal(order);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    // 更新订单地址
    public Order updateOrderAddress(UUID orderId, AddressChangeRequest addressChangeRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        order.setAddress(addressChangeRequest.address());
        order.setName(addressChangeRequest.name());
        order.setPhone(addressChangeRequest.phone());

        return orderRepository.save(order);
    }
}
