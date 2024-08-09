package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {
    Optional<Director> getById(final int id);

    List<Director> findAll();

    Director create(final Director director);

    Director update(final Director director);

    void delete(final int id);

    List<Integer> getAllId();
}
