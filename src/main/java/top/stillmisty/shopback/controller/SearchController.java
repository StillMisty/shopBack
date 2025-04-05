package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.ProductQueryRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.service.SearchService;

@RestController
@RequestMapping("/api/search")
@Tag(name = "搜索服务", description = "商品搜索接口")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/products")
    @Operation(summary = "高级商品搜索", description = "支持多条件、排序和分页")
    public ResponseEntity<ApiResponse<Page<Product>>> searchProducts(
            @Valid @RequestBody ProductQueryRequest request
    ) {
        Page<Product> products = searchService.searchProducts(request);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/quick")
    @Operation(summary = "快速搜索", description = "简单关键字搜索")
    public ResponseEntity<ApiResponse<Page<Product>>> quickSearch(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = searchService.quickSearch(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}
