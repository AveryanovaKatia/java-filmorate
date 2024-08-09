package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.UserExtractor;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.UsersExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JdbcUserRepository implements UserRepository {
    NamedParameterJdbcOperations jdbc;
    UserExtractor userExtractor;
    UsersExtractor usersExtractor;

    @Override
    public Optional<User> getById(final int id) {
        String sql = "SELECT * " +
                "FROM users AS u " +
                "LEFT JOIN friends f ON u.user_id = f.user_id " +
                "WHERE u.user_id = :user_id;";
        User user = jdbc.query(sql, Map.of("user_id", id), userExtractor);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * " +
                "FROM users u; ";
                //"LEFT JOIN friends f ON u.user_id = f.user_id"; ";
        return jdbc.query(sql, usersExtractor);
    }

    @Override
    public User create(final User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday); ";
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        jdbc.update(sql, new MapSqlParameterSource().addValues(params), keyHolder, new String[]{"user_id"});
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(final User user) {
        String sql = "UPDATE users SET login = :login, " +
                "name = :name, " +
                "email = :email, " +
                "birthday = :birthday " +
                "WHERE user_id = :user_id; ";
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());
        jdbc.update(sql, params);
        return user;
    }

    @Override
    public void delete(final int id) {
        String sql = "DELETE FROM users WHERE user_id = :user_id; ";
        jdbc.update(sql, Map.of("user_id", id));
    }

    @Override
    public void addNewFriend(final int id, final int friendId) {
        String sql = "MERGE INTO friends (user_id, friend_user_id) " +
                "VALUES (:user_id, :friend_user_id); ";
        jdbc.update(sql, Map.of("user_id", id, "friend_user_id", friendId));
    }

    @Override
    public void deleteFriend(final int id, final int friendId) {
        String sql = "DELETE FROM friends " +
                "WHERE user_id = :user_id AND friend_user_id = :friend_user_id; ";
        jdbc.update(sql, Map.of("user_id", id, "friend_user_id", friendId));
    }

    @Override
    public List<User> getAllFriends(final int id) {
        String sql = "SELECT * " +
                "FROM users u " +
                "WHERE user_id IN (SELECT friend_user_id " +
                "FROM friends " +
                "WHERE user_id = :user_id); ";
        return jdbc.query(sql, Map.of("user_id", id), usersExtractor);
    }

    @Override
    public List<User> getMutualFriends(final int id, final int otherId) {
        String sql =  "SELECT * " +
                "FROM users " +
                "WHERE user_id IN (SELECT f.friend_user_id " +
                "FROM users AS u " +
                "LEFT JOIN friends AS f ON u.user_id = f.user_id " +
                "WHERE u.user_id = :user_id AND f.friend_user_id IN (SELECT fr.friend_user_id " +
                "FROM users AS us " +
                "LEFT JOIN friends fr ON us.user_id = fr.user_id " +
                "WHERE us.user_id = :other_id));";

        return jdbc.query(sql, Map.of("user_id", id, "other_id", otherId), usersExtractor);
    }
}