package top.stillmisty.shopback.enums;

public enum ProductSearchType {
    PRODUCT("商品"),
    MERCHANT("商家");

    private final String description;

    ProductSearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}