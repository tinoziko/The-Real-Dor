package com.tinoziko.the_real_dor.the_real_dor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// FORCES Spring to find everything!
public class TheRealDorApplication {

    public static void main(String[] args) {
        System.out.println("----> THE MAIN APPLICATION HAS STARTED SUCCESSFULLY!");
        SpringApplication.run(TheRealDorApplication.class, args);
    }

}