package ru.yandex.practicum.filmorate.repository;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    Optional<User> getById(int id);

    List<User> findAll();

    User create(@Valid @RequestBody User user);

    User update(@Validated(UpdateGroup.class) @RequestBody User user);

    void addNewFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getAllFriends(int id);

    List<User> getMutualFriends(int id, int otherId);

    List<Integer> getAllId();
}
