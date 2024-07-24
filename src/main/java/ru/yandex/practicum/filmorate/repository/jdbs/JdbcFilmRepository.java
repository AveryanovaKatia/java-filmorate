package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.FilmsExtractor;
import ru.yandex.practicum.filmorate.repository.jdbs.extractor.FilmExtractor;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final JdbcTemplate jdbcTemplate;
    String select = "SELECT f.film_id, " +
            "f.name AS film_name, " +
            "f.description, " +
            "f.releaseDate, " +
            "f.duration, " +
            "f.mpa_id" +
            "m.name AS mpa, " +
            "g.genre_id" +
            "g.name AS genre ";

    @Override
    public Optional<Film> getDyId(Long id) {
        String sql = select +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id" +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id" +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = :film_id; ";
        Film film = jdbc.query(sql, Map.of("film_id", id), new FilmExtractor());
        return Optional.ofNullable(film);
    }

    @Override
    public List<Film> findAll() {
        String sql = select +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id" +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id" +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id; ";
        Map<Long, Film> films = jdbc.query(sql, Map.of(), new FilmsExtractor());
        assert films != null;
        return films.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO FILMS (name, description, releaseDate, duration, mpa) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpa); ";
        Map<String, Object> params = addParams(film);
        jdbc.update(sql, new MapSqlParameterSource().addValues(params), keyHolder, new String[]{"film_id"});
        film.setId(keyHolder.getKeyAs(Long.class));
        addGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET name = :name," +
                "description = :description," +
                "release_date = :release_date," +
                "duration = :duration," +
                "mpa = :mpa" +
                "WHERE film_id = :film_id; ";
        jdbc.update(sql, addParams(film));
        addGenres(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public void putLike(Long id, Long userId) {
        String sql = "MERGE INTO LIKES(film_id, user_id) VALUES ( :film_id, :user_id ); ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID = :film_id AND USER_ID = :user_id; ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }

    @Override
    public Collection<Film> getBestFilm(Long count) {
        String sql = select +
                "COUNT(l.film_id) AS likes_count" +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id" +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id" +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id" +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id" +
                "GROUP BY film_id, fg.genre_id" +
                "ORDER BY likes_count DESC" +
                "LIMIT :count; ";
        Map<Long, Film> films = jdbc.query(sql, Map.of("count", count), new FilmsExtractor());
        assert films != null;
        return films.values().stream().toList();
    }

    @Override
    public List<Long> getAllId() {
        return jdbcTemplate.queryForObject("SELECT film_id FROM films; ", (rs, rowNum) -> {
            List<Long> ids = new ArrayList<>();
            if (rs.next()) {
                do {
                    ids.add(rs.getLong("film_id"));
                } while (rs.next());
            }
            return ids;
        });
    }

    private void addGenres(Long filmId, Set<Genre> genres) {
        Map<String, Object>[] batch = new HashMap[genres.size()];
        int count = 0;
        for (Genre genre : genres) {
            Map<String, Object> map = new HashMap<>();
            map.put("film_id", filmId);
            map.put("genre_id", genre.getId());
            batch[count++] = map;
        }
        String sqlDelete = "DELETE FROM film_genres WHERE film_id = :film_id AND genre_id = :genre_id; ";
        String sqlInsert = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id); ";
        jdbc.batchUpdate(sqlDelete, batch);
        jdbc.batchUpdate(sqlInsert, batch);
    }

    private Map<String, Object> addParams(Film film) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpa", film.getMpa());
        return params;
    }
}
