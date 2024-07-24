package ru.yandex.practicum.filmorate.repository.jdbs.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmExtractor implements ResultSetExtractor<Film> {
    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        Film film = null;
        if (rs.next()) {
            film = new Film();
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getLong("duration"));
            film.setId(rs.getLong("film_id"));
            film.setMpa(new Mpa(rs.getLong("mpa_id"), rs.getString("mpa")));
            long idGenre = rs.getLong("genre_id");
            if (idGenre != 0) {
                do {
                    film.getGenres()
                            .add(new Genre(rs.getLong("genre_id"), rs.getString("genre")));
                } while (rs.next());
            }
        }
        return film;
    }
}
