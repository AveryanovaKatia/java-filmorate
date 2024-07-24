package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
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
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final JdbcTemplate jdbcTemplate;
    String select = "SELECT u.user_id AS id, " +
            "u.name, " +
            "u.email, " +
            "u.login, " +
            "u.birthday " +
            "s.friend_user_id AS friend_id ";

    @Override
    public Optional<User> getById(Long id) {
        String sql = select +
                "FROM users u " +
                "JOIN friends f ON u.user_id = f.user_id; " +
                "WHERE u.user_id = :id;";
        User user = jdbc.query(sql, Map.of("user_id", id), new UserExtractor());
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = select +
                "FROM users u " +
                "JOIN friends f ON u.user_id = f.user_id; ";
        return jdbc.query(sql, new UsersExtractor());
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (login, name, email, birthday) " +
                "VALUES ( :login, :name, :email, :birthday); ";
        Map<String, Object> param = addParams(user);
        jdbc.update(sql, new MapSqlParameterSource().addValues(param), keyHolder, new String[]{"user_id"});
        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET login = :login, " +
                "name = :name, " +
                "email = :email, " +
                "birthday = :birthday " +
                "WHERE user_id = :user_id; ";
        jdbc.update(sql, addParams(user));
        return user;
    }

    @Override
    public void addNewFriend(Long id, Long friendId) {
        String sql = "MERGE INTO friends (user_id, friend_id) " +
                "VALUES (:user_id, :friend_id); ";
        jdbc.update(sql, Map.of("user_id", id, "friend_id", friendId));
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friends " +
                "WHERE user_id = :user_id AND friend_id = :friend_id; ";
        jdbc.update(sql, Map.of("user_id", id, "friend_id", friendId));
    }

    @Override
    public List<User> getAllFriends(Long id) {
        String sql = select +
                "FROM users u " +
                "WHERE users_id IN (SELECT f.friend_id " +
                "FROM friends AS f " +
                "WHERE users_id = :user_id); ";
        return jdbc.query(sql, Map.of("user_id", id), new UsersExtractor());
    }

    @Override
    public List<User> getMutualFriends(Long id, Long otherId) {
        String sql = select +
                "FROM users u " +
                "WHERE users_id IN (SELECT f.friend_id " +
                "FROM friends AS f " +
                "WHERE users_id = :user_id AND f.friend_id = (SELECT fr.friend_id " +
                "FROM friends AS fr " +
                "WHERE fr.user_id = :other_id)); ";
        return jdbc.query(sql, Map.of("user_id", id, "other_id", otherId), new UsersExtractor());
    }

    @Override
    public List<Long> getAllId() {
        return jdbcTemplate.queryForObject("SELECT user_id FROM users; ", (rs, rowNum) -> {
            List<Long> ids = new ArrayList<>();
            if (rs.next()) {
                do {
                    ids.add(rs.getLong("user_id"));
                } while (rs.next());
            }
            return ids;
        });
    }

    private Map<String, Object> addParams(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("birthday", user.getBirthday());
        return params;
    }
}
