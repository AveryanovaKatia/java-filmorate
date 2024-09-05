package ru.yandex.practicum.filmorate.repository.jdbs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.repository.FeedRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JdbcFeedRepository implements FeedRepository {
    NamedParameterJdbcOperations jdbc;

    @Override
    public void create(Feed feed) {
        String sql = "INSERT INTO feeds (user_id, event_type, operation, entiti_id, timestamp) " +
                "VALUES (:userId, :eventType, :operation, :entityId, :timestamp)";

        Map<String, Object> params = Map.of("userId", feed.getUserId(),
                "eventType", feed.getEventType(),
                "operation", feed.getOperation(),
                "entityId", feed.getEntityId(),
                "timestamp", feed.getTimestamp());

        jdbc.update(sql, params);
    }

    @Override
    public List<Feed> getUserFeeds(int id) {
        String sql = " SELECT * FROM feeds WHERE user_id = :userId ";

        return jdbc.query(sql, Map.of("userId", id), rs -> {
            List<Feed> events = new ArrayList<>();
            while (rs.next()) {
                 Feed feed = new Feed(
                        rs.getInt("user_id"),
                        rs.getString("event_type"),
                        rs.getString("operation"),
                        rs.getInt("entiti_id")
                );
                feed.setId(rs.getInt("feed_id"));
                feed.setTimestamp(rs.getLong("timestamp"));
                events.add(feed);
            }
            return events;
        });
    }
}
