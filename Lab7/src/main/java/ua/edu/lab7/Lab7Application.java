package ua.edu.lab7;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Lab7Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab7Application.class, args);
    }
}
