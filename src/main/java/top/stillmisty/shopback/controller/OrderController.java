package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.CheckoutRequest;
import top.stillmisty.shopback.entity.Order;
import top.stillmisty.shopback.service.OrderService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单信息", description = "订单信息相关接口")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @GetMapping
    @Operation(summary = "获取当前用户的所有订单")
    public ResponseEntity<ApiResponse<List<Order>>> getOrders() {
        UUID userId = AuthUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrdersByUserId(userId)));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情", description = "只允许获取自己的订单")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable UUID orderId) {
        UUID userId = AuthUtils.getCurrentUserId();
        Order order = orderService.getOrderById(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/checkout")
    @Operation(summary = "购物车结算创建订单")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody CheckoutRequest checkoutRequest) {
        UUID userId = AuthUtils.getCurrentUserId();
        Order order = orderService.createOrderFromCart(userId, checkoutRequest.addressId());
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/{orderId}/payment")
    @Operation(summary = "支付订单", description = "支付订单，订单状态会变为已支付，只有订单状态为PENDING_PAYMENT可用")
    public ResponseEntity<ApiResponse<Order>> payOrder(@PathVariable UUID orderId) {
        Order order = orderService.payOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/{orderId}/address")
    @Operation(summary = "修改订单地址")
    public ResponseEntity<ApiResponse<Order>> updateOrderAddress(
            @PathVariable UUID orderId,
            @Valid @RequestBody AddressChangeRequest addressChangeRequest
    ) {
        Order order = orderService.updateOrderAddress(orderId, addressChangeRequest);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/{orderId}/status/cancelled")
    @Operation(summary = "取消订单", description = "取消订单，订单状态会变为已取消，只有订单状态为PENDING_PAYMENT可用")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable UUID orderId) {
        Order order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/{orderId}/status/completed")
    @Operation(summary = "确认收货", description = "确认收货，订单状态会变为已完成，只有订单状态为SHIPPED可用")
    public ResponseEntity<ApiResponse<Order>> confirmOrder(@PathVariable UUID orderId) {
        Order order = orderService.confirmOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/{orderId}/status/refunding")
    @Operation(summary = "申请退款", description = "申请退款，订单状态会变为退款中，只有订单状态为PAID可用")
    public ResponseEntity<ApiResponse<Order>> refundOrder(@PathVariable UUID orderId) {
        Order order = orderService.refundOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}