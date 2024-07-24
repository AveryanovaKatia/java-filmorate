//package ru.yandex.practicum.filmorate;
//
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.yandex.practicum.filmorate.controller.UserController;
//import ru.yandex.practicum.filmorate.dto.UserDTO;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class UserControllerTest {
//    UserController userController;
//    MockMvc mockMvc;
//    User user;
//
//    @BeforeEach
//    public void beforeEachTest() {
//        userController = new UserController(new UserServiceImpl(new InMemoryUserStorage()));
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(userController)
//                .build();
//        user = new User();
//    }
//
//    @Test // добавление фильма с корректными полями Mock
//    public void createUserTestMock() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"gromgrommolnia@yandex.ru\","
//                                + "\"login\":\"gromgrommolnia\","
//                                + "\"name\":\"Katia\","
//                                + "\"birthday\":\"1993-12-15\"}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test // нельзя добавить пользователя с некорректным логином
//    public void validBadLoginTest() {
//        user.setEmail("gromgrommolnia@yandex.ru");
//        user.setLogin("grom grommolnia");
//        user.setName("Katia");
//        user.setBirthday(LocalDate.of(1993, 12, 15));
//        Assertions.assertThrows(ValidationException.class, () -> userController.create(user),
//                "Не работает проверка корректности логина");
//    }
//
//    @Test // нельзя передать некорректно логин
//    public void validBadLoginAnTest() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"gromgrommolnia@yandex.ru\","
//                                + "\"login\":\"\","
//                                + "\"name\":\"Katia\","
//                                + "\"birthday\":\"1993-12-15\"}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test // обновление пользователя с корректными полями
//    public void updateUserTest() {
//        user.setEmail("gromgrommolnia@yandex.ru");
//        user.setLogin("gromgrommolnia");
//        user.setName("Katia");
//        user.setBirthday(LocalDate.of(1993, 12, 15));
//        userController.create(user);
//
//        User user1 = new User();
//        user1.setEmail("gromgrommolnia@yandex.ru");
//        user1.setLogin("gromgrommolnia");
//        user1.setName("Lena");
//        user1.setId(1L);
//        user1.setBirthday(LocalDate.of(1993, 12, 15));
//        userController.update(user1);
//        List<UserDTO> userTest = userController.findAll();
//        Assertions.assertEquals(userTest.getFirst().getName(), "Lena", "Имя не обновлено");
//    }
//
//    @Test // нельзя передать некорректно имейл
//    public void validBadEmailTest() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"gromgrommolnia\","
//                                + "\"login\":\"gromgrommolnia\","
//                                + "\"name\":\"Katia\","
//                                + "\"birthday\":\"1993-12-15\"}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test // нельзя передать некорректно дату рождения
//    public void validBadBirthdayTest() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"gromgrommolnia@yandex.ru\","
//                                + "\"login\":\"gromgrommolnia\","
//                                + "\"name\":\"Katia\","
//                                + "\"birthday\":\"2024-12-15\"}"))
//                .andExpect(status().isBadRequest());
//    }
//}
