package com.learn.inventoryservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.learn.inventoryservice.dto.InventoryRequest;
import com.learn.inventoryservice.dto.InventoryResponse;
import com.learn.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;

	//localhost:port/api/inventory/skuCode=iphone-13,skuCode=iphone-14,..
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> availableInStock(@RequestParam List<String> skuCode) {
		return inventoryService.availableInStock(skuCode);
		
	}
	
	
	//localhost:port/api/inventory/iphone-13
//	@GetMapping("/{sku-code}")
//	@ResponseStatus(HttpStatus.OK)
//	public boolean isInStock(@PathVariable("skuCode") String skuCode) {
//		return inventoryService.isInStock(skuCode);
//		
//	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void isInStock(@RequestBody InventoryRequest inventoryRequest) {
		inventoryService.createInventory(inventoryRequest);
		
	}
}
