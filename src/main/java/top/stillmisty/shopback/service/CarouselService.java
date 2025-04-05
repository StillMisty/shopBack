package top.stillmisty.shopback.service;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.CarouselCreateRequest;
import top.stillmisty.shopback.entity.Carousel;
import top.stillmisty.shopback.repository.CarouselRepository;
import top.stillmisty.shopback.utils.PictureUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CarouselService {

    private final CarouselRepository carouselRepository;


    private final Path carouselPath;

    @Value("${app.base-url}")
    private String baseUrl;

    public CarouselService(CarouselRepository carouselRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.carouselRepository = carouselRepository;
        this.carouselPath = Paths.get(uploadDir + "/carousels");
        try {
            if (!Files.exists(carouselPath)) {
                Files.createDirectories(carouselPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建轮播图目录", e);
        }
    }

    /**
     * 获取所有启用的轮播图
     *
     * @return 启用的轮播图列表
     */
    public List<Carousel> getHomeCarousels() {
        return carouselRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    /**
     * 获取所有轮播图
     *
     * @return 所有轮播图列表
     */
    public List<Carousel> getAllCarousels() {
        return carouselRepository.findAll();
    }

    /**
     * 创建新的轮播图
     *
     * @param imageFile 上传的图片文件
     * @param request   轮播图创建请求
     * @return 创建的轮播图
     */
    public Carousel createCarouselWithImage(MultipartFile imageFile, CarouselCreateRequest request) {
        String imageUrl = uploadImage(imageFile);

        Carousel carousel = new Carousel();
        carousel.setImageUrl(imageUrl);
        carousel.setLinkUrl(request.linkUrl());
        carousel.setTitle(request.title());
        carousel.setSortOrder(request.sortOrder());
        carousel.setEnabled(request.enabled());

        return carouselRepository.save(carousel);
    }

    /**
     * 更新轮播图
     *
     * @param id        轮播图ID
     * @param imageFile 上传的图片文件
     * @param request   轮播图更新请求
     * @return 更新后的轮播图
     */
    public Carousel updateCarousel(UUID id, MultipartFile imageFile, CarouselCreateRequest request) {
        Carousel existingCarousel = carouselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));

        // 如果上传了新图片，先处理图片上传
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            existingCarousel.setImageUrl(imageUrl);
        }

        // 更新其他字段
        existingCarousel.setLinkUrl(request.linkUrl());
        existingCarousel.setTitle(request.title());
        existingCarousel.setSortOrder(request.sortOrder());
        existingCarousel.setEnabled(request.enabled());

        try {
            return carouselRepository.save(existingCarousel);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new RuntimeException("数据已被其他用户修改，请刷新后重试", ex);
        }
    }

    public void deleteCarousel(UUID id) {
        carouselRepository.deleteById(id);
    }

    /**
     * 上传图片并返回可访问的URL
     *
     * @param file 上传的图片文件
     * @return 可访问的图片URL
     */
    private String uploadImage(MultipartFile file) {
        try {

            String filename = PictureUtils.savePicture(IdUtil.simpleUUID(), file, carouselPath);

            // 返回可访问的URL
            return baseUrl + "/public/carousels/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("无法保存上传的图片", e);
        }
    }
}