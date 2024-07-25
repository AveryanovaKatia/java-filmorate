package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM genres;", genreRowMapper());
    }

    @Override
        public Optional<Genre> findById(final int id) {
            String sql = "SELECT * FROM genres WHERE genre_id = :genre_id;";
            return Optional.ofNullable(jdbc.query(sql, Map.of("genre_id", id), rs -> {
                if (!rs.next()) {
                    return null;
                }
                return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            }));
        }

    @Override
    public List<Genre> getListGenres(List<Integer> ids) {
        String sql = "SELECT * FROM genres WHERE genre_id IN (:genre_id);";
        return jdbc.query(sql, Map.of("genre_id", ids), genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}