package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmServiceImpl implements FilmService {
    FilmRepository filmRepository;
    UserRepository userRepository;
    GenreRepository genreRepository;
    MpaRepository mpaRepository;

    public Optional<Film> getById(final int id) {
        log.info("Запрос на получение фильма с id = {}", id);
        validId(id);
        return filmRepository.getDyId(id);
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

    public Collection<Film> getBestFilm(final int count) {
        log.info("Запрос на получение списка лучших фильмов");
        int size = filmRepository.getAllId().size();
        if (size < count) {
            log.info("В запросе на получение списка лучших фильмов count превышвет размер списка");
            return filmRepository.getBestFilm(size);
        }
        log.info("Отбираем лучшие фильмы");
        return filmRepository.getBestFilm(count);
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
            Set<Integer> genresId = Set.copyOf(film.getGenres().stream().map(Genre::getId).toList());
            Set<Genre> genres = new HashSet<>();
            for (Integer i : genresId) {
                if (genreRepository.findById(i).isEmpty()) {
                    log.warn("Передан несуществующий жанр");
                    throw new ValidationException("Передан несуществующий жанр");
                }
                genres.add(genreRepository.findById(i).get());
            }
            log.info("Жанры переданы верно");
            film.getGenres().clear();
            film.setGenres(genres);
        }
        return film;
    }
}