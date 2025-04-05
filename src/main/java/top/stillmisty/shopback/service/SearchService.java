package top.stillmisty.shopback.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.ProductQueryRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.repository.ProductRepository;

@Service
public class SearchService {

    private final ProductRepository productRepository;

    public SearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> searchProducts(ProductQueryRequest productQueryRequest) {
        Sort sort = Sort.by(productQueryRequest.direction(), productQueryRequest.sortBy());
        PageRequest pageRequest = PageRequest.of(productQueryRequest.page(), productQueryRequest.size(), sort);
        System.out.println("pageRequest = " + productQueryRequest);
        return productRepository.advancedSearch(
                productQueryRequest.keyword(),
                productQueryRequest.category(),
                productQueryRequest.merchant(),
                productQueryRequest.minPrice(),
                productQueryRequest.maxPrice(),
                productQueryRequest.includeOffShelf(),
                pageRequest
        );
    }

    public Page<Product> quickSearch(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.quickSearch(
                keyword,
                pageable,
                false
        );
    }
}