package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.CategoryDto;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryDto getCategory(int id);

    List<CategoryDto> getAllCategories();

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(int id);

    List<CategoryDto> saveCategoriesFromFile(MultipartFile file);

    String saveCategoriesFromBD(String fileName) throws ExportToFIleException;
}
