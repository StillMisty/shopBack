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

    public List<Carousel> getHomeCarousels() {
        return carouselRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public List<Carousel> getAllCarousels() {
        return carouselRepository.findAll();
    }

    public Carousel saveCarousel(Carousel carousel) {
        return carouselRepository.save(carousel);
    }

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

    public Carousel toggleCarouselStatus(UUID id, boolean enabled) {
        Carousel carousel = carouselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));
        carousel.setEnabled(enabled);

        try {
            return carouselRepository.save(carousel);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new RuntimeException("数据已被其他用户修改，请刷新后重试", ex);
        }
    }

    private String uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("文件不能为空");
            }

            if (PictureUtils.isValidPicture(file)) {
                throw new RuntimeException("文件类型不合法");
            }

            // 生成唯一文件名
            String filename = IdUtil.simpleUUID() + PictureUtils.getFileExtension(file);
            Path filePath = carouselPath.resolve(filename);

            // 保存文件
            Files.copy(file.getInputStream(), filePath);

            // 返回可访问的URL
            return baseUrl + "/public/carousels/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("无法保存上传的图片", e);
        }
    }
}