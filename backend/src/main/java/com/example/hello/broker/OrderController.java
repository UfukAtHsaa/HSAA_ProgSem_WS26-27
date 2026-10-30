package com.example.hello.broker;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Der Controller übersetzt nur HTTP. Keine fachlichen Regeln hier. */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping
	public List<OrderDto> mine(Principal me) {
		return service.mine(me.getName());
	}

	@GetMapping("/{id}")
	public OrderDto get(@PathVariable long id, Principal me) {
		return service.find(id, me.getName());
	}

	@PostMapping
	public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderRequest req, Principal me) {
		OrderDto o = service.place(req, me.getName());
		URI uri = URI.create("/api/orders/" + o.id());
		return ResponseEntity.created(uri).body(o);
	}
}
