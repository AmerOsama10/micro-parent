package com.learn.order.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learn.order.dtos.OrderItemsDto;
import com.learn.order.dtos.OrderRequest;
import com.learn.order.model.Order;
import com.learn.order.model.OrderItems;
import com.learn.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	public void placeOrder(OrderRequest orderRequest) {
		List<OrderItems> orderItems = orderRequest.getOrderItemsDtosList().stream().map(this::mapToOrderItems).toList();

		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderitemsList(orderItems);
		
		orderRepository.save(order);
	}

	private OrderItems mapToOrderItems(OrderItemsDto orderItemsDto) {
		OrderItems orderItems = new OrderItems();
		orderItems.setSkuCode(orderItemsDto.getSkuCode());
		orderItems.setPrice(orderItemsDto.getPrice());
		orderItems.setQuantity(orderItemsDto.getQuantity());

		return orderItems;

	}
}
