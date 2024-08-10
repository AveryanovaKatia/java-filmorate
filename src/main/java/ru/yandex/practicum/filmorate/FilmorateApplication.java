package ru.yandex.practicum.filmorate;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@SpringBootApplication
@Slf4j
public class FilmorateApplication {
	public static void main(String[] args) {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
		SpringApplication.run(FilmorateApplication.class, args);

		log.info("Приложение запущено");
	}
}
