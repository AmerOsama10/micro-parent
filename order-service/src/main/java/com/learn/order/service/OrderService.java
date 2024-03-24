package com.learn.order.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.learn.order.dtos.InventoryResponse;
import com.learn.order.dtos.OrderItemsDto;
import com.learn.order.dtos.OrderRequest;
import com.learn.order.event.OrderPlacedEvent;
import com.learn.order.model.Order;
import com.learn.order.model.OrderItems;
import com.learn.order.repository.OrderRepository;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Observed
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;
	
	public String placeOrder(OrderRequest orderRequest) {
        log.info("Processing order: {}", orderRequest);

		List<OrderItems> orderItems = orderRequest.getOrderItemsDtosList()
													.stream()
													.map(this::mapToOrderItems)
													.toList();

		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderitemsList(orderItems);
		
		List<String> skuCodes =order.getOrderitemsList()
				.stream()
				.map(orderItem->orderItem.getSkuCode()).toList();
	
		log.info("All skuCodes: {}", skuCodes);
		

		//call inventory service , and place order if it is in stock
		InventoryResponse[] inventoryResponses =webClientBuilder.build().get()
		.uri("http://inventory-service/api/inventory",
				uriBuilder->uriBuilder.queryParam("skuCode", skuCodes).build())
		.retrieve()
		.bodyToMono(InventoryResponse[].class)
		.block();
        
		log.info("Received inventory responses: {}", Arrays.toString(inventoryResponses));
	
		boolean allProductsInStock =true;
		   // Check if all products are in stock
		if(inventoryResponses.length == 0 ) {
			   allProductsInStock = false;
			   System.err.println("allProductsInStock is "+allProductsInStock);
			   }
		else {
	     allProductsInStock = Arrays.stream(inventoryResponses)
	            .allMatch(InventoryResponse::isInStock);
	     
	     for (InventoryResponse response : inventoryResponses) {
	    	    System.err.println(response.isInStock());
	    	}

		}
	   


		
        log.info("Does All products are in the stock: {}", allProductsInStock);

		if(allProductsInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
			return "order placed sucessfully";

		}else {
            log.info("Products are not in stock, order cannot be placed: {}", orderRequest);
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
	}

	private OrderItems mapToOrderItems(OrderItemsDto orderItemsDto) {
		OrderItems orderItems = new OrderItems();
		orderItems.setSkuCode(orderItemsDto.getSkuCode());
		orderItems.setPrice(orderItemsDto.getPrice());
		orderItems.setQuantity(orderItemsDto.getQuantity());

		return orderItems;

	}
}
