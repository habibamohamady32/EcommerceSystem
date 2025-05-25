package com.ecomfurniture.ecomsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ecomfurniture.ecomsys.entity")
@EnableJpaRepositories("com.ecomfurniture.ecomsys.repositories")
public class EcomsysApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomsysApplication.class, args);
	}

}
