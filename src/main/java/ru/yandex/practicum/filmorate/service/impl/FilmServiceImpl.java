package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmServiceImpl implements FilmService {
    FilmRepository filmRepository;
    UserRepository userRepository;
    GenreRepository genreRepository;
    MpaRepository mpaRepository;
    DirectorRepository directorRepository;

    public Film getById(final int id) {
        log.info("Запрос на получение фильма с id = {}", id);
        validId(id);
        return filmRepository.getDyId(id)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + id + " не существует"));
    }

    public List<Film> findAll() {
        log.info("Запрос на получение списка фильмов");
        if (filmRepository.getAllId().isEmpty()) {
            log.info("В приложение еще не добавлен ни один фильм");
            return new ArrayList<>();
        }
        return filmRepository.findAll();
    }

    public Film create(final Film film) {
        log.info("Запрос на добавление нового фильма");
        Film filmGenre = validAndAddMpaGenres(film);
        log.info("Запрос на добавление нового фильма в репозиторий");
        Film newFilm = filmRepository.create(filmGenre);
        log.info("Фильм успешно добавлен под id {}", newFilm.getId());
        return newFilm;
    }

    public Film update(final Film film) {
        log.info("Запрос на обновление фильма");
        validId(film.getId());
        Film filmGenre = validAndAddMpaGenres(film);
        Film newFilm = filmRepository.update(filmGenre);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return newFilm;
    }

    public void putLike(final int id, final int userId) {
        validId(id);
        validIdUser(userId);
        filmRepository.putLike(id, userId);
        log.info("Пользователь с id {} поставил like фильму с id {}", userId, id);
    }

    public void deleteLike(final int id, final int userId) {
        validId(id);
        validIdUser(userId);
        filmRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил like у фильма с id {}", userId, id);
    }

    public List<Film> getBestFilm(final int count) {
        log.info("Запрос на получение списка лучших фильмов");
        int size = filmRepository.findAll().size();
        if (size < count) {
            log.info("В запросе на получение списка лучших фильмов count превышвет размер списка");
            return filmRepository.getBestFilm(size);
        }
        log.info("Отбираем лучшие фильмы");
        return filmRepository.getBestFilm(count);
    }

    @Override
    public List<Film> directorFilmsSortBy(int directorId, String sortBy) {
        if (!sortBy.equals("year") && !sortBy.equals("likes")) {
            log.error("Неверно указаны параметры запроса");
            throw new NotFoundException("Неверно указаны параметры запроса");
        }
        if (!directorRepository.getAllId().contains(directorId)) {
            log.error("Режиссера с id = {} нет.", directorId);
            throw new NotFoundException("Режиссера с id = {} нет." + directorId);
        }
        return filmRepository.directorFilmsSortBy(directorId, sortBy);
    }

    private void validId(final int id) {
        if (!filmRepository.getAllId().contains(id)) {
            log.error("Фильма с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }

    private void validIdUser(final int id) {
        if (userRepository.getById(id).isEmpty()) {
            log.error("Киномана с id = {} нет.", id);
            throw new NotFoundException("Киномана с id = {} нет." + id);
        }
    }

    private Film validAndAddMpaGenres(final Film film) {
        if (Objects.nonNull(film.getMpa())) {
            log.info("Проверка на корректность введенного к фильму mpa");
            film.setMpa(mpaRepository.findById(film.getMpa().getId())
                    .orElseThrow(() -> new ValidationException("В приложении не предусмотрено такое mpa"))
            );
        }

        if (Objects.nonNull(film.getGenres())) {
            log.info("Проверка на корректность введенных к фильму жанров");
            List<Integer> genresId = film.getGenres().stream().map(Genre::getId).toList();
            LinkedHashSet<Genre> genres = genreRepository.getListGenres(genresId).stream()
                    .sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
            if (film.getGenres().size() == genres.size()) {
                log.info("Жанры переданы верно");
                film.getGenres().clear();
                film.setGenres(genres);
            } else {
                log.warn("Передан несуществующий жанр");
                throw new ValidationException("Передан несуществующий жанр");
            }
        }
        return film;
    }

    @Override
    public List<Film> search(String query, String by) {
        log.info("Запрос на поиск фильма по названию и/или по режиссёру");
        if (!by.equals("director") && !by.equals("title")
        && !by.equals("director,title") && !by.equals("title,director")) {
            log.error("Параметры запроса переданны неверно");
            throw new NotFoundException("Параметры запроса переданны неверно");
        }
        return filmRepository.search(query, by);
    }
}