package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public Optional<List<FilmDTO>> findAll() {
        List<FilmDTO> allFilmDTO = new ArrayList<>();
        films.values().forEach(film -> allFilmDTO.add(getDTO(film)));
        return Optional.of(allFilmDTO);
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
    public FilmDTO putLike(Long id, Long userId) {
        films.get(id).setLikes(userId);
        return getDTO(films.get(id));
    }

    @Override
    public FilmDTO deleteLike(Long id, Long userId) {
        films.get(id).getLikes().remove(userId);
        return getDTO(films.get(id));
    }

    @Override
    public Optional<List<FilmDTO>> getBestFilm(Long count) {
        return Optional.of(films.values().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .map(this::getDTO)
                .collect(Collectors.toList()));
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
