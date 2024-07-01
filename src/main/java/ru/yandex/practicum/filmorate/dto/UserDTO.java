package ru.yandex.practicum.filmorate.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friends = new TreeSet<>();
}
