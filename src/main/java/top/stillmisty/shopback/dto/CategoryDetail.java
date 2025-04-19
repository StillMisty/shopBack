package top.stillmisty.shopback.dto;

import top.stillmisty.shopback.entity.Category;

import java.util.Set;
import java.util.UUID;

public record CategoryDetail(
        UUID categoryId,
        String categoryName,
        Integer level,
        Integer sortOrder,
        Category parent,
        Set<Category> children
) {
}
