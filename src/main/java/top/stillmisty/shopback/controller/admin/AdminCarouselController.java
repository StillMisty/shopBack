package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.CarouselCreateRequest;
import top.stillmisty.shopback.entity.Carousel;
import top.stillmisty.shopback.service.CarouselService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/carousel")
@Tag(name = "管理员-轮播图管理接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarouselController {

    private final CarouselService carouselService;

    public AdminCarouselController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @GetMapping
    @Operation(summary = "管理员获取所有轮播图")
    public ResponseEntity<ApiResponse<List<Carousel>>> getAllCarousels() {
        List<Carousel> carousels = carouselService.getAllCarousels();
        return ResponseEntity.ok(ApiResponse.success(carousels));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "添加轮播图")
    public ResponseEntity<ApiResponse<Carousel>> addCarousel(
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("data") CarouselCreateRequest request
    ) {
        Carousel savedCarousel = carouselService.createCarouselWithImage(imageFile, request);
        return ResponseEntity.ok(ApiResponse.success(savedCarousel));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "更新轮播图")
    public ResponseEntity<ApiResponse<Carousel>> updateCarousel(
            @PathVariable UUID id,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestPart("data") CarouselCreateRequest request
    ) {
        Carousel updatedCarousel = carouselService.updateCarousel(id, imageFile, request);
        return ResponseEntity.ok(ApiResponse.success(updatedCarousel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除轮播图")
    public ResponseEntity<ApiResponse<Void>> deleteCarousel(@PathVariable UUID id) {
        carouselService.deleteCarousel(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "启用或禁用轮播图")
    public ResponseEntity<ApiResponse<Carousel>> toggleCarouselStatus(
            @PathVariable UUID id,
            @RequestParam boolean enabled
    ) {
        Carousel carousel = carouselService.toggleCarouselStatus(id, enabled);
        return ResponseEntity.ok(ApiResponse.success(carousel));
    }
}