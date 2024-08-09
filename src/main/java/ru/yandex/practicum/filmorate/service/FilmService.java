package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmService {
    Film getById(int id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void putLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getBestFilm(int count);

    List<Film> directorFilmsSortBy(int directorId, String sortBy);

    List<Film> search(String query, String by);
}
