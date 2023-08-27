package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.entities.Cart;
import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final ProductRepository productRepository;

    private final Cart shopCart;

    public CartService(ProductRepository productRepository, Cart shopCart) {
        this.productRepository = productRepository;
        this.shopCart = shopCart;
    }

    public ProductDto addProductToCart(ProductDto productDto) {
        Product product = Optional.ofNullable(productRepository.findById(productDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", productDto.getId())));
        shopCart.addProduct(product);
        return productDto;
    }

    public void removeProductFromCart(int id) throws by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException {
        shopCart.removeProduct(id);
    }

    public void clear() {
        shopCart.clear();
    }
}
