package ru.yandex.practicum.filmorate.repository.jdbs;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JdbsDirectorRepository implements DirectorRepository {
    NamedParameterJdbcOperations jdbc;
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Director> getById(int id) {
        String sql = "SELECT * " +
                "FROM directors " +
                "WHERE director_id = :director_id; ";
        Director director = jdbc.query(sql, Map.of("director_id", id), rs -> {
            Director director1 = null;
            if (rs.next()) {
                director1 = new Director(rs.getInt("director_id"),
                        rs.getString("director_name"));
            }
            return director1;
        });
        return Optional.ofNullable(director);
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT * FROM directors ";
        return jdbc.query(sql, rs -> {
            List<Director> directorsList = new ArrayList<>();
            while (rs.next()) {
                Director director = new Director(rs.getInt("director_id"),
                        rs.getString("director_name"));
                directorsList.add(director);
            }
            return directorsList;
        });
    }

    @Override
    public Director create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO directors (director_name) " +
                "VALUES (:director_name); ";
        Map<String, Object> params = new HashMap<>();
        params.put("director_name", director.getName());

        jdbc.update(sql, new MapSqlParameterSource().addValues(params), keyHolder, new String[]{"director_id"});
        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE directors SET director_name = :director_name " +
                "WHERE director_id = :director_id; ";
        Map<String, Object> params = new HashMap<>();
        params.put("director_name", director.getName());
        params.put("director_id", director.getId());
        jdbc.update(sql, params);
        return director;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM directors WHERE director_id = :director_id; ";
        jdbc.update(sql, Map.of("director_id", id));
    }

    @Override
    public List<Integer> getAllId() {
        return jdbcTemplate.query("SELECT director_id FROM directors; ",
                (rs, rowNum) -> rs.getInt("director_id"));
    }
}
