package top.stillmisty.shopback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentIsNull();

    List<Category> findByParentCategoryId(UUID parentId);

    List<Category> findByLevel(Integer level);

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> searchByName(String keyword);
}