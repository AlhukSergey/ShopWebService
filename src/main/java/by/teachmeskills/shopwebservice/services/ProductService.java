package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto getProduct(int id);

    List<ProductDto> getAllProductsByCategoryId(int categoryId);

    List<ProductDto> getProductsBySearchParameter(String parameter);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(ProductDto productDto);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(int id);

    List<ProductDto> saveProductsFromFile(MultipartFile file) throws Exception;

    void saveProductsFromBD(HttpServletResponse response, int categoryId) throws ExportToFIleException;
}
