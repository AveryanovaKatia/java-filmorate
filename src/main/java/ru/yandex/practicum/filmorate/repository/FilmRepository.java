package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

public interface FilmRepository {
    Optional<Film> getDyId(int id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void delete(int id);

    void  putLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getBestFilm(int count);

    List<Integer> getAllId();

    List<Film> directorFilmsSortBy(int directorId, String sortBy);

    List<Film> search(String query, String by);

    List<Film> common(int userId, int friendId);
}
