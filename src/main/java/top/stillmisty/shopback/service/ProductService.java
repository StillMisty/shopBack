package top.stillmisty.shopback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.ProductAddRequest;
import top.stillmisty.shopback.dto.ProductChangeRequest;
import top.stillmisty.shopback.dto.ProductPageRequest;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.repository.ProductRepository;
import top.stillmisty.shopback.utils.PictureUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Path productImagePath;
    @Value("${app.base-url}")
    private String baseUrl;

    public ProductService(ProductRepository productRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.productImagePath = Paths.get(uploadDir + "/products");
        try {
            if (!Files.exists(productImagePath)) {
                Files.createDirectories(productImagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建商品图片目录", e);
        }
        this.productRepository = productRepository;
    }

    // 根据ID获取商品
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    // 分页获取商品列表
    public Page<Product> getProductsWithSort(ProductPageRequest pageRequest) {
        Sort sort = Sort.by(pageRequest.sortDirection(), pageRequest.sortBy());
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        return productRepository.findAll(pageable);
    }

    // 分页获取未下架商品列表
    public Page<Product> getProductsOnShelfWithSort(ProductPageRequest pageRequest) {
        Sort sort = Sort.by(pageRequest.sortDirection(), pageRequest.sortBy());
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        return productRepository.findAllByProductIsOffShelf(false, pageable);
    }

    public Product addProduct(ProductAddRequest productAddRequest) {
        Product product = new Product(
                productAddRequest.productName(),
                productAddRequest.productCategory(),
                productAddRequest.productMerchant(),
                productAddRequest.productDescription(),
                productAddRequest.productPrice(),
                productAddRequest.productDiscount(),
                productAddRequest.productStock()
        );
        return productRepository.save(product);
    }

    public Product updateProduct(UUID id, ProductChangeRequest productChangeRequest) {
        Product product = getProductById(id);
        product.setProductName(productChangeRequest.productName());
        product.setProductCategory(productChangeRequest.productCategory());
        product.setProductMerchant(productChangeRequest.productMerchant());
        product.setProductDescription(productChangeRequest.productDescription());
        product.setProductPrice(productChangeRequest.productPrice());
        product.setProductDiscount(productChangeRequest.productDiscount());
        product.setProductStock(productChangeRequest.productStock());
        product.setProductSoldCount(productChangeRequest.productSoldCount());
        product.setProductOnShelfTime(productChangeRequest.productOnShelfTime());
        return productRepository.save(product);
    }

    public Product offShelfProduct(UUID id) {
        Product product = getProductById(id);
        product.setProductIsOffShelf(true);
        return productRepository.save(product);
    }

    public Product updateProductImage(UUID productId, MultipartFile productImage) throws IOException {

        // 生成新的文件名
        String filename = PictureUtils.savePicture(productId.toString(), productImage, productImagePath);
        // 更新商品图片URL
        String productImageUrl = baseUrl + "/public/products/" + filename;
        Product product = getProductById(productId);
        product.setProductImage(productImageUrl);
        return productRepository.save(product);
    }
}
