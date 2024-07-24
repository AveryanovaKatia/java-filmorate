package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

public interface FilmRepository {
    Optional<Film> getDyId(Long id);

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void putLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    Collection<Film> getBestFilm(Long count);

    List<Long> getAllId();
}
