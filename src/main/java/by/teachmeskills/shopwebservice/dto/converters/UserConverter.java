package by.teachmeskills.shopwebservice.dto.converters;

import by.teachmeskills.shopwebservice.dto.UserDto;
import by.teachmeskills.shopwebservice.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserConverter {
    private final OrderConverter orderConverter;

    public UserConverter(OrderConverter orderConverter) {
        this.orderConverter = orderConverter;
    }

    public UserDto toDto(User user) {
        return Optional.ofNullable(user).map(u -> UserDto.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .surname(u.getSurname())
                        .birthday(u.getBirthday())
                        .email(u.getEmail())
                        .balance(u.getBalance())
                        .password(u.getPassword())
                        .orders(Optional.ofNullable(u.getOrders()).map(orders -> orders.stream()
                                .map(orderConverter::toDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public User fromDto(UserDto userDto) {
        return Optional.ofNullable(userDto).map(ud -> User.builder()
                        .name(ud.getName())
                        .surname(ud.getSurname())
                        .birthday(ud.getBirthday())
                        .email(ud.getEmail())
                        .password(ud.getPassword())
                        .balance(ud.getBalance())
                        .build())
                .orElse(null);
    }
}
