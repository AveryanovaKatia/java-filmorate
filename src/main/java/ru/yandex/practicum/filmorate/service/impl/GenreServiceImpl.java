package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAll() {
        log.info("Запрос на получение всех возможных жанров");
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(final int id) {
        log.info("запрос на получение жанра с id {}", id);
        if (genreRepository.findById(id).isEmpty()) {
            log.error("Жанра с id = {} нет.", id);
            throw new NotFoundException("Жанра с id = {} нет." + id);
        }
        return genreRepository.findById(id);
    }
}
