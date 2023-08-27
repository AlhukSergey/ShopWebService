package by.teachmeskills.shopwebservice.dto.converters;

import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductConverter {
    private final CategoryRepository categoryRepository;
    private final ImageConverter imageConverter;

    public ProductConverter(CategoryRepository categoryRepository, ImageConverter imageConverter) {
        this.categoryRepository = categoryRepository;
        this.imageConverter = imageConverter;
    }

    public ProductDto toDto(Product product) {
        return Optional.ofNullable(product).map(p -> ProductDto.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .categoryId(p.getCategory().getId())
                        .images(Optional.ofNullable(p.getImages())
                                .map(images -> images.stream()
                                        .map(imageConverter::toDto)
                                        .toList())
                                .orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public Product fromDto(ProductDto productDto) {
        return Optional.ofNullable(productDto).map(pd -> Product.builder()
                        .name(pd.getName())
                        .description(pd.getDescription())
                        .price(pd.getPrice())
                        .category(categoryRepository.findById(pd.getCategoryId()))
                        .images(Optional.ofNullable(pd.getImages())
                                .map(images -> images
                                        .stream()
                                        .map(imageConverter::fromDto).toList())
                                .orElse(List.of()))
                        .build())
                .orElse(null);
    }
}
