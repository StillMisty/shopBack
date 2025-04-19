package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.CategoryAddRequest;
import top.stillmisty.shopback.dto.CategoryDetail;
import top.stillmisty.shopback.dto.CategoryUpdateRequest;
import top.stillmisty.shopback.entity.Category;
import top.stillmisty.shopback.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "管理员-类别管理接口", description = "类别的增删改查")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "获取类别详情")
    public CategoryDetail getCategory(@PathVariable UUID categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加类别")
    public Category addCategory(@Valid @RequestBody CategoryAddRequest request) {
        return categoryService.addCategory(request);
    }

    @PutMapping
    @Operation(summary = "更新类别")
    public Category updateCategory(@Valid @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategory(request);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "删除类别")
    public void deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping
    @Operation(summary = "获取所有类别")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "获取指定层级的所有类别")
    public List<Category> getCategoriesByLevel(@PathVariable Integer level) {
        return categoryService.getCategoriesByLevel(level);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索类别")
    public List<Category> searchCategories(@RequestParam String keyword) {
        return categoryService.searchCategories(keyword);
    }
}