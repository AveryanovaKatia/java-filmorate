package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    Optional<FilmDTO> getById(int id);

    List<FilmDTO> findAll();

    FilmDTO create(Film film);

    FilmDTO update(Film film);

    void putLike(int id, int userId);

    void deleteLike(int id, int userId);

    Collection<FilmDTO> getBestFilm(int count);
}
