package com.learn.order.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDto {
	
    private Long id;

    private String skuCode;
    
    private BigDecimal price;
    
    private Integer quantity;

}
