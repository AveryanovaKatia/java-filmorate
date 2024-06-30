package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    @Override
    public Optional<List<UserDTO>> findAll() {
        List<UserDTO> allUserDTO = new ArrayList<>();
        users.values().forEach(user -> allUserDTO.add(getDTO(user)));
        return Optional.of(allUserDTO);
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
    public UserDTO addNewFriend(Long id, Long friendId) {
        users.get(id).setFriends(friendId);
        users.get(friendId).setFriends(id);
        return getDTO(users.get(id));
    }

    @Override
    public UserDTO deleteFriend(Long id, Long friendId) {
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        return getDTO(users.get(id));
    }

    @Override
    public Optional<List<UserDTO>> getAllFriends(Long id) {
        return Optional.of(users.get(id).getFriends().stream().map(users::get).map(this::getDTO).toList());
    }

   @Override
    public Optional<Collection<UserDTO>> getMutualFriends(Long id, Long otherId) {
        Set<Long> user = users.get(id).getFriends();
        Set<Long> other = users.get(otherId).getFriends();
        Set<Long> mutualFriendTds = user.stream().filter(other::contains).collect(Collectors.toSet());
        if (mutualFriendTds.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mutualFriendTds.stream().map(users::get).map(this::getDTO).toList());
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
