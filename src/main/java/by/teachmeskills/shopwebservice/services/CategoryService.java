package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto getCategory(int id);

    List<CategoryDto> getAllCategories();

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(int id);
}
