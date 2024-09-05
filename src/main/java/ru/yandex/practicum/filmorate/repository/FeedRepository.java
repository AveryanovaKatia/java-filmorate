package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedRepository {
    void create(Feed feed);

    List<Feed> getUserFeeds(int id);
}
