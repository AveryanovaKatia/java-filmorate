package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * Mpa.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mpa {
    @NotNull
    int id;
    String name;
}