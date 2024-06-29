package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

public interface FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    public List<FilmDTO> findAll();

    public FilmDTO create(Film film);

    public FilmDTO update(Film film);

    Set<Long> putLike (Long id, Long userId);

    Set<Long> deleteLike(Long id, Long userId);

    List<FilmDTO> getBestFilm(Long count);

    Map<Long, Film> getFilms();
}
