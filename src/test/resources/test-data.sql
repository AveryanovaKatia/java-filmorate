INSERT into mpa(rating_id, name) values ( 1, 'G' );
INSERT into mpa(rating_id, name) values ( 2, 'PG' );
INSERT into mpa(rating_id, name) values ( 3, 'PG-13' );
INSERT into mpa(rating_id, name) values ( 4, 'R' );
INSERT into mpa(rating_id, name) values ( 5, 'NC-17' );
INSERT into genres(genre_id, name) values ( 1, 'Комедия' );
INSERT into genres(genre_id, name) values ( 2, 'Драма' );
INSERT into genres(genre_id, name) values ( 3, 'Мультфильм' );
INSERT into genres(genre_id, name) values ( 4, 'Триллер' );
INSERT into genres(genre_id, name) values ( 5, 'Документальный' );
INSERT into genres(genre_id, name) values ( 6, 'Боевик' );
INSERT INTO users(name, login, email, birthday)
VALUES('рон', 'уизли', 'гриффиндор@mail.ru', '1993-03-12'),
      ('невелл', 'долгопуппс', 'когтевран@mail.ru', '1994-02-02'),
      ('винсент', 'креб', 'слизерин@mail.ru', '1992-12-01'),
      ('седрик', 'диггори', 'пуффендуй@mail.ru', '1990-05-24');
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('филосовский камень', 'description1', '2001-11-22', '121', 1);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('тайная комната', 'description2', '2002-11-14', '174', 2);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('узник азкабана', 'description3', '2003-11-15', '180', 3);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('кубок огня', 'description4', '2004-11-06', '167', 4);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('орден феникса', 'description5', '2005-11-12', '188', 5);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('принц полукровка', 'description6', '2006-11-24', '167', 5);
INSERT INTO films(name, description, release_date, duration, mpa_id)
VALUES ('дары смерти', 'description7', '2010-11-07', '198', 5);
INSERT INTO film_genres(film_id, genre_id) VALUES (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 5);
INSERT INTO friends(user_id, friend_user_id) VALUES (1, 2), (1, 3), (2, 1), (2, 4), (3, 1), (3, 4);
