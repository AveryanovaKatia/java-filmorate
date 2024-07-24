package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getById(Long id);

    List<UserDTO> findAll();

    UserDTO create(User user);

    UserDTO update(User user);

    void addNewFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<UserDTO> getAllFriends(Long id);

    List<UserDTO> getMutualFriends(Long id, Long otherId);
}
