package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/order")
@Tag(name = "订单信息", description = "订单信息相关接口")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "获取所有订单信息")
    public ResponseEntity<ApiResponse<List<Order>>> getOrder() {
        UUID userId = AuthUtils.getCurrentUserId();
        return ResponseEntity.ok()
                .body(ApiResponse.success(orderService.getOrdersByUserId(userId)));
    }

    @PostMapping("/checkout")
    @Operation(summary = "购物车结算创建订单")
    public ResponseEntity<ApiResponse<Order>> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        UUID userId = AuthUtils.getCurrentUserId();
        Order order = orderService.createOrderFromCart(userId, checkoutRequest.addressId());
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }

    @PutMapping("/pay/{orderId}")
    @Operation(summary = "支付订单，支付成功后订单状态变更为已支付")
    public ResponseEntity<ApiResponse<Order>> pay(@PathVariable UUID orderId) {
        Order order = orderService.payOrder(orderId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }

    @PutMapping("/address/{orderId}")
    @Operation(summary = "修改订单地址")
    public ResponseEntity<ApiResponse<Order>> updateAddress(@PathVariable UUID orderId, @RequestBody AddressChangeRequest addressChangeRequest) {
        Order order = orderService.updateOrderAddress(orderId, addressChangeRequest);
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }

    @PutMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单，未支付订单前可取消")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable UUID orderId) {
        Order order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }

    @PutMapping("/confirm/{orderId}")
    @Operation(summary = "确认收货，订单状态变更为已完成")
    public ResponseEntity<ApiResponse<Order>> confirmOrder(@PathVariable UUID orderId) {
        Order order = orderService.confirmOrder(orderId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }

    @PutMapping("/refund/{orderId}")
    @Operation(summary = "申请退款，订单状态变更为退款中")
    public ResponseEntity<ApiResponse<Order>> refundOrder(@PathVariable UUID orderId) {
        Order order = orderService.refundOrder(orderId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(order));
    }
}
