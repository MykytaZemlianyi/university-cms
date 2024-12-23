package ua.foxminded.mykyta.zemlianyi.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ua.foxminded.zemlianyi.mykyta.university.dao")
@SpringBootApplication(scanBasePackages = "ua.foxminded.zemlianyi.mykyta.university")
@EntityScan(basePackages = "ua.foxminded.zemlianyi.mykyta.university.dto")
public class UniversityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
    }

}
