package ru.yandex.practicum.filmorate.repository.jdbs.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FilmsExtractor implements ResultSetExtractor<Map<Long, Film> > {
    @Override
    public Map<Long, Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> films = new HashMap<>();
        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            if (Objects.nonNull(films.get(filmId))) {
                films.get(filmId)
                        .getGenres()
                        .add(new Genre(rs.getLong("genre_id"), rs.getString("genre")));
                continue;
            }
            Film film = new Film();
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getLong("duration"));
            film.setId(rs.getLong("film_id"));
            film.setMpa(new Mpa(rs.getLong("mpa_id"), rs.getString("mpa")));
            film.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("genre")));
            films.put(filmId, film);
        }
        return films;
    }
}