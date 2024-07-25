package ru.yandex.practicum.filmorate.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreRepositoryTest {
    GenreRepository genreRepository;

    @Test
    @Order(1)
    @DisplayName("GenreRepository_findAll")
    void findAllTest() {
        Optional<List<Genre>> genresOptional = Optional.ofNullable(genreRepository.findAll());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        {
                            assertThat(genres).isNotEmpty();
                            assertThat(genres).hasSize(6);
                            assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(genres).element(0)
                                    .hasFieldOrPropertyWithValue("name", "Комедия");
                            assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2);
                            assertThat(genres).element(1)
                                    .hasFieldOrPropertyWithValue("name", "Драма");
                            assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3);
                            assertThat(genres).element(2)
                                    .hasFieldOrPropertyWithValue("name", "Мультфильм");
                            assertThat(genres).element(3).hasFieldOrPropertyWithValue("id", 4);
                            assertThat(genres).element(3)
                                    .hasFieldOrPropertyWithValue("name", "Триллер");
                            assertThat(genres).element(4).hasFieldOrPropertyWithValue("id", 5);
                            assertThat(genres).element(4)
                                    .hasFieldOrPropertyWithValue("name", "Документальный");
                            assertThat(genres).element(5).hasFieldOrPropertyWithValue("id", 6);
                            assertThat(genres).element(5)
                                    .hasFieldOrPropertyWithValue("name", "Боевик");
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("GenreRepository_findById")
    void findByIdTest() {
        Optional<Genre> genreOp = genreRepository.findById(1);
        assertThat(genreOp)
                .isPresent()
                .hasValueSatisfying(genres ->
                {
                    assertThat(genres).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(genres).hasFieldOrPropertyWithValue("name", "Комедия");
                });
    }
}
