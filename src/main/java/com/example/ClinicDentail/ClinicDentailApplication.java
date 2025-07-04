package com.example.ClinicDentail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicDentailApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicDentailApplication.class, args);
		System.out.println(java.util.TimeZone.getDefault());
	}

}
