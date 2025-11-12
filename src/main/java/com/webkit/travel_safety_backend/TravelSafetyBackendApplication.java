package com.webkit.travel_safety_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TravelSafetyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelSafetyBackendApplication.class, args);
    }

}
