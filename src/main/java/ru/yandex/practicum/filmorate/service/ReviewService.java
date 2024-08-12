package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    Review getById(final int id);

    List<Review> getAll(final int filmId, final int count);

    Review create(final Review review);

    Review update(final Review review);

    void delete(final int id);

    void putLike(final int id, final int userId);

    void putDislike(final int id, final int userId);

    void deleteLike(final int id, final int userId);

    void deleteDislike(final int id, final int userId);
}
