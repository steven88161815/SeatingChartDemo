package com.example.seating;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.seating.mapper")
public class SeatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatingApplication.class, args);
	}

}
