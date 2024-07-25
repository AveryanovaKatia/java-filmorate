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

public class FilmsExtractor implements ResultSetExtractor<Map<Integer, Film> > {
    @Override
    public Map<Integer, Film> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> films = new HashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            if (Objects.nonNull(films.get(filmId))) {
                films.get(filmId)
                        .getGenres()
                        .add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                continue;
            }
            Film film = new Film();
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setId(rs.getInt("film_id"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            films.put(filmId, film);
        }
        return films;
    }
}