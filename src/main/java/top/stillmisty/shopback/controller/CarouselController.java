package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.entity.Carousel;
import top.stillmisty.shopback.service.CarouselService;

import java.util.List;

@RestController
@RequestMapping("/api/carousel")
@Tag(name = "轮播图相关接口")
public class CarouselController {

    private final CarouselService carouselService;

    public CarouselController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @GetMapping("/home")
    @Operation(summary = "获取首页轮播图")
    public ResponseEntity<ApiResponse<List<Carousel>>> getHomeCarousels() {
        List<Carousel> carousels = carouselService.getHomeCarousels();
        return ResponseEntity.ok()
                .body(ApiResponse.success(carousels));
    }
}