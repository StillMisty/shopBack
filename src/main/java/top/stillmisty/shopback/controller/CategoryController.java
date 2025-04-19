package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.CategoryDetail;
import top.stillmisty.shopback.entity.Category;
import top.stillmisty.shopback.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "类别", description = "类别相关接口")
@SecurityRequirements
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "获取类别详情")
    public CategoryDetail getCategory(@PathVariable UUID categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("/top")
    @Operation(summary = "获取所有顶级类别")
    public List<Category> getTopCategories() {
        return categoryService.getTopCategories();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索类别")
    public List<Category> searchCategories(@RequestParam String keyword) {
        return categoryService.searchCategories(keyword);
    }
}