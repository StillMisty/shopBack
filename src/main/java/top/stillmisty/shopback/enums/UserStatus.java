package top.stillmisty.shopback.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户状态")
public enum UserStatus {
    NORMAL("正常"),
    FROZEN("冻结"),
    BLACKLISTED("黑名单");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
