package top.stillmisty.shopback.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.dto.OrderPageRequest;
import top.stillmisty.shopback.entity.*;
import top.stillmisty.shopback.enums.OrderStatus;
import top.stillmisty.shopback.repository.*;
import top.stillmisty.shopback.utils.AuthUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    private final WalletService walletService;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, UserRepository userRepository, AddressRepository addressRepository, ProductRepository productRepository, WalletService walletService) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.walletService = walletService;
    }

    /**
     * 计算订单总金额，对每个订单商品计算折扣后的价格四舍五入，保留两位小数，再乘以数量，最后相加
     *
     * @param order 订单
     * @return 订单总金额
     */
    private BigDecimal calculateTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            BigDecimal price = orderItem.getProduct().getProductPrice();
            BigDecimal discount = orderItem.getProduct().getProductDiscount();
            // 折扣后的价格，四舍五入，保留两位小数
            BigDecimal discountedPrice = price.multiply(discount).setScale(2, RoundingMode.HALF_UP);
            BigDecimal quantity = new BigDecimal(orderItem.getQuantity());
            BigDecimal itemTotal = discountedPrice.multiply(quantity);
            total = total.add(itemTotal);
        }
        return total;
    }

    /**
     * 检查订单是否属于当前用户
     *
     * @param order 订单
     */
    private void assertOrderBelongsToUser(Order order, UUID userId) {
        if (!order.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("只能操作自己的订单");
        }
    }

    /**
     * 获取当前用户的所有订单
     *
     * @return 订单列表
     */
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    /**
     * 创建订单
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 创建的订单
     */
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
            // 检查商品是否下架
            if (product.isProductIsOffShelf()) {
                throw new RuntimeException("商品已下架");
            }
            // 检查库存是否足够
            if (cartItem.getQuantity() > product.getProductStock()) {
                throw new RuntimeException("库存不足");
            }
            // 创建订单商品
            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    product.getProductPrice(),
                    product.getProductDiscount()
            );
            orderItems.add(orderItem);
            // 更新商品库存
            product.setProductStock(product.getProductStock() - cartItem.getQuantity());
            // 更新商品销量
            product.setProductSoldCount(product.getProductSoldCount() + cartItem.getQuantity());
            // 保存商品
            productRepository.save(product);
        }
        // 清空购物车
        cartItemRepository.deleteAll(cartItemsByUser);
        // 设置订单商品列表
        order.setOrderItems(orderItems);
        // 设置订单状态为待支付
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        BigDecimal total = calculateTotal(order);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    /**
     * 用户取消订单
     * 订单状态变更为已取消
     **/
    public Order cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("只能取消待支付的订单");
        }
        // 取消订单时，恢复商品库存
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setProductStock(product.getProductStock() + orderItem.getQuantity());
            productRepository.save(product);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    /**
     * 用户支付订单
     * 支付成功后，订单状态变更为已支付
     **/
    public Order payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("只能支付待支付的订单");
        }

        walletService.pay(userId, order.getTotalAmount(), orderId);

        // 支付成功
        order.setOrderStatus(OrderStatus.PAID);
        order.setPayTime(Instant.now());
        return orderRepository.save(order);
    }

    /**
     * 用户确认收货
     * 订单状态变更为已完成
     **/
    public Order confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (order.getOrderStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("只能确认已发货的订单");
        }
        order.setOrderStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    /**
     * 用户申请退款
     * 退款申请成功后，订单状态变更为退款中
     **/
    public Order refundOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new RuntimeException("只能申请已支付的订单退款");
        }
        // 改为退款中状态
        order.setOrderStatus(OrderStatus.REFUNDING);
        return orderRepository.save(order);
    }

    /**
     * 用户修改订单地址
     *
     * @param orderId              订单ID
     * @param addressChangeRequest 地址变更请求
     * @return 更新后的订单
     */
    public Order updateOrderAddress(UUID orderId, AddressChangeRequest addressChangeRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (!(order.getOrderStatus() == OrderStatus.PENDING_PAYMENT
                || order.getOrderStatus() == OrderStatus.PAID
                || order.getOrderStatus() == OrderStatus.PROCESSING)) {
            throw new RuntimeException("只能修改待支付、已支付或处理中订单的地址");
        }

        // 修改订单地址
        order.setAddress(addressChangeRequest.address());
        order.setName(addressChangeRequest.name());
        order.setPhone(addressChangeRequest.phone());

        return orderRepository.save(order);
    }

    /**
     * 商家确认退款
     * 退款成功后，订单状态变更为已退款
     **/
    public Order confirmRefund(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        // 检查订单是否属于当前用户
        UUID userId = AuthUtils.getCurrentUserId();
        assertOrderBelongsToUser(order, userId);
        // 检查订单状态
        if (order.getOrderStatus() != OrderStatus.REFUNDING) {
            throw new RuntimeException("只能确认退款中的订单");
        }
        // 恢复用户余额
        walletService.refund(userId, order.getTotalAmount(), orderId);
        order.setOrderStatus(OrderStatus.REFUNDED);
        return orderRepository.save(order);
    }

    /**
     * 商家确认发货
     * 发货成功后，订单状态变更为已发货
     **/
    public Order confirmDelivery(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("只能确认处理中的订单发货");
        }
        order.setOrderStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }

    /**
     * 商家处理订单
     * 处理成功后，订单状态变更为处理中
     **/
    public Order processOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new RuntimeException("只能处理已支付的订单");
        }
        order.setOrderStatus(OrderStatus.PROCESSING);
        return orderRepository.save(order);
    }

    /**
     * 商家获取所有订单
     *
     * @param orderPageRequest 分页请求参数
     * @return 分页订单列表
     */
    public Page<Order> getOrders(OrderPageRequest orderPageRequest) {
        Sort sort = Sort.by(orderPageRequest.sortDirection(), orderPageRequest.sortBy());
        Pageable pageable = PageRequest.of(orderPageRequest.page(), orderPageRequest.size(), sort);
        return orderRepository.findAll(pageable);
    }

    /**
     * 商家获取所有订单
     *
     * @param orderStatus      订单状态
     * @param orderPageRequest 分页请求参数
     * @return 分页订单列表
     */
    public Page<Order> getOrdersByStatus(OrderStatus orderStatus, OrderPageRequest orderPageRequest) {
        Sort sort = Sort.by(orderPageRequest.sortDirection(), orderPageRequest.sortBy());
        Pageable pageable = PageRequest.of(orderPageRequest.page(), orderPageRequest.size(), sort);
        return orderRepository.findAllByOrderStatus(orderStatus, pageable);
    }
}
