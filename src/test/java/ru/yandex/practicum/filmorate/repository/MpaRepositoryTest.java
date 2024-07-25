package ru.yandex.practicum.filmorate.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Collection;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MpaRepositoryTest {
    MpaRepository mpaRepository;

    @Test
    @Order(1)
    @DisplayName("MpaRepository_findAll")
    void findAllTeat() {
        Optional<Collection<Mpa>> ratingMPAOptional = Optional.ofNullable(mpaRepository.findAll());
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                {
                    assertThat(mpa).isNotEmpty();
                    assertThat(mpa).hasSize(5);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("name", "G");
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("name", "PG");
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("name", "PG-13");
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("id", 4);
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("name", "R");
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("id", 5);
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("name", "NC-17");
                });
    }

    @Test
    @Order(2)
    @DisplayName("MpaRepository_findById")
    void findByIdTest() {
        Optional<Mpa> ratingMPAOptional = mpaRepository.findById(1);
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }
}
