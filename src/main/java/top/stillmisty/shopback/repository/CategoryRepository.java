package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import top.stillmisty.shopback.entity.Category;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID> {
}
