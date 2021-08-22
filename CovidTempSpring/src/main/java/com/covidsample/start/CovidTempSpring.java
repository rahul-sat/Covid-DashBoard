package com.covidsample.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidTempSpring {

	public static void main(String[] args) {
		SpringApplication.run(CovidTempSpring.class, args);
	}

}
