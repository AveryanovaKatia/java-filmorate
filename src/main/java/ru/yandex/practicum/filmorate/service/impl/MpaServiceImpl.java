package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaServiceImpl implements MpaService {
    MpaRepository mpaRepository;

    @Override
    public List<Mpa> findAll() {
        log.info("Запрос на получение всех возможных рейтингов");
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(final int id) {
        log.info("запрос на получение рейтинга с id {}", id);
        return mpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA с id = " + id + " не существует"));
    }
}
