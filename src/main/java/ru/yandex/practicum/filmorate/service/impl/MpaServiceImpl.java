package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> findAll() {
        log.info("Запрос на получение всех возможных рейтингов");
        return mpaRepository.findAll();
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        log.info("запрос на получение рейтинга с id {}", id);
        return mpaRepository.findById(id);
    }
}
