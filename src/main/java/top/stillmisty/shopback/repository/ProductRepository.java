package top.stillmisty.shopback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByProductIsOffShelf(boolean isOffShelf, Pageable pageable);

    // 模糊查询商品名称和描述
    @Query("SELECT p FROM Product p WHERE " +
            "(:isOffShelf = false OR p.productIsOffShelf = :isOffShelf) AND " +
            "(LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> quickSearch(
            String keyword,
            Pageable pageable,
            boolean isOffShelf
    );

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.productCategories c WHERE " +
            "(:keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:category = '' OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:merchant = '' OR LOWER(p.productMerchant) LIKE LOWER(CONCAT('%', :merchant, '%'))) AND " +
            "(:minPrice IS NULL OR p.productPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.productPrice <= :maxPrice) AND " +
            "(p.productIsOffShelf = :includeOffShelf)")
    Page<Product> advancedSearch(
            String keyword,
            String category,
            String merchant,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean includeOffShelf,
            Pageable pageable
    );
}
