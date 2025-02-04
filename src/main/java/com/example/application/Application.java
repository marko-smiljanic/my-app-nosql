package com.example.application;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.example.application.entity.Nesto;
import com.example.application.repository.NestoRepository;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "my-app")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner testRepository(NestoRepository nestoRepository) {
        return args -> {
            Nesto nesto = new Nesto();
            nesto.setIme("Testtttttt");
            nesto.setBroj(123);
            nestoRepository.save(nesto);

            System.out.println("Sačuvan entitet: " + nesto);
        };
    }

}
