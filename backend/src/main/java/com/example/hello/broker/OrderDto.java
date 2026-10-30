package com.example.hello.broker;

import java.math.BigDecimal;

/** DTO: das sieht der Client. Kein internalNote, kein ownerId. */
public record OrderDto(Long id, String symbol, int quantity, BigDecimal price) {

	static OrderDto from(Order o) {
		return new OrderDto(o.getId(), o.getSymbol(), o.getQuantity(), o.getPrice());
	}
}
