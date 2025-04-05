package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.ProductPageRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.service.ProductService;

import java.util.UUID;

@RestController
@SecurityRequirements
@RequestMapping("/api/product")
@Tag(name = "商品信息", description = "商品信息相关接口")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/list")
    @Operation(summary = "分页获取未下架的商品列表")
    public ResponseEntity<ApiResponse<Page<Product>>> getProducts(
            @Valid @RequestBody ProductPageRequest productPageRequest
    ) {
        Page<Product> products = productService.getProductsOnShelfWithSort(productPageRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据商品 ID 获取商品信息")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
}
