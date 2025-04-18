package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.ProductQueryRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.service.SearchService;

@RestController
@RequestMapping("/api/search")
@SecurityRequirements
@Tag(name = "搜索服务", description = "商品搜索接口")
@Validated
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
            @RequestParam
            @Parameter(description = "搜索关键字")
            @Size(min = 1, max = 100, message = "关键字长度必须在 1 到 100 个字符之间")
            String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") @Min(0)
            int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100)
            int size
    ) {
        Page<Product> products = searchService.quickSearch(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}
