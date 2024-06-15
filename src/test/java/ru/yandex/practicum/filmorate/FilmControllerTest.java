package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmControllerTest {
    FilmController filmController;
    MockMvc mockMvc;
    Film film;

    @BeforeEach
    public void beforeEachTest() {
        filmController = new FilmController();
        mockMvc = MockMvcBuilders.standaloneSetup(new FilmController()).build();
        film = new Film();
    }

    @Test // добавление фильма с корректными полями JUnit
    public void createFilmTestAssert() {
        film.setName("Harry Potter and the Philosopher's Stone");
        film.setDescription("The boy who lived");
        film.setDuration(121L);
        film.setReleaseDate(LocalDate.of(2001, 11, 22));
        filmController.create(film);
        Assertions.assertEquals(film.getId(), 1, "Фильм не добавлен");
    }

    @Test // добавление фильма с корректными полями Mock
    public void createFilmTestMock() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Harry Potter and the Philosopher's Stone\","
                                + "\"description\":\"The boy who lived\","
                                + "\"releaseDate\":\"2001-11-22\",\"duration\":121}"))
                .andExpect(status().isOk());
    }

    @Test // нельзя добавить фильм с неккоректной датой релиза
    public void createWithBadTimeReleaseTest() {
        film.setName("Harry Potter and the Philosopher's Stone");
        film.setDescription("The boy who lived");
        film.setDuration(121L);
        film.setReleaseDate(LocalDate.of(1001, 11, 22));
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film),
                "Не работает проверка на добавление корректной даты релиза");
    }

    @Test // обновление фильма с корректными полями
    public void updateFilmTest() {
        film.setName("Harry Potter and the Philosopher's Stone");
        film.setDescription("The boy who lived");
        film.setDuration(121L);
        film.setReleaseDate(LocalDate.of(2001, 11, 22));
        filmController.create(film);

        Film film1 = new Film();
        film1.setName("Harry Potter and the Chamber of Secrets");
        film1.setDescription("The boy who lived");
        film1.setDuration(174L);
        film1.setId(1L);
        film1.setReleaseDate(LocalDate.of(2002, 11, 14));
        filmController.update(film1);
        Film filmTest = filmController.getFilms().get(1L);
        Assertions.assertEquals(filmTest.getDuration(), 174, "Фильм не обновлен");
    }

    @Test // нельзя обновить фильм с id которого нет
    public void updateWithBadIdTest() {
        film.setName("Harry Potter and the Philosopher's Stone");
        film.setDescription("The boy who lived");
        film.setDuration(121L);
        film.setId(12L);
        film.setReleaseDate(LocalDate.of(2001, 11, 22));
        Assertions.assertThrows(NotFoundException.class, () -> filmController.update(film),
                "Не работает проверка корректности id");
    }


    @Test // нельзя присвоить полю продолжительность отрицательное значение
    public void validBadDurationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Harry Potter and the Philosopher's Stone\","
                                + "\"description\":\"The boy who lived\","
                                + "\"releaseDate\":\"2001-11-22\",\"duration\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test // нельзя присвоить полю имя пустое значение
    public void validBadNameTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \","
                                + "\"description\":\"The boy who lived\","
                                + "\"releaseDate\":\"2001-11-22\",\"duration\":121}"))
                .andExpect(status().isBadRequest());
    }

    @Test // нельзя присвоить полю описание строку размером больше 200 символов
    public void validBadDescriptionTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Harry Potter and the Philosopher's Stone\","
                                + "\"description\":\"The boy who lived. Wenn du zustimmst, können wir deine " +
                                "persönlichen Informationen von einem dieser Amazon-Dienste verwenden, um die" +
                                " Anzeigen, die wir dir auf anderen Diensten zeigen, zu personalisieren. " +
                                "Beispielsweise können " +
                                "wir deinen Prime Video-Wiedergabeverlauf verwenden, um die Werbung, die wir dir in " +
                                "unseren Stores oder auf Fire TV zeigen, zu personalisieren. Wir können auch" +
                                " persönliche Informationen verwenden, die wir von Drittanbietern erhalten " +
                                "(wie demografische Informationen).\","
                                + "\"releaseDate\":\"2001-11-22\",\"duration\":121}"))
                .andExpect(status().isBadRequest());
    }
}
