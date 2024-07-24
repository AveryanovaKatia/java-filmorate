package ru.yandex.practicum.filmorate.service.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userStorage;

    @Override
    public Optional<UserDTO> getById(Long id) {
        log.info("Запрос на получение пользователя под id {}", id);
        validId(id);
        return Optional.of(getDTO(userStorage.getById(id).get()));
    }

    public List<UserDTO> findAll() {
        log.info("Запрос на получение списка пользователей");
        return userStorage.findAll().stream().map(this::getDTO).toList();
    }

    public UserDTO create(@Valid @RequestBody User user) {
        validLogin(user.getLogin());
        User newUser = userStorage.create(user);
        log.info("Пользователь успешно добавлен под id {}", user.getId());
        return getDTO(newUser);
    }

    public UserDTO update(@Validated(UpdateGroup.class) @RequestBody User user) {
        validId(user.getId());
        validLogin(user.getLogin());
        User newUser = userStorage.update(user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return getDTO(newUser);
    }

    public void addNewFriend(Long id, Long friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя добавить пользователя в друзья к самому себе");
        userStorage.addNewFriend(id, friendId);
        log.info("Пользователь с id {} успешно добавлен в друзья к пользователю с id {}", friendId, id);
    }

    public void deleteFriend(Long id, Long friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя удалить пользователя из друзей у самого себя");
        userStorage.deleteFriend(id, friendId);
        log.info("Пользователь с id {} успешно удален из друзей у пользователя с id {}", friendId, id);
    }

    public List<UserDTO> getAllFriends(Long id) {
        validId(id);
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        return userStorage.getAllFriends(id).stream().map(this::getDTO).toList();
    }

    public List<UserDTO> getMutualFriends(Long id, Long otherId) {
        validUserEqualsFriend(id, otherId, "Нельзя проверять соответствие друзей у себя и себя");
        log.info("Общие друзья пользователь с id {} и пользователя с id {}", otherId, id);
        return userStorage.getMutualFriends(id, otherId).stream().map(this::getDTO).toList();
    }

    private void validLogin(String login) {
        if (login.contains(" ")) {
            log.error("Логин пользователя не должен содержать пробелы");
            throw new ValidationException("Логин пользователя не должен содержать пробелы");
        }
    }

    private void validUserEqualsFriend(Long id, Long friendId, String message) {
        validId(id);
        validId(friendId);
        if (Objects.equals(id, friendId)) {
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validId(Long id) {
        if (!userStorage.getAllId().contains(id)) {
            log.warn("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Пользователя с id = {} нет." + id);
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
