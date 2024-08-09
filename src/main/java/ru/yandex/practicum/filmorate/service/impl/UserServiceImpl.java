package ru.yandex.practicum.filmorate.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public User getById(final int id) {
        log.info("Запрос на получение пользователя под id {}", id);
        validId(id);
        return userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + id + " не существует"));
    }

    @Override
    public List<User> findAll() {
        log.info("Запрос на получение списка пользователей");
        return userRepository.findAll();
    }

    @Override
    public User create(final User user) {
        log.info("Запрос на добавление пользователя");
        User newUser = userRepository.create(user);
        log.info("Пользователь успешно добавлен под id {}", user.getId());
        return newUser;
    }

    @Override
    public User update(final User user) {
        log.info("Запрос на обновление пользователя с id {}", user.getId());
        validId(user.getId());
        User newUser = userRepository.update(user);
        log.info("Пользователь с id {} успешно обновлен", user.getId());
        return newUser;
    }

    @Override
    public void delete(final int id) {
        log.info("Запрос на удаление пользователя с id {}", id);
        validId(id);
        userRepository.delete(id);
    }

    @Override
    public void addNewFriend(final int id, final int friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя добавить пользователя в друзья к самому себе");
        userRepository.addNewFriend(id, friendId);
        log.info("Пользователь с id {} успешно добавлен в друзья к пользователю с id {}", friendId, id);
    }

    @Override
    public void deleteFriend(final int id, final int friendId) {
        validUserEqualsFriend(id, friendId, "Нельзя удалить пользователя из друзей у самого себя");
        userRepository.deleteFriend(id, friendId);
        log.info("Пользователь с id {} успешно удален из друзей у пользователя с id {}", friendId, id);
    }

    @Override
    public List<User> getAllFriends(final int id) {
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        validId(id);
        return userRepository.getAllFriends(id);
    }

    @Override
    public List<User> getMutualFriends(final int id, final int otherId) {
        validUserEqualsFriend(id, otherId, "Нельзя проверять соответствие друзей у себя и себя");
        log.info("Общие друзья пользователь с id {} и пользователя с id {}", otherId, id);
        return userRepository.getMutualFriends(id, otherId);
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
        if (userRepository.getById(id).isEmpty()) {
            log.warn("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Пользователя с id = {} нет." + id);
        }
    }
}
