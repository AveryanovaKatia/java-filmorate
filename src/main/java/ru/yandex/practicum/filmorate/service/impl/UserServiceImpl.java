package ru.yandex.practicum.filmorate.service.impl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDTO> getById(final int id) {
        log.info("Запрос на получение пользователя под id {}", id);
        validId(id);
        return Optional.of(getDTO(userRepository.getById(id).get()));
    }

    public List<UserDTO> findAll() {
        log.info("Запрос на получение списка пользователей");
        return userRepository.findAll().stream().map(this::getDTO).toList();
    }

    public UserDTO create(@Valid @RequestBody final User user) {
        validLogin(user.getLogin());
        User newUser = userRepository.create(user);
        log.info("Пользователь успешно добавлен под id {}", user.getId());
        return getDTO(newUser);
    }

    public UserDTO update(@Validated(UpdateGroup.class) @RequestBody final User user) {
        validId(user.getId());
        validLogin(user.getLogin());
        User newUser = userRepository.update(user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return getDTO(newUser);
    }

    public void addNewFriend(final int id, final int friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя добавить пользователя в друзья к самому себе");
        userRepository.addNewFriend(id, friendId);
        log.info("Пользователь с id {} успешно добавлен в друзья к пользователю с id {}", friendId, id);
    }

    public void deleteFriend(final int id, final int friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя удалить пользователя из друзей у самого себя");
        userRepository.deleteFriend(id, friendId);
        log.info("Пользователь с id {} успешно удален из друзей у пользователя с id {}", friendId, id);
    }

    public List<UserDTO> getAllFriends(final int id) {
        validId(id);
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        return userRepository.getAllFriends(id).stream().map(this::getDTO).toList();
    }

    public List<UserDTO> getMutualFriends(final int id, final int otherId) {
        validUserEqualsFriend(id, otherId, "Нельзя проверять соответствие друзей у себя и себя");
        log.info("Общие друзья пользователь с id {} и пользователя с id {}", otherId, id);
        return userRepository.getMutualFriends(id, otherId).stream().map(this::getDTO).toList();
    }

    private void validLogin(String login) {
        if (login.contains(" ")) {
            log.error("Логин пользователя не должен содержать пробелы");
            throw new ValidationException("Логин пользователя не должен содержать пробелы");
        }
    }

    private void validUserEqualsFriend(final int id, final int friendId, final String message) {
        validId(id);
        validId(friendId);
        if (Objects.equals(id, friendId)) {
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validId(final int id) {
        if (!userRepository.getAllId().contains(id)) {
            log.warn("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Пользователя с id = {} нет." + id);
        }
    }

    private UserDTO getDTO(final User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setName(user.getName());
        userDTO.setBirthday(user.getBirthday());
        return userDTO;
    }
}
