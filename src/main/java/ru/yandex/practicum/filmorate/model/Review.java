package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.group.UpdateGroup;

import java.util.List;

/**
 * Review.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @NotNull(groups = {UpdateGroup.class})
    @JsonProperty("reviewId")
    Integer id;
    @NotBlank
    String content;
    @NotNull
    @JsonProperty("isPositive")
    final boolean isPositive;
    @NotNull
    final Integer userId;
    @NotNull
    final Integer filmId;
    List<Integer> useful;
}
