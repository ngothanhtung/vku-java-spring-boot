package vn.vku.crud.services;

import org.springframework.stereotype.Service;
import vn.vku.crud.entities.Category;
import vn.vku.crud.repositories.CategoryJpaRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryService(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    public List<Category> getAllCategories() {
        return categoryJpaRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryJpaRepository.findById(id).orElse(null);
    }

    public Category createCategory(Category category) {
        return categoryJpaRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        this.categoryJpaRepository.findById(id).orElseThrow();
        return categoryJpaRepository.save(category);
    }

    public void deleteCategory(Long id) {
        this.categoryJpaRepository.findById(id).orElseThrow();
        categoryJpaRepository.deleteById(id);
    }
}
