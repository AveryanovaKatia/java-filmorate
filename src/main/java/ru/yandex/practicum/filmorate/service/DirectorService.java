package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    Director getById(int id);

    List<Director> findAll();

    Director create(Director director);

    Director update(Director director);

    void delete(int id);
}
