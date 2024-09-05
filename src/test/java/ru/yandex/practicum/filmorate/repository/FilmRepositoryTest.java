package ru.yandex.practicum.filmorate.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmRepositoryTest {
    FilmRepository filmRepository;

    @Test
    @Order(1)
    @DisplayName("FilmRepository_getById")
    void getByIdTest() {
        Optional<Film> filmOptional = filmRepository.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "филосовский камень");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description1");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 121);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2001, 11, 22));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(film.getGenres()).hasSize(1);
                            assertThat(film.getGenres()).element(0)
                                    .hasFieldOrPropertyWithValue("id", 1);
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("FilmRepository_findAll")
    void findAllTest() {
        Optional<Collection<Film>> filmListOptional = Optional.ofNullable(filmRepository.findAll());
        assertThat(filmListOptional)
                .isPresent()
                .hasValueSatisfying(films -> {
                    assertThat(films).isNotEmpty();
                    assertThat(films).hasSize(7);
                    assertThat(films).element(0).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(films).element(0)
                            .hasFieldOrPropertyWithValue("name", "филосовский камень");
                    assertThat(films).element(1).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(films).element(1)
                            .hasFieldOrPropertyWithValue("name", "тайная комната");
                    assertThat(films).element(2).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(films).element(2)
                            .hasFieldOrPropertyWithValue("name", "узник азкабана");
                    assertThat(films).element(3).hasFieldOrPropertyWithValue("id", 4);
                    assertThat(films).element(3)
                            .hasFieldOrPropertyWithValue("name", "кубок огня");
                    assertThat(films).element(4).hasFieldOrPropertyWithValue("id", 5);
                    assertThat(films).element(4)
                            .hasFieldOrPropertyWithValue("name", "орден феникса");
                    assertThat(films).element(5).hasFieldOrPropertyWithValue("id", 6);
                    assertThat(films).element(5)
                            .hasFieldOrPropertyWithValue("name", "принц полукровка");
                    assertThat(films).element(6).hasFieldOrPropertyWithValue("id", 7);
                    assertThat(films).element(6)
                            .hasFieldOrPropertyWithValue("name", "дары смерти");
                });
    }

    @Test
    @Order(3)
    @DisplayName("FilmRepository_create")
    void createTest() {
        Film newFilm = new Film();
        newFilm.setName("проклятое дитя");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        newFilm.setDuration(128);
        newFilm.setMpa(new Mpa(1, null));

        Optional<Film> filmOptional = Optional.ofNullable(filmRepository.create(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 8);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "проклятое дитя");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description8");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 128);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2022, 1, 1));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1);
                        }
                );
    }

    @Test
    @Order(4)
    @DisplayName("FilmRepository_update")
    void update() {
        Film newFilm = new Film();
        newFilm.setId(8);
        newFilm.setName("фантастические твари");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2001, 11, 22));
        newFilm.setDuration(121);
        newFilm.setMpa(new Mpa(1, null));

        Optional<Film> filmOptional = Optional.ofNullable(filmRepository.update(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 8);
                            assertThat(film).hasFieldOrPropertyWithValue("name",
                                    "фантастические твари");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description8");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 121);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2001, 11, 22));
                        }
                );
    }

    @Test
    @Order(5)
    @DisplayName("FilmRepository_search")
    void searchTest() {

    }

}
