package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@Getter
public class FilmController {
    public static final LocalDate START_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validStartRelease(film.getReleaseDate());
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен под id {}", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Validated(UpdateGroup.class) @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validStartRelease(film.getReleaseDate());
            films.put(film.getId(), film);
            log.info("Фильм с id {} успешно обновлен", film.getId());
            return film;
        }
        log.error("Фильма с id = {} нет.", film.getId());
        throw new NotFoundException("Фильма с id = {} нет." + film.getId());
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validStartRelease(LocalDate filmRelease) {
        if (filmRelease.isBefore(START_RELEASE)) {
            log.error("Дата релиза должна быть не раньше {}", START_RELEASE);
            throw new ValidationException("Дата релиза должна быть не раньше " + START_RELEASE);
        }
    }
}
