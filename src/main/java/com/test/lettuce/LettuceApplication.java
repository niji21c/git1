package com.test.lettuce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LettuceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LettuceApplication.class, args);
		System.out.println("100");
		System.out.println("master");
	}

}
