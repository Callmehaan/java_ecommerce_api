package callmehaan.dev.ecommerce.category;

import callmehaan.dev.ecommerce.category.dto.CreateCategoryRequest;
import callmehaan.dev.ecommerce.category.entity.Category;
import callmehaan.dev.ecommerce.common.exception.ResourceNotFoundException;
import callmehaan.dev.ecommerce.product.ProductService;
import callmehaan.dev.ecommerce.product.entity.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();
        if(createCategoryRequest.parentId() != null) {
            Category parentCategory = this.categoryRepository.findById(createCategoryRequest.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            category.setParent(parentCategory);
            category.setLevel(parentCategory.getLevel() + 1);
        } else {
            category.setParent(null);
            category.setLevel(0);
        }

        category.setName(createCategoryRequest.name());
        category.setDescription(createCategoryRequest.description());
        category.setSlug(createCategoryRequest.slug() != null
                ? createCategoryRequest.slug()
                : slugify(createCategoryRequest.name())
        );
        category.setActive(createCategoryRequest.active());
        category.setSortOrder(createCategoryRequest.sortOrder());

        return this.categoryRepository.save(category);
    }

    public List<Category> getCategoryPath(UUID categoryId) {
        List<Category> path = new ArrayList<>();
        Category current = categoryRepository.findById(categoryId)
                .orElseThrow();

        while (current != null) {
            path.addFirst(current);
            current = current.getParent();
        }
        return path;
    }

    public Category getCategory(UUID categoryId) {
        return this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    //? =========== Helper Methods ===========
    private String slugify(String categoryName) {
        return categoryName
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
