package top.stillmisty.shopback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.stillmisty.shopback.dto.CategoryAddRequest;
import top.stillmisty.shopback.dto.CategoryDetail;
import top.stillmisty.shopback.dto.CategoryUpdateRequest;
import top.stillmisty.shopback.entity.Category;
import top.stillmisty.shopback.exception.BusinessException;
import top.stillmisty.shopback.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * 创建新类别
     */
    @Transactional
    public Category addCategory(CategoryAddRequest request) {
        // 检查父类别
        Category parent = null;
        if (request.parentId() != null) {
            parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new BusinessException("父类别不存在"));

            // 检查层级关系
            if (request.level() != parent.getLevel() + 1) {
                throw new BusinessException("类别层级设置错误，子类别层级应为父类别层级+1");
            }
        } else if (request.level() != 1) {
            throw new BusinessException("顶级类别的层级必须为1");
        }

        Category category = new Category(
                request.categoryName(),
                request.level(),
                parent
        );

        if (request.sortOrder() != null) {
            category.setSortOrder(request.sortOrder());
        }

        return categoryRepository.save(category);
    }

    /**
     * 更新类别
     */
    @Transactional
    public Category updateCategory(CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new BusinessException("类别不存在"));

        if (request.categoryName() != null && !request.categoryName().isBlank()) {
            category.setCategoryName(request.categoryName());
        }

        if (request.parentId() != null) {
            // 不能将类别设为自己的子类别的父类别（避免循环依赖）
            if (isChildCategory(category.getCategoryId(), request.parentId())) {
                throw new BusinessException("不能将类别设为自己子类别的父类别");
            }

            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new BusinessException("父类别不存在"));

            category.setParent(parent);
            category.setLevel(parent.getLevel() + 1);
        }

        if (request.sortOrder() != null) {
            category.setSortOrder(request.sortOrder());
        }

        return categoryRepository.save(category);
    }

    /**
     * 判断是否是子类别，用于防止循环依赖
     */
    private boolean isChildCategory(UUID parentId, UUID potentialChildId) {
        if (parentId.equals(potentialChildId)) {
            return true;
        }

        List<Category> children = categoryRepository.findByParentCategoryId(potentialChildId);
        for (Category child : children) {
            if (isChildCategory(parentId, child.getCategoryId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 删除类别
     */
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException("类别不存在"));

        // 检查是否有子类别
        if (!category.getChildren().isEmpty()) {
            throw new BusinessException("该类别下有子类别，无法删除");
        }

        categoryRepository.delete(category);
    }


    /**
     * 获取所有顶级类别
     */
    public List<Category> getTopCategories() {
        return categoryRepository.findByParentIsNull();
    }

    /**
     * 获取指定层级的所有类别
     */
    public List<Category> getCategoriesByLevel(Integer level) {
        return categoryRepository.findByLevel(level);
    }

    /**
     * 根据名称搜索类别
     */
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.searchByName(keyword);
    }

    /**
     * 获取所有类别
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 获取类别详情
     */
    public CategoryDetail getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException("类别不存在"));
        return new CategoryDetail(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getLevel(),
                category.getSortOrder(),
                category.getParent(),
                category.getChildren()
        );
    }
}