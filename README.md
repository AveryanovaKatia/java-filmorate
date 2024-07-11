# java-filmorate

![diagram.svg](diagram.svg)

CREATE TABLE "film" (
"film_id" integer PRIMARY KEY,
"name" varchar(255),
"description" varchar(500),
"relase_data" date,
"duration" integer,
"mpa_id" integer
);

CREATE TABLE "mpa" (
"mpa_id" integer PRIMARY KEY,
"name" varchar(255)
);

CREATE TABLE "genres" (
"genre_id" integer PRIMARY KEY,
"name" varchar(255)
);

CREATE TABLE "film_genres" (
"film_id" integer,
"genre_id" integer,
PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE "user" (
"user_id" integer PRIMARY KEY,
"name" varchar(255),
"login" varchar(255),
"email" varchar(255),
"birthday" date
);

CREATE TABLE "likes" (
"film_id" integer,
"user_id" integer,
PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE "friends" (
"user_id" integer,
"friend_user_id" integer,
"status" boolean,
PRIMARY KEY ("user_id", "friend_user_id")
);

ALTER TABLE "mpa" ADD FOREIGN KEY ("mpa_id") REFERENCES "film" ("mpa_id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("genre_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

