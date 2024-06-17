package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<FilmDTO> findAll() {
        log.info("Запрос на получение списка фильмов");
        List<FilmDTO> allFilmDTO = new ArrayList<>();
        films.values().forEach(film -> allFilmDTO.add(getDTO(film)));
        return allFilmDTO;
    }

    @PostMapping
    public FilmDTO create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен под id {}", film.getId());
        return getDTO(film);
    }

    @PutMapping
    public FilmDTO update(@Validated(UpdateGroup.class) @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с id {} успешно обновлен", film.getId());
            return getDTO(film);
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

    private FilmDTO getDTO(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        return filmDTO;
    }
}
