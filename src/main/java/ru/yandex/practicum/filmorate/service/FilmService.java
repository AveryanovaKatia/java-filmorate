package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    Optional<FilmDTO> getById(Long id);

    List<FilmDTO> findAll();

    FilmDTO create(Film film);

    FilmDTO update(Film film);

    void putLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<FilmDTO> getBestFilm(Long count);
}
