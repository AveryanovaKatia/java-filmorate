package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<FilmDTO> getById(@PathVariable @Positive Long id) {
        return filmService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDTO> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDTO create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDTO update(@Validated(UpdateGroup.class) @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void putLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDTO> getBestFilm(@RequestParam(defaultValue = "10") @Positive Long count) {
        return filmService.getBestFilm(count);
    }
}