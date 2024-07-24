package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Genre.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
}