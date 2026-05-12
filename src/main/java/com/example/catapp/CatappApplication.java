package com.example.catapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CatappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatappApplication.class, args);
	}

}
