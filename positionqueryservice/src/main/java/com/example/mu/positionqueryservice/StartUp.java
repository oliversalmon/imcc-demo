package com.example.mu.positionqueryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.example.mu.positionqueryservice")
//@EnableDiscoveryClient
public class StartUp {

	public static void main(String[] args) {
		SpringApplication.run(StartUp.class, args);

	}

}
