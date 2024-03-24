package com.learn.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learn.inventoryservice.dto.InventoryRequest;
import com.learn.inventoryservice.dto.InventoryResponse;
import com.learn.inventoryservice.model.Inventory;
import com.learn.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	public final InventoryRepository inventoryRepository;

//	@Transactional(readOnly =true)
//	public boolean isInStock(String skuCode) {
//		return inventoryRepository.findBySkuCode(skuCode).isPresent();
//	}

	@Transactional(readOnly = true)
	@SneakyThrows
	public List<InventoryResponse> availableInStock(List<String> skuCode) {
//		log.info("Wait Started");
//		Thread.sleep(10000);
//		log.info("Wait Ended");

		return inventoryRepository.findBySkuCodeIn(skuCode)
				.stream()
				.map(inventory -> InventoryResponse.builder()
				.skuCode(inventory.getSkuCode())
				.isInStock(inventory.getQuantity() > 0)
				.build())
				.toList();
	}

	public void createInventory(InventoryRequest inventoryRequest) {
		Inventory inventory = Inventory.builder().skuCode(inventoryRequest.getSkuCode())
				.quantity(inventoryRequest.getQuantity()).build();

		inventoryRepository.save(inventory);
		log.info("inventory {} is saved", inventory.getId());
	}

}
