package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.dto.converters.ProductConverter;
import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.repositories.ProductRepository;
import by.teachmeskills.shopwebservice.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public ProductServiceImpl(ProductRepository productRepository, ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
    }

    @Override
    public ProductDto getProduct(int id) {
        return productConverter.toDto(productRepository.findById(id));
    }

    @Override
    public List<ProductDto> getAllProductsByCategoryId(int categoryId) {
        return productRepository.findAllByCategoryId(categoryId).stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getProductsBySearchParameter(String parameter) {
        return productRepository.findAllBySearchParameter(parameter).stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productConverter::toDto).toList();
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = Optional.ofNullable(productRepository.findById(productDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", productDto.getId())));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return productConverter.toDto(productRepository.createOrUpdate(product));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productConverter.fromDto(productDto);
        product = productRepository.createOrUpdate(product);
        return productConverter.toDto(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.delete(id);
    }
}
