package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.OrderPageRequest;
import top.stillmisty.shopback.dto.OrderStatusUpdateRequest;
import top.stillmisty.shopback.entity.Order;
import top.stillmisty.shopback.enums.OrderStatus;
import top.stillmisty.shopback.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/order")
@Tag(name = "管理员-订单管理接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/list")
    @Operation(summary = "分页获取所有状态的订单信息")
    public ResponseEntity<ApiResponse<Page<Order>>> getOrders(
            @Valid @RequestBody OrderPageRequest orderPageRequest
    ) {
        Page<Order> orders = orderService.getOrders(orderPageRequest);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PostMapping("/{status}")
    @Operation(summary = "根据订单状态分页获取订单信息")
    public ResponseEntity<ApiResponse<Page<Order>>> getOrders(
            @PathVariable OrderStatus status,
            @Valid @RequestBody OrderPageRequest orderPageRequest
    ) {
        Page<Order> orders = orderService.getOrdersByStatus(status, orderPageRequest);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "更新订单状态，更新状态为REFUNDING -> REFUNDED、PAID -> PROCESSING、PROCESSING -> SHIPPED")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        Order order;
        switch (request.status()) {
            case REFUNDED:
                order = orderService.confirmRefund(orderId);
                break;
            case PROCESSING:
                order = orderService.processOrder(orderId);
                break;
            case SHIPPED:
                order = orderService.confirmDelivery(orderId);
                break;
            default:
                return ResponseEntity.badRequest().body(ApiResponse.error("不支持的状态更新"));
        }
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}
