package com.solidwall.tartib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication
public class TartibApplication {

	public static void main(String[] args) {
		SpringApplication.run(TartibApplication.class, args);
		System.out.println("-------------------------------------");
		System.out.println("* * * Application run correctly * * *");		
		System.out.println("-------------------------------------");
	}
 
}