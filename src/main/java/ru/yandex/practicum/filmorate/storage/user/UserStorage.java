package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    Optional<List<UserDTO>> findAll();

    UserDTO create(@Valid @RequestBody User user);

    UserDTO update(@Validated(UpdateGroup.class) @RequestBody User user);

    UserDTO addNewFriend(Long id, Long friendId);

    UserDTO deleteFriend(Long id, Long friendId);

    Optional<List<UserDTO>> getAllFriends(Long id);

    Optional<Collection<UserDTO>> getMutualFriends(Long id, Long otherId);

    Map<Long, User> getUsers();
}
