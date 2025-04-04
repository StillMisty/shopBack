package top.stillmisty.shopback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.stillmisty.shopback.entity.Carousel;

import java.util.List;
import java.util.UUID;

public interface CarouselRepository extends JpaRepository<Carousel, UUID> {
    List<Carousel> findByEnabledTrueOrderBySortOrderAsc();
}