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
    int id;
    @Email(message = "Емейл должен содержать @ и наименование")
    String email;
    @NotBlank(message = "Поле не может быть пустым")
    String login;
    String name;
    @Past(message = "День рождения не может быть позднее этого мгновения")
    LocalDate birthday;
}


