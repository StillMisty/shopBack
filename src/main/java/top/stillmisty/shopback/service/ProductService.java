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
import top.stillmisty.shopback.entity.Category;
import top.stillmisty.shopback.entity.Product;
import top.stillmisty.shopback.repository.CategoryRepository;
import top.stillmisty.shopback.repository.ProductRepository;
import top.stillmisty.shopback.utils.PictureUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Path productImagePath;
    @Value("${app.base-url}")
    private String baseUrl;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.categoryRepository = categoryRepository;
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

    /**
     * 根据商品ID获取商品信息
     *
     * @param productId 商品ID
     * @return 商品列表
     */
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    /**
     * 分页获取所有的商品列表
     *
     * @param pageRequest 分页请求参数
     * @return 商品列表
     */
    public Page<Product> getProductsWithSort(ProductPageRequest pageRequest) {
        Sort sort = Sort.by(pageRequest.sortDirection(), pageRequest.sortBy());
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        return productRepository.findAll(pageable);
    }

    /**
     * 分页获取未下架商品列表
     *
     * @param pageRequest 分页请求参数
     * @return 商品列表
     */
    public Page<Product> getProductsOnShelfWithSort(ProductPageRequest pageRequest) {
        Sort sort = Sort.by(pageRequest.sortDirection(), pageRequest.sortBy());
        Pageable pageable = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        return productRepository.findAllByProductIsOffShelf(false, pageable);
    }

    /**
     * 添加商品
     *
     * @param productAddRequest 商品添加请求
     * @return 商品实体
     */
    public Product addProduct(ProductAddRequest productAddRequest) {
        // 处理类别
        Set<Category> productCategories = productAddRequest.productCategories()
                .stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("类别不存在")))
                .collect(Collectors.toSet());

        Product product = new Product(
                productAddRequest.productName(),
                productCategories,
                productAddRequest.productMerchant(),
                productAddRequest.productDescription(),
                productAddRequest.productPrice(),
                productAddRequest.productDiscount(),
                productAddRequest.productStock()
        );
        return productRepository.save(product);
    }

    /**
     * 更新商品
     *
     * @param id 商品ID
     * @return 更新后的商品实体
     */
    public Product updateProduct(UUID id, ProductChangeRequest productChangeRequest) {
        // 处理类别
        Set<Category> productCategories = productChangeRequest.productCategories()
                .stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("类别不存在")))
                .collect(Collectors.toSet());

        Product product = getProductById(id);
        product.setProductName(productChangeRequest.productName());
        product.setProductCategories(productCategories);
        product.setProductMerchant(productChangeRequest.productMerchant());
        product.setProductDescription(productChangeRequest.productDescription());
        product.setProductPrice(productChangeRequest.productPrice());
        product.setProductDiscount(productChangeRequest.productDiscount());
        product.setProductStock(productChangeRequest.productStock());
        product.setProductSoldCount(productChangeRequest.productSoldCount());
        product.setProductOnShelfTime(productChangeRequest.productOnShelfTime());
        product.setProductIsOffShelf(productChangeRequest.productIsOffShelf());
        return productRepository.save(product);
    }

    /**
     * 更新商品图片
     *
     * @param productId    商品ID
     * @param productImage 商品图片文件
     * @return 更新后的商品实体
     * @throws IOException 如果文件处理失败
     */
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
