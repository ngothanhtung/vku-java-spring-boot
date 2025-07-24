package vn.vku.crud.services;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.vku.crud.entities.Category;
import vn.vku.crud.repositories.CategoryRepository;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElse(null);
  }

  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }
}
