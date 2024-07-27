package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;
import java.util.Optional;

public interface MpaService {
    List<Mpa> findAll();

    Mpa findById(int id);
}
