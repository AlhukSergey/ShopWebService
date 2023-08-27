package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.CategoryDto;
import by.teachmeskills.shopwebservice.dto.converters.CategoryConverter;
import by.teachmeskills.shopwebservice.entities.Category;
import by.teachmeskills.shopwebservice.repositories.CategoryRepository;
import by.teachmeskills.shopwebservice.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public CategoryDto getCategory(int id) {
        return categoryConverter.toDto(Optional.ofNullable(categoryRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не существует.", id))));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryConverter::toDto).toList();
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryConverter.fromDto(categoryDto);
        category = categoryRepository.createOrUpdate(category);
        return categoryConverter.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = Optional.ofNullable(categoryRepository.findById(categoryDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено.", categoryDto.getId())));
        category.setName(categoryDto.getName());
        category.getImage().setImagePath(categoryDto.getImagePath());
        return categoryConverter.toDto(categoryRepository.createOrUpdate(category));
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.delete(id);
    }
}
