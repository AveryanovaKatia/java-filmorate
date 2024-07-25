package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM mpa;";
        return jdbc.query(sql, new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
            }
        });
    }

    @Override
    public Optional<Mpa> findById(final int id) {
        String sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = :mpa_id";
        Map<String, Object> param = Map.of("mpa_id", id);
        return Optional.ofNullable(jdbc.query(sql, param, new ResultSetExtractor<Mpa>() {
            @Override
            public Mpa extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (!rs.next()) {
                    return null;
                }
                return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
            }
        }));
    }
}