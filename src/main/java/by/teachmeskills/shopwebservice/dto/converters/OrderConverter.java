package by.teachmeskills.shopwebservice.dto.converters;

import by.teachmeskills.shopwebservice.dto.OrderDto;
import by.teachmeskills.shopwebservice.entities.Order;
import by.teachmeskills.shopwebservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class OrderConverter {
    private final ProductConverter productConverter;
    private final UserRepository userRepository;

    public OrderConverter(ProductConverter productConverter, UserRepository userRepository) {
        this.productConverter = productConverter;
        this.userRepository = userRepository;
    }

    public OrderDto toDto(Order order) {
        return Optional.ofNullable(order).map(o -> OrderDto.builder()
                        .id(o.getId())
                        .orderStatus(o.getOrderStatus())
                        .createdAt(o.getCreatedAt().toLocalDateTime())
                        .products(Optional.ofNullable(o.getProducts()).map(products -> products.stream()
                                .map(productConverter::toDto).toList()).orElse(List.of()))
                        .price(o.getPrice())
                        .userId(o.getUser().getId())
                        .build())
                .orElse(null);
    }

    public Order fromDto(OrderDto orderDto) {
        return Order.builder()
                .user(userRepository.findById(orderDto.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", orderDto.getUserId()))))
                .orderStatus(orderDto.getOrderStatus())
                .price(orderDto.getPrice())
                .createdAt(Timestamp.valueOf(orderDto.getCreatedAt()))
                .build();
    }
}
