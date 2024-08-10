package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
//    ReviewService reviewService;
//
//    @GetMapping("/reviews/{id}")
//    public Review getById(@PathVariable @Positive final int id) {
//        return reviewService.getById(id);
//    }
//
//    @GetMapping(/reviews) //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
//    public List<Review> getAll(@RequestParam(required = false) int filmId,
//                               @RequestParam(defaultValue = "10") int count) {
//        return reviewService.getAll(filmId, count);
//    }
//
//    @PostMapping("/reviews")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Review create(@Valid @RequestBody final Review review) {
//        return reviewService.create(review);
//    }
//
//    @PutMapping("/reviews")
//    public Review update(@Validated(UpdateGroup.class) @Valid @RequestBody final Review review) {
//        return reviewService.update(review);
//    }
//
//    @DeleteMapping("/reviews/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable @Positive final int id) {
//        return reviewService.delete(id);
//    }
//
//    @PutMapping("/reviews/{id}/like/{userId}") // пользователь ставит лайк отзыву.
//    public void putLike(@PathVariable @Positive final int id,
//                        @PathVariable @Positive final int userId) {
//        return reviewService.putLike(id, userId);
//    }
//
//    @PutMapping("/reviews/{id}/dislike/{userId}") // пользователь ставит дизлайк отзыву.
//    public void putDislike(@PathVariable @Positive final int id,
//                        @PathVariable @Positive final int userId) {
//        return reviewService.putDislike(id, userId);
//    }
//
//    @DeleteMapping("/reviews/{id}/like/{userId}") //пользователь удаляет лайк/дизлайк отзыву.
//    public void deleteLike(@PathVariable @Positive final int id,
//                           @PathVariable @Positive final int userId) {
//        return reviewService.deleteLike(id, userId);
//    }
//
//    @DeleteMapping("/reviews/{id}/dislike/{userId}") // пользователь удаляет дизлайк отзыву.
//    public void deleteDislike(@PathVariable @Positive final int id,
//                           @PathVariable @Positive final int userId) {
//        return reviewService.deleteDislike(id, userId);
//    }
}
