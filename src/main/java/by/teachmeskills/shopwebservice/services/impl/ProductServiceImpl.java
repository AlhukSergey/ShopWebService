package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.dto.converters.ProductConverter;
import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.exceptions.ExportToFIleException;
import by.teachmeskills.shopwebservice.exceptions.ParsingException;
import by.teachmeskills.shopwebservice.repositories.ProductRepository;
import by.teachmeskills.shopwebservice.services.ProductService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
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

    @Override
    public List<ProductDto> saveProductsFromFile(MultipartFile file) {
        List<ProductDto> csvProducts = parseCsv(file);
        List<Product> orders = Optional.ofNullable(csvProducts)
                .map(list -> list.stream()
                        .map(productConverter::fromDto)
                        .toList())
                .orElse(null);
        if (Optional.ofNullable(orders).isPresent()) {
            orders.forEach(productRepository::createOrUpdate);
            return orders.stream().map(productConverter::toDto).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void saveProductsFromBD(HttpServletResponse response, int categoryId) throws ExportToFIleException {
        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=productsOfCategoryWithId" + categoryId + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");

        List<ProductDto> dtoProducts = productRepository.findAllByCategoryId(categoryId).stream().map(productConverter::toDto).toList();

        try (ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE)) {
            String[] csvHeader = {"Product ID", "Name", "Descriptions", "Price", "Category ID", "Images"};
            String[] nameMapping = {"id", "name", "description", "price", "categoryId", "images"};

            csvWriter.writeHeader(csvHeader);

            for (ProductDto productDto : dtoProducts) {
                csvWriter.write(productDto, nameMapping);
            }
        } catch (IOException e) {
            throw new ExportToFIleException("Во время записи в файл произошла непредвиденная ошибка. Попробуйте позже.");
        }
    }

    private List<ProductDto> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<ProductDto> csvToBean = new CsvToBeanBuilder<ProductDto>(reader)
                        .withType(ProductDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                return csvToBean.parse();
            } catch (Exception ex) {
                throw new ParsingException(String.format("Ошибка во время преобразования данных: %s", ex.getMessage()));
            }
        }
        return Collections.emptyList();
    }
}
