package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa", mpaRowMapper());
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.
                queryForObject("SELECT * FROM mpa WHERE id = ?", mpaRowMapper(), id));
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getLong("id"), rs.getString("name"));
    }
}
