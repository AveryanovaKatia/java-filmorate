package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    Optional<User> getById(int id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(int id);

    void addNewFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getAllFriends(int id);

    List<User> getMutualFriends(int id, int otherId);
}
