package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    UserRepository userRepository;
    FilmRepository filmRepository;

    @Override
    public Review getById(final int id) {
        log.info("Запрос на получение отзыва с id = {}", id);
        validId(id);
        return reviewRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Отзыва с id = " + id + " не существует"));
    }

    @Override
    public List<Review> getAll(final int filmId, final int count) {
        if (filmId == 0) {
            log.info("Запрос на получение всех отзывов");
            return reviewRepository.getAll(filmId, count);
        }
        validIdFilm(filmId);
        log.info("Запрос на получение отзывов фильма с id = {}", filmId);
        return reviewRepository.getAll(filmId, count);
    }

    @Override
    public Review create(final Review review) {
        log.info("Запрос на добавление нового отзыва");
        validIdUser(review.getUserId());
        validIdFilm(review.getFilmId());
        final Review newReview = reviewRepository.create(review);
        log.info("Отзыв успешно добавлен под id {}", newReview.getId());
        return newReview;
    }

    @Override
    public Review update(final Review review) {
        log.info("Запрос на обновление отзыва");
        validId(review.getId());
        validIdUser(review.getUserId());
        validIdFilm(review.getFilmId());
        final Review newReview = reviewRepository.update(review);
        log.info("Отзыв с id {} успешно обновлен", newReview.getId());
        return newReview;
    }

    @Override
    public void delete(final int id) {
        log.info("Запрос на удаление отзыва с id {}", id);
        validId(id);
        reviewRepository.delete(id);
    }

    @Override
    public void putLike(final int id, final int userId) {
        log.info("Пользователь с id {} хочет поставить like отзыву с id {}", userId, id);
        validId(id);
        validIdUser(userId);
        reviewRepository.putLike(id, userId);
        log.info("Пользователь с id {} поставил like отзыву с id {}", userId, id);
    }

    @Override
    public void putDislike(final int id, final int userId) {
        log.info("Пользователь с id {} хочет поставить dislike отзыву с id {}", userId, id);
        validId(id);
        validIdUser(userId);
        reviewRepository.putDislike(id, userId);
        log.info("Пользователь с id {} поставил dislike отзыву с id {}", userId, id);
    }

    @Override
    public void deleteLike(final int id, final int userId) {
        log.info("Пользователь с id {} хочет удалить like у отзыва с id {}", userId, id);
        validId(id);
        validIdUser(userId);
        int i = reviewRepository.getReviewLike(id, userId).getFirst();
        if (i < 1) {
            throw new NotFoundException("Лайка нет");
        }
        reviewRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил like у отзыва с id {}", userId, id);
    }

    @Override
    public void deleteDislike(final int id, final int userId) {
        log.info("Пользователь с id {} хочет удалить dislike у отзыва с id {}", userId, id);
        validId(id);
        validIdUser(userId);
        int i = reviewRepository.getReviewLike(id, userId).getFirst();
        if (i > -1) {
            throw new NotFoundException("Дизлайка нет");
        }
        reviewRepository.deleteDislike(id, userId);
        log.info("Пользователь с id {} удалил dislike у отзыва с id {}", userId, id);
    }

    private void validId(final int id) {
        if (!reviewRepository.getAllId().contains(id)) {
            log.error("Отзыва с id = {} нет.", id);
            throw new NotFoundException("Отзыва с id = {} нет." + id);
        }
    }

    private void validIdUser(final int id) {
        if (userRepository.getById(id).isEmpty()) {
            log.error("Пользователя с id = {} нет.", id);
            throw new NotFoundException("Пользователя с id = {} нет." + id);
        }
    }

    private void validIdFilm(final int id) {
        if (filmRepository.getById(id).isEmpty()) {
            log.error("Фильма с id = {} нет.", id);
            throw new NotFoundException("Фильма с id = {} нет." + id);
        }
    }
}
