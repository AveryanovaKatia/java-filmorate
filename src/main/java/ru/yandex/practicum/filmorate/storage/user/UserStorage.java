package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {
    Map<Long, User> users = new HashMap<>();

    List<UserDTO> findAll();

    UserDTO create(@Valid @RequestBody User user);

    UserDTO update(@Validated(UpdateGroup.class) @RequestBody User user);

    Set<Long> addNewFriend(Long id, Long friendId);

    Set<Long> deleteFriend(Long id, Long friendId);

    List<UserDTO> getAllFriends(long id);

    List<UserDTO> getMutualFriends(long id, long otherId);

    Map<Long, User> getUsers();
}
