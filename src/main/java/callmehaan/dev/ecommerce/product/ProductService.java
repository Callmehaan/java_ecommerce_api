package callmehaan.dev.ecommerce.product;

import callmehaan.dev.ecommerce.category.CategoryService;
import callmehaan.dev.ecommerce.category.entity.Category;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.product.dto.CreateProductRequest;
import callmehaan.dev.ecommerce.product.dto.ProductDto;
import callmehaan.dev.ecommerce.product.dto.UpdateProductRequest;
import callmehaan.dev.ecommerce.product.entity.Product;
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
    private final StorageService storageService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(StorageService storageService, ProductRepository productRepository, CategoryService categoryService) {
        this.storageService = storageService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Product createProduct(CreateProductRequest productRequest, List<MultipartFile> images) throws IOException {
        Product product = new Product();
        product.setTitle(productRequest.title());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setStock(productRequest.stock());

        List<String> imageUrls = storeImages(product, images);

        try {
            Product savedProduct = productRepository.save(product);
            log.info("Saved product with id {}", savedProduct.getId());

            return savedProduct;
        } catch (Exception e) {
            deleteUploadedFiles(imageUrls);
            throw e;
        }

    }

    public Product getProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Product not found with id {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });
    }

    public PageResponse<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductDto> productDtoPage = productPage.map(ProductDto::from);

        return PageResponse.from(productDtoPage);
    }

    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequest updateProductRequest, List<MultipartFile> images)
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

        storeImages(product, images);

        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        if (productRepository.existsById(id)) productRepository.deleteById(id);
        log.info("Product not found with id {}", id);
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

    //? =========== Helper Methods ===========
    private List<String> storeImages(Product product, List<MultipartFile> images) throws IOException {
        List<String> imageUlrs = new ArrayList<>();

        try {
            if (images != null && !images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    String imageUrl = storageService.save(images.get(i));

                    Image image = new Image(
                            imageUrl,
                            i == 0,
                            product
                    );

                    product.addImage(image);
                    imageUlrs.add(imageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return imageUlrs;
    }

    private void deleteUploadedFiles(List<String> urls) {

        for (String url : urls) {
            try {
                storageService.delete(url);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

}
