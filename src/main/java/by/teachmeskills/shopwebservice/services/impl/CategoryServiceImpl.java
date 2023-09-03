package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.CategoryDto;
import by.teachmeskills.shopwebservice.dto.converters.CategoryConverter;
import by.teachmeskills.shopwebservice.entities.Category;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import by.teachmeskills.shopwebservice.exceptions.ParsingException;
import by.teachmeskills.shopwebservice.repositories.CategoryRepository;
import by.teachmeskills.shopwebservice.services.CategoryService;
import by.teachmeskills.shopwebservice.utils.FileService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final FileService<Category> fileService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryConverter categoryConverter, FileService<Category> fileService) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
        this.fileService = fileService;
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

    @Override
    public List<CategoryDto> saveCategoriesFromFile(MultipartFile file) {
        List<CategoryDto> csvCategories = parseCsv(file);
        List<Category> categories = Optional.ofNullable(csvCategories)
                .map(list -> list.stream()
                        .map(categoryConverter::fromDto)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(categories).isPresent()) {
            categories.forEach(categoryRepository::createOrUpdate);
            return categories.stream().map(categoryConverter::toDto).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String saveCategoriesFromBD(String fileName) throws ExportToFIleException {
        return fileService.writeToFile(fileName, categoryRepository.findAll());
    }

    private List<CategoryDto> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<CategoryDto> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CategoryDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время преобразования данных данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}
