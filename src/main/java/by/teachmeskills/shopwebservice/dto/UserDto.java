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
    /* Marker interface for grouping validations to be applied at the time of creating a new user. */
    public interface UserRegistration {
    }

    /* Marker interface for grouping validations to be applied at the time of updating user data. */
    public interface UserUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of user login. */
    public interface UserLogin {
    }

    private int id;

    @NotBlank(message = "Поле должно быть заполнено!", groups = {UserRegistration.class, UserUpdate.class})
    @Size(min = 3, max = 100, message = "Имя не может содержать меньше 3 и больше 100 символов.", groups = {UserRegistration.class, UserUpdate.class})
    private String name;

    @NotBlank(message = "Поле должно быть заполнено!", groups = {UserRegistration.class, UserUpdate.class})
    @Size(min = 3, max = 100, message = "Фамилия не может содержать меньше 3 и больше 100 символов.", groups = {UserRegistration.class, UserUpdate.class})
    private String surname;

    @Past(groups = {UserRegistration.class, UserUpdate.class})
    private LocalDate birthday;
    private double balance;

    @Email(message = "Неверный формат email.", groups = {UserRegistration.class, UserUpdate.class})
    @NotBlank(message = "Поле должно быть заполнено!", groups = {UserLogin.class, UserRegistration.class, UserUpdate.class})
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
            "одну заглавную букву, одну букву нижнего регистра, один специальный символ.", groups = UserRegistration.class)
    @NotBlank(message = "Поле должно быть заполнено!", groups = {UserLogin.class, UserRegistration.class})
    private String password;
    private List<OrderDto> orders;
}
