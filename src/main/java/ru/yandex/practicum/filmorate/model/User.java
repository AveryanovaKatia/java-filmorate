package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * User.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotNull(groups = {UpdateGroup.class})
    Long id;
    @Email(message = "Емейл должен содержать @ и наименование")
    String email;
    @NotBlank(message = "Поле не может быть пустым")
    String login;
    String name;
    @Past(message = "День рождения не может быть позднее этого мгновения")
    LocalDate birthday;
    Set<Long> friends = new TreeSet<>();

    public void setFriends(Long id) {
        friends.add(id);
    }
}

