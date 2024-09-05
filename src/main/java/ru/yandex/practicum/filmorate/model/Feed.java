package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * Feed.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feed {
    Integer id;
    @NotNull
    final Integer userId;
    @NotBlank
    final String eventType; // LIKE, REVIEW, FRIEND - тип события
    @NotBlank
    final String operation; // DELETE, PUT, UPDATE - тип операции
    @NotNull
    final Integer entityId; // идентификатор сущности, с которой произошло событие
    @NotNull
    Long timestamp =  System.currentTimeMillis();
}
