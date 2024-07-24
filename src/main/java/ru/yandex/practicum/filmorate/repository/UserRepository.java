package ru.yandex.practicum.filmorate.repository;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    Optional<User> getById(Long id);

    List<User> findAll();

    User create(@Valid @RequestBody User user);

    User update(@Validated(UpdateGroup.class) @RequestBody User user);

    void addNewFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getAllFriends(Long id);

    List<User> getMutualFriends(Long id, Long otherId);

    List<Long> getAllId();
}
