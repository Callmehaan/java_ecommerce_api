package callmehaan.dev.ecommerce.product;

import callmehaan.dev.ecommerce.common.BaseController;
import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import callmehaan.dev.ecommerce.common.dto.PageResponse;
import callmehaan.dev.ecommerce.product.dto.CreateProductRequest;
import callmehaan.dev.ecommerce.product.dto.ProductDto;
import callmehaan.dev.ecommerce.product.dto.UpdateProductRequest;
import callmehaan.dev.ecommerce.product.entity.Product;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestPart("data") CreateProductRequest createProductRequest,
            @RequestPart(value = "images", required = false)List<MultipartFile> images
            ) throws IOException {
        ProductDto productDto = productService.createProduct(createProductRequest, images);

        return ok(HttpStatus.CREATED.value(), "Product created successfully", productDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable UUID id) {
        Product product = productService.getProduct(id);

        return ok(HttpStatus.OK.value(), "Product fetched successfully", ProductDto.from(product));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductDto>>> getAllProducts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PageResponse<ProductDto> response = productService.getAllProducts(pageable);

        return ok(
                HttpStatus.OK.value(),
                response.totalElements() > 0 ? "product(s) fetched successfully" : "No product found",
                response
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable UUID id,
            @RequestPart("data") UpdateProductRequest updateProductRequest,
            @RequestPart("images")  List<MultipartFile> images
    ) throws IOException {
        Product updatedProduct = productService.updateProduct(id, updateProductRequest, images);
        ApiResponse<ProductDto> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(), "Product updated successfully", ProductDto.from(updatedProduct));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        ApiResponse<Void> apiResponse = ApiResponse.success(
                HttpStatus.OK.value(), "Product deleted successfully", null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("{productId}/assign-category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> assignCategoryToProduct(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId
    ) {
        this.productService.assignCategoryToProduct(productId, categoryId);

        return ok(HttpStatus.OK.value(), "Category assigned successfully", null);
    }
}
