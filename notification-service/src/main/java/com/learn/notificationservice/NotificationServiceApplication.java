package com.learn.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

import com.learn.notificationservice.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	
	@KafkaListener(topics="notificationTopic")
	public void handleNotificatin(OrderPlacedEvent orderPlacedEvent) {
		log.info("Recieved Notification for order - {}",orderPlacedEvent.getOrderNumber());
	}
}
