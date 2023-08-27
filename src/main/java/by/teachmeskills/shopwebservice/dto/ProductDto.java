package by.teachmeskills.shopwebservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int id;

    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 3, max = 100, message = "Имя продукта не может содержать меньше 3 и больше 100 символов.")
    private String name;

    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 5, message = "Описание продукта не может содержать меньше 5 символов.")
    private String description;

    @NotNull(message = "Поле должно быть заполнено!")
    @Min(value = 0, message = "Значение не должно быть меньше 0.")
    private double price;
    private List<ImageDto> images;
    private int categoryId;
}
