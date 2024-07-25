package ru.yandex.practicum.filmorate.repository;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.jdbs.JdbcFilmRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.time.LocalDate;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTests {
    private final JdbcFilmRepository filmRepository;

    @Test
    public void createTest() {
        Film film1 = new Film();
        film1.setName("Harry Potter and the Philosopher's Stone");
        film1.setDescription("The boy who lived");
        film1.setReleaseDate(LocalDate.of(2001,11,22));
        film1.setDuration(121);
        film1.setMpa(new Mpa(1, null));

        Film film2 = filmRepository.create(film1);
        assertThat(film2).hasFieldOrPropertyWithValue("name", "Harry Potter and the Philosopher's Stone");
        assertThat(film2).hasFieldOrPropertyWithValue("description", "The boy who lived");
        assertThat(film2).hasFieldOrPropertyWithValue("releaseDate",
                LocalDate.of(2001,11,22));
        assertThat(film2).hasFieldOrPropertyWithValue("duration", 121);
    }
}
