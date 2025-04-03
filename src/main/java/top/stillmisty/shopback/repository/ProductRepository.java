package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, UUID>,
        CrudRepository<Product, UUID> {
}
