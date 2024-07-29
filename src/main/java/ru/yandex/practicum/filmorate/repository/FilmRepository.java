package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

public interface FilmRepository {
    Optional<Film> getDyId(int id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void  putLike(int id, int userId);

    void deleteLike(int id, int userId);

    List<Film> getBestFilm(int count);

    List<Integer> getAllId();
}
