package top.stillmisty.shopback.enums;

public enum OrderStatus {
    PENDING_PAYMENT("待支付"),
    PAID("已支付"),
    PROCESSING("处理中"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    REFUNDING("退款中"),
    REFUNDED("已退款");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
