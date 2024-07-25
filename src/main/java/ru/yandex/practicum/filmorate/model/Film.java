package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validation.StartRelease;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NotNull(groups = {UpdateGroup.class})
    int id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Size(max = 200, message = "Превышено количество символов")
    String description;
    @StartRelease
    LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма не может быть отрицательным числом")
    int duration;
    Mpa mpa;
    Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    Set<Integer> likes;
}