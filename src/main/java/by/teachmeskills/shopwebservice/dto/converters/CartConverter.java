package by.teachmeskills.shopwebservice.dto.converters;

import by.teachmeskills.shopwebservice.dto.CartDto;
import by.teachmeskills.shopwebservice.entities.Cart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartConverter {
    private final ProductConverter productConverter;

    public CartConverter(ProductConverter productConverter) {
        this.productConverter = productConverter;
    }

    public CartDto toDto(Cart cart) {
        return Optional.ofNullable(cart)
                .map(c -> CartDto.builder()
                        .totalPrice(cart.getTotalPrice())
                        .products(Optional.ofNullable(c.getProducts()).map(products -> products.stream()
                                .map(productConverter::toDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }
}
