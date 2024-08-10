package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/users/{id}")
    public User getById(@PathVariable @Positive final int id) {
        return userService.getById(id);
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@Validated(UpdateGroup.class) @Valid @RequestBody final User user) {
        return userService.update(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive final int id) {
        userService.delete(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable @Positive final int id, @PathVariable @Positive final int friendId) {
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable @Positive final int id, @PathVariable @Positive final int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable @Positive final int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable final int id, @PathVariable final int otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping("/users/{id}/recommendations")
    public List<Film> recommendations(@PathVariable final int id) {
        return userService.recommendations(id);
    }
}