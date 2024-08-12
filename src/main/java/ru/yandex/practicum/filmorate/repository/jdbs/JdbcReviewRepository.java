package ru.yandex.practicum.filmorate.repository.jdbs;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JdbcReviewRepository implements ReviewRepository {
    NamedParameterJdbcOperations jdbc;
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> getById(final int id) {
        String sql = "SELECT r.*, " +
                "SUM(rl.user_id) AS useful " +
                "FROM reviews AS r " +
                "LEFT JOIN review_likes AS rl ON r.review_id = rl.review_id " +
                "WHERE r.review_id = :review_id " +
                "GROUP BY r.review_id; ";
        Review review = jdbc.query(sql, Map.of("review_id", id), rs -> {
            Review review1 = null;
            if (rs.next()) {
                review1 = new Review(rs.getInt("review_id"),
                        rs.getString("content"),
                        rs.getBoolean("is_positive"),
                        rs.getInt("user_id"),
                        rs.getInt("film_id"),
                        rs.getInt("useful"));
            }
            return review1;
        });
        return Optional.ofNullable(review);
    }

    @Override
    public List<Review> getAll(final int filmId, final int count) {
        String s = "SELECT r.*, " +
                "SUM(rl.user_id) AS useful " +
                "FROM reviews AS r " +
                "LEFT JOIN review_likes AS rl ON r.review_id = rl.review_id ";

        String sql;
        Map<String, Object> params;

        if (filmId == 0) {
            sql = s + "GROUP BY r.review_id " +
                    "LIMIT :count; ";
            params = Map.of("count", count);
        } else {
            sql = s + "WHERE r.film_id = :film_id " +
                    "GROUP BY r.review_id " +
                    "LIMIT :count; ";
            params = Map.of("count", count,  "film_id", filmId);
        }

            Map<Integer, Review> reviews = jdbc.query(sql, params, rs -> {
                Map<Integer, Review> reviews1 = new LinkedHashMap<>();
                while (rs.next()) {
                    int id = rs.getInt("review_id");
                    Review review = new Review(rs.getInt("review_id"),
                            rs.getString("content"),
                            rs.getBoolean("is_positive"),
                            rs.getInt("user_id"),
                            rs.getInt("film_id"),
                            rs.getInt("useful"));
                    reviews1.put(id, review);
                }
                return reviews1;
            });
            assert reviews != null;
            return reviews.values().stream().toList();
    }

    @Override
    public Review create(final Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
                "VALUES (:content, :is_positive, :user_id, :film_id); ";
        Map<String, Object> params = new HashMap<>();
        params.put("content", review.getContent());
        params.put("is_positive", review.getIsPositive());
        params.put("user_id", review.getUserId());
        params.put("film_id", review.getFilmId());

        jdbc.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"review_id"});
        review.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

    @Override
    public Review update(final Review review) {
        String sql = "UPDATE reviews SET content = :content " +
                "WHERE review_id = :review_id; ";
        jdbc.update(sql, Map.of("content", review.getContent(), "review_id", review.getId()));
        return review;
    }

    @Override
    public void delete(final int id) {
        String sql = "DELETE FROM reviews WHERE review_id = :review_id; ";
        jdbc.update(sql, Map.of("review_id", id));
    }

    @Override
    public void putLike(final int id, final int userId) {
        String sqlDelete = "DELETE FROM review_likes WHERE review_id = :review_id AND user_id = :user_id;";
        String sql = "INSERT INTO review_likes (review_id, user_id, review_like) " +
                "VALUES (:review_id, :user_id, 1); ";
        jdbc.update(sqlDelete, Map.of("review_id", id, "user_id", userId));
        jdbc.update(sql, Map.of("review_id", id, "user_id", userId));

    }

    @Override
    public void putDislike(final int id, final int userId) {
        String sqlDelete = "DELETE FROM review_likes WHERE review_id = :review_id AND user_id = :user_id;";
        String sql = "INSERT INTO review_likes (review_id, user_id, review_like) " +
                "VALUES (:review_id, :user_id, -1); ";
        jdbc.update(sqlDelete, Map.of("review_id", id, "user_id", userId));
        jdbc.update(sql, Map.of("review_id", id, "user_id", userId));
    }

    @Override
    public void deleteLike(final int id, final int userId) {
        String sql = "DELETE FROM review_likes " +
                "WHERE review_id = :review_id AND user_id = :user_id AND review_like = 1; ";
        jdbc.update(sql, Map.of("review_id", id, "user_id", userId));
    }

    @Override
    public void deleteDislike(final int id, final int userId) {
        String sql = "DELETE FROM review_dislikes " +
                "WHERE review_id = :review_id AND user_id = :user_id AND review_like = -1; ";
        jdbc.update(sql, Map.of("review_id", id, "user_id", userId));
    }

    @Override
    public List<Integer> getAllId() {
        return jdbcTemplate.query("SELECT review_id FROM reviews; ",
                (rs, rowNum) -> rs.getInt("review_id"));
    }

    @Override
    public List<Integer> getReviewLike(final int id, final int userId) {
        String sql = "SELECT review_like " +
                "FROM review_likes " +
                "WHERE review_id = :review_id AND user_id = :user_id ";
        return jdbc.query(sql, Map.of("review_id", id, "user_id", userId),
                (rs, rowNum) -> rs.getInt("review_like"));
    }
}
