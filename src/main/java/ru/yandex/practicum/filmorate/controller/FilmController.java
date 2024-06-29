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
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDTO> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDTO create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDTO update(@Validated(UpdateGroup.class) @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Long> putLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Long> deleteLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDTO> getBestFilm(@RequestParam(defaultValue = "10") @Positive Long count) {
        return filmService.getBestFilm(count);
    }
}