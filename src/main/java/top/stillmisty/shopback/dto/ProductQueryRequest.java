package top.stillmisty.shopback.dto;

import top.stillmisty.shopback.enums.ProductSearchType;

import java.util.List;

public record ProductQueryRequest(
        String keyword,
        List<String> category,
        ProductSearchType type
) {
}
