package by.teachmeskills.shopwebservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;

    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 3, max = 100, message = "Имя не может содержать меньше 3 и больше 100 символов.")
    private String name;

    @NotBlank(message = "Поле должно быть заполнено!")
    @Size(min = 3, max = 100, message = "Фамилия не может содержать меньше 3 и больше 100 символов.")
    private String surname;

    @Past
    private LocalDate birthday;
    private double balance;

    @Email(message = "Неверный формат email.")
    @NotBlank(message = "Поле должно быть заполнено!")
    private String email;

    /*
       (?=.*[0-9]) a digit must occur at least once
       (?=.*[a-z]) a lower case letter must occur at least once
       (?=.*[A-Z]) an upper case letter must occur at least once
       (?=.*[@#$%^&+=]) a special character must occur at least once
       (?=\\S+$) no whitespace allowed in the entire string
       .{8,} at least 8 characters
       Password example (used for login) : A!1+=asasasaas
       */
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Неверный формат пароля! " +
            "Длина пароля должна быть не короче 8 символов. Пароль должен содержать как минимум одну цифру," +
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.")
    @NotBlank(message = "Поле должно быть заполнено!")
    private String password;
    private List<OrderDto> orders;
}
