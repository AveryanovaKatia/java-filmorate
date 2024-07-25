package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.UserExtractor;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.UsersExtractor;

import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JdbcUserRepository implements UserRepository {
    NamedParameterJdbcOperations jdbc;
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> getById(final int id) {
        String sql = "SELECT * " +
                "FROM users u " +
                "JOIN friends f ON u.user_id = f.user_id " +
                "WHERE u.user_id = :id;";
        User user = jdbc.query(sql, Map.of("user_id", id), new UserExtractor());
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * " +
                "FROM users u " +
                "JOIN friends f ON u.user_id = f.user_id; ";
        return jdbc.query(sql, new UsersExtractor());
    }

    @Override
    public User create(final User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (login, name, email, birthday) " +
                "VALUES (:login, :name, :email, :birthday); ";
        Map<String, Object> param = addParams(user);
        jdbc.update(sql, new MapSqlParameterSource().addValues(param), keyHolder, new String[]{"user_id"});
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
        jdbc.update(sql, addParams(user));
        return user;
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
        return jdbc.query(sql, Map.of("user_id", id), new UsersExtractor());
    }

    @Override
    public List<User> getMutualFriends(final int id, final int otherId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_user_id " +
                "FROM friends " +
                "WHERE user_id = :user_id AND friend_user_id = (SELECT friend_user_id " +
                "FROM friends " +
                "WHERE user_id = :other_id)); ";
        return jdbc.query(sql, Map.of("user_id", id, "other_id", otherId), new UsersExtractor());
    }

    @Override
    public List<Integer> getAllId() {
        return jdbcTemplate.queryForObject("SELECT user_id FROM users; ", (rs, rowNum) -> {
            List<Integer> ids = new ArrayList<>();
            if (rs.next()) {
                do {
                    ids.add(rs.getInt("user_id"));
                } while (rs.next());
            }
            return ids;
        });
    }

    private Map<String, Object> addParams(final User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("birthday", user.getBirthday());
        return params;
    }
}
