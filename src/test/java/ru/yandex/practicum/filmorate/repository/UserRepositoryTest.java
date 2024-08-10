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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Genre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepositoryTest {
    UserRepository userRepository;
    FilmRepository filmRepository;

    @Test
    @Order(1)
    @DisplayName("UserRepository_findById")
    void findByIdTest() {
        Optional<User> userOptional = userRepository.getById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "уизли");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "рон");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "гриффиндор@mail.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1993, 3, 12));
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("UserRepository_findAll")
    void findAllTest() {
        Optional<Collection<User>> userList = Optional.ofNullable(userRepository.findAll());
        assertThat(userList)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).isNotEmpty();
                    assertThat(user).hasSize(4);
                    assertThat(user).element(0).hasFieldOrPropertyWithValue("id", 1);
                    assertThat(user).element(1).hasFieldOrPropertyWithValue("id", 2);
                    assertThat(user).element(2).hasFieldOrPropertyWithValue("id", 3);
                    assertThat(user).element(3).hasFieldOrPropertyWithValue("id", 4);
                });
    }

    @Test
    @Order(3)
    @DisplayName("UserRepository_create")
    void createTest() {
        User newUser = new User();
        newUser.setLogin("qrommolnia");
        newUser.setName("grom molnia");
        newUser.setEmail("grommolnia@yangex.ru");
        newUser.setBirthday(LocalDate.of(1993, 12, 15));

        Optional<User> userOptional = Optional.ofNullable(userRepository.create(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "qrommolnia");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "grom molnia");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "grommolnia@yangex.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1993, 12, 15));
                        }
                );
    }

    @Test
    @Order(4)
    @DisplayName("UserRepository_update")
    void updateTest() {
        User newUser = new User();
        newUser.setId(5);
        newUser.setLogin("gromqrommolnia");
        newUser.setName("gromgrom molnia");
        newUser.setEmail("gromgrommolnia@yangex.ru");
        newUser.setBirthday(LocalDate.of(1993, 12, 15));

        Optional<User> userOptional = Optional.ofNullable(userRepository.update(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "gromqrommolnia");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "gromgrom molnia");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "gromgrommolnia@yangex.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1993, 12, 15));
                        }
                );
    }

    @Test
    @Order(5)
    @DisplayName("UserRepository_getAllFriends")
    void getAllFriendsTest() {
        Optional<List<User>> usersOptional = Optional.ofNullable(userRepository.getAllFriends(1));
        assertThat(usersOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).first().hasFieldOrPropertyWithValue("id", 2);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 3);
                        }
                );
    }

    @Test
    @Order(6)
    @DisplayName("UserRepository_getMutualFriends")
    void getMutualFriendsTest() {
        Optional<List<User>> commonFriendsOptional = Optional
                .ofNullable(userRepository.getMutualFriends(2, 3));
        assertThat(commonFriendsOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 4);
                        }
                );
    }


    @Test
    @Order(7)
    @DisplayName("UserRepository_Recommendations_ForTwoLikes")
    public void recommendationsForTwoLikesTest() {
        User newUser = new User();
        newUser.setLogin("Katia");
        newUser.setName("ka tia");
        newUser.setEmail("katia@yangex.ru");
        newUser.setBirthday(LocalDate.of(1993, 12, 15));
        int newUserId = userRepository.create(newUser).getId();

        User newUser2 = new User();
        newUser2.setLogin("Valeria");
        newUser2.setName("vale ria");
        newUser2.setEmail("valeria@yangex.ru");
        newUser2.setBirthday(LocalDate.of(1993, 12, 15));
        int newUser2Id = userRepository.create(newUser2).getId();

        Film newFilm = new Film();
        newFilm.setName("проклятое дитя");
        newFilm.setDescription("description");
        newFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        newFilm.setDuration(128);
        newFilm.setMpa(new Mpa(1, null));
        int newFilmId = filmRepository.create(newFilm).getId();
        newFilm.setId(newFilmId);
        filmRepository.putLike(newFilmId, newUserId);
        filmRepository.putLike(newFilmId, newUser2Id);

        Film newFilm2 = new Film();
        newFilm2.setName("фантастические твари");
        newFilm2.setDescription("description");
        newFilm2.setReleaseDate(LocalDate.of(2001, 11, 22));
        newFilm2.setDuration(121);
        newFilm2.setMpa(new Mpa(1, "G"));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, "Комедия"));
        newFilm2.setGenres(genres);
        int newFilm2Id = filmRepository.create(newFilm2).getId();
        newFilm2.setId(newFilm2Id);
        filmRepository.putLike(newFilm2Id, newUserId);
        List<Film> films = new ArrayList<>();
        films.add(newFilm2);

        assertThat(userRepository.recommendations(newUser2Id)).hasSize(1);
        assertEquals(films, userRepository.recommendations(newUser2Id));
    }

    @Test
    @Order(8)
    @DisplayName("UserRepository_Recommendations_ForNoLikes")
    public void recommendationsForNoLikesTest() {
        User newUser = new User();
        newUser.setLogin("Taia");
        newUser.setName("ta ia");
        newUser.setEmail("taia@yangex.ru");
        newUser.setBirthday(LocalDate.of(1993, 12, 15));
        int newUserId = userRepository.create(newUser).getId();

        User newUser2 = new User();
        newUser2.setLogin("Raia");
        newUser2.setName("ra ia");
        newUser2.setEmail("raia@yangex.ru");
        newUser2.setBirthday(LocalDate.of(1993, 12, 15));
        int newUser2Id = userRepository.create(newUser2).getId();

        Film newFilm = new Film();
        newFilm.setName("Тайны Дамблдора");
        newFilm.setDescription("description");
        newFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        newFilm.setDuration(128);
        newFilm.setMpa(new Mpa(1, null));
        int newFilmId = filmRepository.create(newFilm).getId();
        newFilm.setId(newFilmId);
        filmRepository.putLike(newFilmId, newUserId);

        Film newFilm2 = new Film();
        newFilm2.setName("Преступление Гриндевальда");
        newFilm2.setDescription("description");
        newFilm2.setReleaseDate(LocalDate.of(2001, 11, 22));
        newFilm2.setDuration(121);
        newFilm2.setMpa(new Mpa(1, "G"));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, "Комедия"));
        newFilm2.setGenres(genres);
        int newFilm2Id = filmRepository.create(newFilm2).getId();
        newFilm2.setId(newFilm2Id);
        filmRepository.putLike(newFilm2Id, newUserId);
        List<Film> films = new ArrayList<>();
        films.add(newFilm2);

        assertThat(userRepository.recommendations(newUser2Id)).hasSize(0);
        assertThat(userRepository.recommendations(newUserId)).hasSize(0);
    }


    @Test
    @Order(9)
    @DisplayName("UserRepository_Recommendations_ForNoAnotherLikes")
    public void recommendationsForNoAnotherLikesTest() {
        User newUser = new User();
        newUser.setLogin("Ania");
        newUser.setName("An ia");
        newUser.setEmail("Ania@yangex.ru");
        newUser.setBirthday(LocalDate.of(1993, 12, 15));
        int newUserId = userRepository.create(newUser).getId();

        User newUser2 = new User();
        newUser2.setLogin("Vania");
        newUser2.setName("Va nia");
        newUser2.setEmail("Vania@yangex.ru");
        newUser2.setBirthday(LocalDate.of(1993, 12, 15));
        int newUser2Id = userRepository.create(newUser2).getId();

        Film newFilm = new Film();
        newFilm.setName("Наитемнейшее искуство");
        newFilm.setDescription("description");
        newFilm.setReleaseDate(LocalDate.of(2022, 1, 1));
        newFilm.setDuration(128);
        newFilm.setMpa(new Mpa(1, null));
        int newFilmId = filmRepository.create(newFilm).getId();
        newFilm.setId(newFilmId);
        filmRepository.putLike(newFilmId, newUserId);
        filmRepository.putLike(newFilmId, newUser2Id);

        Film newFilm2 = new Film();
        newFilm2.setName("История");
        newFilm2.setDescription("description8");
        newFilm2.setReleaseDate(LocalDate.of(2001, 11, 22));
        newFilm2.setDuration(121);
        newFilm2.setMpa(new Mpa(1, "G"));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, "Комедия"));
        newFilm2.setGenres(genres);
        int newFilm2Id = filmRepository.create(newFilm2).getId();
        newFilm2.setId(newFilm2Id);
        filmRepository.putLike(newFilm2Id, newUserId);
        filmRepository.putLike(newFilm2Id, newUser2Id);
        List<Film> films = new ArrayList<>();
        films.add(newFilm2);

        assertThat(userRepository.recommendations(newUser2Id)).hasSize(0);
        assertThat(userRepository.recommendations(newUserId)).hasSize(0);
    }
}
