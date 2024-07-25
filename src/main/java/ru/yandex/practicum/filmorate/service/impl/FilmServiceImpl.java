package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    public Optional<FilmDTO> getById(final int id) {
        log.info("Запрос на получение фильма с id = {}", id);
        validId(id);
        return Optional.of(getDTO(filmRepository.getDyId(id).get()));
    }

    public List<FilmDTO> findAll() {
        log.info("Запрос на получение списка фильмов");
        if (filmRepository.getAllId().isEmpty()) {
            log.info("В приложение еще не добавлен ни один фильм");
            return new ArrayList<>();
        }
        return filmRepository.findAll().stream().map(this::getDTO).toList();
    }

    public FilmDTO create(final Film film) {
        log.info("Запрос на добавление нового фильма");
        validAndAddMpaGenres(film);
        Film newFilm = filmRepository.create(film);
        log.info("Фильм успешно добавлен под id {}", newFilm.getId());
        return getDTO(newFilm);
    }

    public FilmDTO update(final Film film) {
        log.info("Запрос на обновление фильма");
        validId(film.getId());
        validAndAddMpaGenres(film);
        Film newFilm = filmRepository.update(film);
        log.info("Фильм с id {} успешно обновлен", film.getId());
        return getDTO(newFilm);
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

    public Collection<FilmDTO> getBestFilm(final int count) {
        log.info("Запрос на получение списка лучших фильмов");
        int size = filmRepository.getAllId().size();
        if (size < count) {
            log.info("В запросе на получение списка лучших фильмов count превышвет размер списка");
            return filmRepository.getBestFilm(size).stream().map(this::getDTO).toList();
        }
        log.info("Отбираем лучшие фильмы");
        return filmRepository.getBestFilm(count).stream().map(this::getDTO).toList();
    }

    private void validId(final int id) {
        if (!filmRepository.getAllId().contains(id)) {
            log.error("Фильма с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }

    private void validIdUser(final int id) {
        if (!userRepository.getAllId().contains(id)) {
            log.error("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }

    private FilmDTO getDTO(final Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        filmDTO.setMpa(film.getMpa());
        filmDTO.setGenres(film.getGenres());
        filmDTO.setLikes(film.getLikes());
        return filmDTO;
    }

    private void validAndAddMpaGenres(final Film film) {
        if (Objects.nonNull(film.getMpa())) {
            log.info("Проверка на корректность введенного к фильму mpa");
            film.setMpa(mpaRepository.findById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("В приложении не предусмотрено такое mpa"))
            );
        }
        if (Objects.nonNull(film.getGenres())) {
            log.info("Проверка на корректность введенных к фильму жанров");
            List<Genre> genres = genreRepository.getListGenres(film.getGenres()
                    .stream().map(Genre::getId).toList());
            if (Objects.isNull(genres)) {
                log.error("Жанры не указаны");
                throw new NotFoundException("Жанры не указаны");
            } else if (genres.size() == film.getGenres().size()) {
                log.info("Перечень жанров указан корректно");
            } else {
                log.error("Перечень жанров указан не корректно");
                throw new ValidationException("Перечень жанров указан не корректно");
            }
        }
    }
}