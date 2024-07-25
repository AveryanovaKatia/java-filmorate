package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    Optional<Film> getById(int id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void putLike(int id, int userId);

    void deleteLike(int id, int userId);

    Collection<Film> getBestFilm(int count);
}
