package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorServiceImp implements DirectorService {
    DirectorRepository directorRepository;

    @Override
    public Director getById(int id) {
        log.info("Запрос на получение режиссера с id = {}", id);
        validId(id);
        return directorRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Режиссера с id = " + id + " не существует"));
    }

    @Override
    public List<Director> findAll() {
        log.info("Запрос на получение списка режиссеров");
        return directorRepository.findAll();
    }

    @Override
    public Director create(Director director) {
        log.info("Запрос на добавление нового режиссера");
        Director newDirector = directorRepository.create(director);
        log.info("Режиссер успешно добавлен под id {}", newDirector.getId());
        return newDirector;
    }

    @Override
    public Director update(Director director) {
        log.info("Запрос на обновление режиссера");
        validId(director.getId());
        Director newDirector = directorRepository.update(director);
        log.info("Режиссер с id {} успешно обновлен", director.getId());
        return newDirector;
    }

    @Override
    public void delete(int id) {
        log.info("Запрос на удаление режиссера");
        validId(id);
        directorRepository.delete(id);
        log.info("Режисер с id {} успешно удален", id);
    }

    private void validId(final int id) {
        if (!directorRepository.getAllId().contains(id)) {
            log.error("Режиссера с id = {} нет.", id);
            throw new NotFoundException("Режиссера с id = {} нет." + id);
        }
    }
}
