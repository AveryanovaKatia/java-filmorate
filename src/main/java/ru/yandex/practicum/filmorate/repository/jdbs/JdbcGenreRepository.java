package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM genres", genreRowMapper());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate
                .queryForObject("SELECT * FROM genres WHERE id = ?", genreRowMapper(), id));
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));
    }
}