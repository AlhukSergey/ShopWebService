package by.teachmeskills.shopwebservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private int id;
    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 10, message = "Путь к картинке не может содержать меньше 3 и больше 100 символов.")
    private String imagePath;

    @NotNull(message = "Поле должно быть заполнено!")
    @Min(value = 0, message = "Поле может принимать значение 0 (второстепенное изображение) либо 1 (главное изображение).")
    @Max(value = 1, message = "Поле может принимать значение 0 (второстепенное изображение) либо 1 (главное изображение).")
    private int primary;
}
