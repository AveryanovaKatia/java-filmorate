package ru.yandex.practicum.filmorate.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDTO {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    Set<Long> likes = new TreeSet<>();
}
