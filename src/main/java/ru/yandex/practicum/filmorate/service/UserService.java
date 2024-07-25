package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getById(int id);

    List<UserDTO> findAll();

    UserDTO create(User user);

    UserDTO update(User user);

    void addNewFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<UserDTO> getAllFriends(int id);

    List<UserDTO> getMutualFriends(int id, int otherId);
}
