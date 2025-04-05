package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.ProductAddRequest;
import top.stillmisty.shopback.dto.ProductChangeRequest;
import top.stillmisty.shopback.dto.ProductPageRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.service.ProductService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/product")
@Tag(name = "管理员-商品管理接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/list")
    @Operation(summary = "分页获取所有产品")
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
            @Valid @RequestBody ProductPageRequest productPageRequest
    ) {
        Page<Product> products = productService.getProductsWithSort(productPageRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping()
    @Operation(summary = "上架新商品")
    public ResponseEntity<ApiResponse<Product>> addProduct(
            @Valid @RequestBody ProductAddRequest productAddRequest
    ) {
        Product product = productService.addProduct(productAddRequest);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据商品 ID 获取商品信息")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品信息")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductChangeRequest productChangeRequest
    ) {
        Product product = productService.updateProduct(id, productChangeRequest);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "更新商品图片")
    public ResponseEntity<ApiResponse<Product>> updateProductImage(
            @PathVariable UUID id,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        Product product = productService.updateProductImage(id, image);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
}
