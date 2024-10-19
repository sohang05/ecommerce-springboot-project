package com.wipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EcommerceCapstoneProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceCapstoneProjectApplication.class, args);
	}

}
