package com.learn.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class orderservice {

	public static void main(String[] args) {
		SpringApplication.run(orderservice.class, args);
	}
	
	
}
