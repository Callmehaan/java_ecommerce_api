package callmehaan.dev.ecommerce.product;

import callmehaan.dev.ecommerce.category.CategoryService;
import callmehaan.dev.ecommerce.category.entity.Category;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.product.dto.CreateProductRequest;
import callmehaan.dev.ecommerce.product.dto.ProductDto;
import callmehaan.dev.ecommerce.product.dto.UpdateProductRequest;
import callmehaan.dev.ecommerce.product.entity.Product;
import callmehaan.dev.ecommerce.storage.ImageStorageService;
import callmehaan.dev.ecommerce.storage.entity.Image;
import callmehaan.dev.ecommerce.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ImageStorageService storageService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductCacheService productCacheService;


    public ProductService(ImageStorageService storageService, ProductRepository productRepository, CategoryService categoryService, ProductCacheService productCacheService) {
        this.storageService = storageService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productCacheService = productCacheService;
    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest productRequest, List<MultipartFile> images) throws IOException {
        Product product = new Product();
        product.setTitle(productRequest.title());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStock(productRequest.stock());

        List<String> imageUrls = storageService.storeImages(product, images);

        try {
            Product savedProduct = productRepository.save(product);
            ProductDto productDto = ProductDto.from(savedProduct);
            productCacheService.cacheProduct(productDto);

            return productDto;
        } catch (Exception e) {
            storageService.deleteUploadedFiles(imageUrls);
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID id) {
        ProductDto cachedProduct = this.productCacheService.getProduct(id);
        if(cachedProduct != null) {
            return cachedProduct;
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Product not found with id {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });
        ProductDto productDto = ProductDto.from(product);
        this.productCacheService.cacheProduct(productDto);
        return productDto;
    }

    public PageResponse<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductDto> productDtoPage = productPage.map(ProductDto::from);

        return PageResponse.from(productDtoPage);
    }

    @Transactional
    public ProductDto updateProduct(UUID id, UpdateProductRequest updateProductRequest, List<MultipartFile> images)
            throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Product not found with id {}", id);
                    return new ResourceNotFoundException(String.format("Product not found with id: %s", id));
                });

        product.setTitle(updateProductRequest.title());
        product.setDescription(updateProductRequest.description());
        product.setPrice(updateProductRequest.price());
        product.setStock(updateProductRequest.stock());

        storageService.storeImages(product, images);

        Product updatedProduct = productRepository.save(product);
        ProductDto productDto = ProductDto.from(updatedProduct);
        this.productCacheService.cacheProduct(productDto);
        log.info("Product updated successfully");

        return productDto;
    }

    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            log.info("Product not found with id {}", id);
            throw new ResourceNotFoundException(
                    "Product not found with id: " + id
            );
        }

        productRepository.deleteById(id);
        productCacheService.deleteCachedProduct(id);

        log.info("Product deleted successfully with id {}", id);
    }

    @Transactional
    public void assignCategoryToProduct(UUID productId, UUID categoryId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.info("Product not found with id {}", productId);
                    return new ResourceNotFoundException(String.format("Product not found with id: %s", productId));
                });

        Category category = this.categoryService.getCategory(categoryId);
        product.addCategory(category);

        this.productRepository.save(product);
    }

}
