package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public Optional<List<FilmDTO>> findAll() {
        log.info("Запрос на получение списка фильмов");
        if (filmStorage.getFilms().isEmpty()) {
            return Optional.empty();
        }
        return filmStorage.findAll();
    }

    public FilmDTO create(Film film) {
        FilmDTO filmDTO = filmStorage.create(film);
        log.info("Фильм успешно добавлен под id {}", film.getId());
        return filmDTO;
    }

    public FilmDTO update(Film film) {
        validId(film.getId());
        FilmDTO filmDTO = filmStorage.update(film);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return filmDTO;
    }

    public FilmDTO putLike(Long id, Long userId) {
        validId(id);
        validIdUser(userId);
        FilmDTO film = filmStorage.putLike(id, userId);
        log.info("Пользователь с id {} поставил like фильму с id {}", userId, id);
        return film;
    }

    public FilmDTO deleteLike(Long id, Long userId) {
        validId(id);
        validIdUser(userId);
        FilmDTO film = filmStorage.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил like у фильма с id {}", userId, id);
        return film;
    }

    public Optional<List<FilmDTO>> getBestFilm(Long count) {
        log.info("Запрос на получение списка лучших фильмов");
        if (filmStorage.getFilms().size() < count) {
            return filmStorage.getBestFilm((long) filmStorage.getFilms().size());
        }
        return filmStorage.getBestFilm(count);
    }

    private void validId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            log.error("Фильма с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }

    private void validIdUser(Long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            log.error("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }
}
