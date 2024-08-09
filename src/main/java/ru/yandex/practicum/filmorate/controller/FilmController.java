package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmController {
    FilmService filmService;

    @GetMapping("/films/{id}")
    public Film getById(@PathVariable @Positive final int id) {
        return filmService.getById(id);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody final Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Validated(UpdateGroup.class) @Valid @RequestBody final Film film) {
        return filmService.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLike(@PathVariable @Positive final int id, @PathVariable @Positive final int userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable @Positive final int id, @PathVariable @Positive final int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilm(@RequestParam(defaultValue = "10") @Positive final int count) {
        return filmService.getBestFilm(count);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> directorFilmsSortBy(@PathVariable @Positive final int directorId,
                             @RequestParam @NotBlank String sortBy) {
        return filmService.directorFilmsSortBy(directorId, sortBy);
    }

    @GetMapping("/films/search")
    public List<Film> search(@RequestParam @NotBlank String query,
                             @RequestParam(required = false, defaultValue = "title") String by) {
        return filmService.search(query, by);
    }
}