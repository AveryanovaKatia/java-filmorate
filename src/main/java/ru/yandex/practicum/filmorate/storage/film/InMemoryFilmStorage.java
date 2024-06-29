package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public List<FilmDTO> findAll() {
        List<FilmDTO> allFilmDTO = new ArrayList<>();
        films.values().forEach(film -> allFilmDTO.add(getDTO(film)));
        return allFilmDTO;
    }

    @Override
    public FilmDTO create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return getDTO(film);
    }

    @Override
    public FilmDTO update(Film film) {
        films.put(film.getId(), film);
        return getDTO(film);
    }

    @Override
    public Set<Long> putLike (Long id, Long userId) {
        films.get(id).getLikes().add(id);
        return films.get(id).getLikes();
    }

    @Override
    public Set<Long> deleteLike(Long id, Long userId) {
        films.get(id).getLikes().remove(userId);
        return films.get(id).getLikes();
    }

    @Override
    public List<FilmDTO> getBestFilm(Long count) {
        return films.values().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    private FilmDTO getDTO(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        return filmDTO;
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
