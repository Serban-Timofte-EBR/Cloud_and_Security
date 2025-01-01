package eu.learn.ro;

import eu.learn.ro.model.Task;
import eu.learn.ro.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Spring Boot application started");
    }

    @Bean
    CommandLineRunner initData(TaskRepository taskRepository) {
        return args -> {
            taskRepository.save(new Task(10000L, "Learn Spring Boot", "Understand Spring Boot basics"));
            taskRepository.save(new Task(10001L, "Learn Hibernate", "Practice Hibernate ORM"));
            System.out.println("Sample data initialized!");
        };
    }
}