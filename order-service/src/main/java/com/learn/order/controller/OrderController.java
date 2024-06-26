package com.learn.order.controller;


import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.learn.order.dtos.OrderRequest;
import com.learn.order.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
    private final OrderService orderService;   
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
	@TimeLimiter(name="inventory")
	@Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Received order request: {}", orderRequest);
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));		
	}
	
	
	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(()-> "Oops! something went wrong , please order after some time !") ;
		
	}
	
	
}