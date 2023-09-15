package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.CartDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.dto.converters.CartConverter;
import by.teachmeskills.shopwebservice.entities.Cart;
import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final ProductRepository productRepository;
    private final CartConverter cartConverter;
    private final Cart shopCart;

    public CartService(ProductRepository productRepository, CartConverter cartConverter, Cart shopCart) {
        this.productRepository = productRepository;
        this.cartConverter = cartConverter;
        this.shopCart = shopCart;
    }

    public CartDto addProductToCart(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", productDto.getId())));
        shopCart.addProduct(product);
        return cartConverter.toDto(shopCart);
    }

    public void removeProductFromCart(int id) throws by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException {
        shopCart.removeProduct(id);
    }

    public void clear() {
        shopCart.clear();
    }
}
