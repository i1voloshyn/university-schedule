package dev.ivanvoloshyn.universityschedule;

import org.springframework.boot.SpringApplication;

public class TestUniversityScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.from(UniversityScheduleApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
