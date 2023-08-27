package by.teachmeskills.shopwebservice.dto;

import jakarta.validation.constraints.NotBlank;
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
public class CategoryDto {
    private int id;

    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 3, max = 100, message = "Имя категории не может содержать меньше 3 и больше 100 символов.")
    private String name;
    private String imagePath;
    private List<ProductDto> products;
}
