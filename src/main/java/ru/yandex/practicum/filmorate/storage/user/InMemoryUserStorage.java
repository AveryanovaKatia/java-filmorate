package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> allUserDTO = new ArrayList<>();
        users.values().forEach(user -> allUserDTO.add(getDTO(user)));
        return allUserDTO;
    }

    @Override
    public UserDTO create(User user) {
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return getDTO(user);
    }

    @Override
    public UserDTO update(User user) {
        users.put(user.getId(), user);
        return getDTO(user);
    }

    @Override
    public Set<Long> addNewFriend(Long id, Long friendId) {
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        return users.get(id).getFriends();
    }

    @Override
    public Set<Long> deleteFriend(Long id, Long friendId) {
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        return users.get(id).getFriends();
    }

    @Override
    public List<UserDTO> getAllFriends(long id) {
        return users.get(id).getFriends().stream().map(users::get).map(this::getDTO).toList();
    }

   @Override
    public List<UserDTO> getMutualFriends(long id, long otherId) {
        Set<Long> user = users.get(id).getFriends();
        Set<Long> other = users.get(otherId).getFriends();
        Set<Long> mutualFriendTds = user.stream().filter(other::contains).collect(Collectors.toSet());
        return mutualFriendTds.stream().map(users::get).map(this::getDTO).toList();
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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
