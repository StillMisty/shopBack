package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import top.stillmisty.shopback.entity.Address;
import top.stillmisty.shopback.entity.Order;
import top.stillmisty.shopback.entity.OrderItem;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.enums.OrderStatus;
import top.stillmisty.shopback.repository.OrderRepository;
import top.stillmisty.shopback.repository.ProductRepository;
import top.stillmisty.shopback.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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

    // 添加订单
    public void addOrder(UUID userID, List<UUID> productIDs, Integer quantity, Address address) {
        Users user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Order order = new Order(user, address.getAddress(), address.getName(), address.getPhone());
        List<OrderItem> orderItems = new ArrayList<>();
        for (UUID productID : productIDs) {
            productRepository.findById(productID)
                    .ifPresent(product -> {
                        OrderItem orderItem = new OrderItem(product, quantity, product.getProductPrice(), product.getProductDiscount());
                        orderItems.add(orderItem);
                    });
        }
        order.setOrderItems(orderItems);  // 设置订单商品列表
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);  // 设置订单状态为待支付
        order.setOrderTime(LocalDateTime.now());  // 设置下单时间
        BigDecimal total = calculateTotal(order);  // 计算订单总金额
        order.setTotalAmount(total);  // 设置订单总金额
        orderRepository.save(order);
    }

    // 更新订单状态
    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    // 获取订单列表
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUser_UserId(userId);
    }
}
