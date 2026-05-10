package callmehaan.dev.ecommerce.category;

import callmehaan.dev.ecommerce.category.dto.CategoryDto;
import callmehaan.dev.ecommerce.category.dto.CreateCategoryRequest;
import callmehaan.dev.ecommerce.category.dto.UpdateCategoryRequest;
import callmehaan.dev.ecommerce.category.entity.Category;
import callmehaan.dev.ecommerce.common.BaseController;
import callmehaan.dev.ecommerce.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> addCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = this.categoryService.createCategory(createCategoryRequest);

        return ok(
                HttpStatus.CREATED.value(),
                "Category created successfully",
                CategoryDto.fromEntity(category)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable UUID id) {
        Category category = this.categoryService.getCategory(id);

        return ok(HttpStatus.OK.value(), "Category found", CategoryDto.fromEntity(category));
    }

    @GetMapping("category-path/{id}")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategory(@PathVariable UUID id) {
        List<CategoryDto> categoryPath = this.categoryService.getCategoryPath(id)
                .stream().map(CategoryDto::fromEntityShallow).toList();

        return ok(HttpStatus.OK.value(), "", categoryPath);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable UUID id, @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        Category category = this.categoryService.updateCategory(id, updateCategoryRequest);

        return ok(HttpStatus.OK.value(), "Category updated successfully", CategoryDto.fromEntity(category));
    }


}
