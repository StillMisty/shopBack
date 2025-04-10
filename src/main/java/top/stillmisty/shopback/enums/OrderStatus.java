package top.stillmisty.shopback.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "订单状态")
public enum OrderStatus {
    PENDING_PAYMENT,    // 待支付
    PAID,               // 已支付
    PROCESSING,         // 处理中
    SHIPPED,            // 已发货
    COMPLETED,          // 已完成
    CANCELLED,          // 已取消
    REFUNDING,          // 退款中
    REFUNDED            // 已退款
}
