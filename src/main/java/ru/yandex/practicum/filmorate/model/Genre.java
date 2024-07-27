package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Genre.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {
    Integer id;
    @Size(max = 255)
    String name;
}