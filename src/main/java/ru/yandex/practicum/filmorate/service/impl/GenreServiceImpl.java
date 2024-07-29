package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreServiceImpl implements GenreService {
    GenreRepository genreRepository;


    @Override
    public List<Genre> findAll() {
        log.info("Запрос на получение всех возможных жанров");
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(final int id) {
        log.info("запрос на получение жанра с id {}", id);
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с id = " + id + " не существует"));
    }
}
