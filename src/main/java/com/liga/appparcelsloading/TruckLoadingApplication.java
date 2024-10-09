package com.liga.appparcelsloading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения {@code TruckLoadingApplication}, который отвечает за запуск Spring Boot приложения.
 * <p>
 * Этот класс содержит метод {@code main}, который является точкой входа в приложение и использует
 * {@link SpringApplication#run(Class, String...)} для запуска Spring Boot контекста.
 * </p>
 *
 * <p><b>Пример запуска приложения:</b></p>
 * <pre>
 *     java -jar truck-loading-app.jar
 * </pre>
 *
 * <p>Аннотация {@link SpringBootApplication} объединяет в себе следующие аннотации:</p>
 * <ul>
 *     <li>{@code @Configuration}: Указывает, что класс содержит бины для контекста Spring.</li>
 *     <li>{@code @EnableAutoConfiguration}: Включает автоматическую настройку Spring на основе зависимостей.</li>
 *     <li>{@code @ComponentScan}: Включает автоматическое сканирование компонентов в пакете и его подмодулях.</li>
 * </ul>
 *
 * <p>Этот класс необходим для корректной работы Spring Boot приложения, отвечающего за загрузку посылок в грузовики.</p>
 *
 * @author [Alexandr Krylov]
 */
@SpringBootApplication
public class TruckLoadingApplication {
    public static void main(String[] args) {
        SpringApplication.run(TruckLoadingApplication.class, args);
    }
}
