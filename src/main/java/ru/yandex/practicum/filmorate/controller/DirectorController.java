package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectorController {
    DirectorService directorService;

    @GetMapping("/directors/{id}")
    public Director getById(@PathVariable final int id) {
        return directorService.getById(id);
    }

    @GetMapping("/directors")
    public List<Director> findAll() {
        return directorService.findAll();
    }

    @PostMapping("/directors")
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody final Director director) {
        return directorService.create(director);
    }

    @PutMapping("/directors")
    public Director update(@Valid @RequestBody final Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/directors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive final int id) {
        directorService.delete(id);
    }
}