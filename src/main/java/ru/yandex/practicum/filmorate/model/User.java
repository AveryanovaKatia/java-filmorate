package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import java.time.LocalDate;

/**
 * User.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotNull(groups = {UpdateGroup.class})
    Integer id;
    @Size(max = 255)
    @Email(message = "Емейл должен содержать @ и наименование")
    String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{1,255}$",
            message = "имя пользователя должно быть длиной от 1 до 255 символов без специальных символов")
    String login;
    @Size(max = 255)
    String name;
    @Past(message = "День рождения не может быть позднее этого мгновения")
    LocalDate birthday;
}


