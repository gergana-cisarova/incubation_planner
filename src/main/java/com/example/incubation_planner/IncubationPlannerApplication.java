package com.example.incubation_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IncubationPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncubationPlannerApplication.class, args);
    }

}
