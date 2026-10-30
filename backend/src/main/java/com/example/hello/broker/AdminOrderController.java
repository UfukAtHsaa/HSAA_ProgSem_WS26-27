package com.example.hello.broker;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Nur mit Rolle ADMIN erreichbar. Die Sperre steht in der SecurityConfig, nicht hier. */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

	private final OrderService service;

	public AdminOrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping
	public PageResponse<OrderDto> all(@RequestParam(required = false) String symbol, Pageable pageable) {
		return service.all(symbol, pageable);
	}
}
