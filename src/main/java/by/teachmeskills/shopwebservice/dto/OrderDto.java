package by.teachmeskills.shopwebservice.dto;

import by.teachmeskills.shopwebservice.entities.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private int id;

    @NotNull(message = "Поле должно быть заполнено!")
    private OrderStatus orderStatus;

    @PastOrPresent
    private LocalDateTime createdAt;
    private List<ProductDto> products;

    @NotNull(message = "Поле должно быть заполнено!")
    @Min(value = 0)
    private double price;

    @NotNull(message = "Поле должно быть заполнено!")
    private int userId;
}
