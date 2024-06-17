package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<UserDTO> findAll() {
        log.info("Запрос на получение списка пользователей");
        List<UserDTO> allUserDTO = new ArrayList<>();
        users.values().forEach(film -> allUserDTO.add(getDTO(film)));
        return allUserDTO;
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody User user) {
        validLogin(user.getLogin());
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен под id {}", user.getId());
        return getDTO(user);
    }

    @PutMapping
    public UserDTO update(@Validated(UpdateGroup.class) @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователя с id = {} нет.", user.getId());
            throw new NotFoundException("Пользователя с id = {} нет." + user.getId());
        }
        validLogin(user.getLogin());
        users.put(user.getId(), user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return getDTO(user);
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validLogin(String login) {
        if (login.contains(" ")) {
            log.error("Логин пользователя не должен содержать пробелы");
            throw new ValidationException("Логин пользователя не должен содержать пробелы");
        }
    }

    private UserDTO getDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setName(user.getName());
        userDTO.setBirthday(user.getBirthday());
        return userDTO;
    }
}