package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.CheckoutRequest;
import top.stillmisty.shopback.entity.Order;
import top.stillmisty.shopback.enums.OrderStatus;
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

    @PostMapping("/pay/{orderId}")
    @Operation(summary = "支付订单")
    public ResponseEntity<ApiResponse<Order>> pay(@PathVariable UUID orderId) {
        Order order = orderService.updateOrderStatus(orderId, OrderStatus.PAID);
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

}
