package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto getProduct(int id);

    List<ProductDto> getAllProductsByCategoryId(int categoryId);

    List<ProductDto> getProductsBySearchParameter(String parameter);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(ProductDto productDto);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(int id);
}
