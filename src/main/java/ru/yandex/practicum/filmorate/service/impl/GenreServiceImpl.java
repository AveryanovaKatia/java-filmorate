package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        log.info("Запрос на получение всех возможных жанров");
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        log.info("запрос на получение жанра с id {}", id);
        return genreRepository.findById(id);
    }
}
