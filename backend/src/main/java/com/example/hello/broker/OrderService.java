package com.example.hello.broker;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hello.security.AppUser;
import com.example.hello.security.AppUserRepository;

/** Hier stehen die fachlichen Regeln. Controller und Repository bleiben dumm. */
@Service
public class OrderService {

	private final OrderRepository orders;
	private final InstrumentRepository instruments;
	private final AppUserRepository users;

	// Kein new: Spring reicht die Repositories über den Konstruktor hinein.
	public OrderService(OrderRepository orders, InstrumentRepository instruments, AppUserRepository users) {
		this.orders = orders;
		this.instruments = instruments;
		this.users = users;
	}

	/** Eine Order des angemeldeten Kunden. Fremde Orders gibt es für ihn nicht: 404. */
	public OrderDto find(long id, String email) {
		AppUser me = currentUser(email);
		return orders.findById(id)
			.filter(o -> o.getOwnerId().equals(me.getId()))
			.map(OrderDto::from)
			.orElseThrow(() -> new NotFoundException("Order " + id + " gibt es nicht."));
	}

	public List<OrderDto> mine(String email) {
		return orders.findByOwnerId(currentUser(email).getId()).stream().map(OrderDto::from).toList();
	}

	@Transactional
	public OrderDto place(CreateOrderRequest req, String email) {
		Instrument instrument = instruments.findBySymbol(req.symbol())
			.orElseThrow(() -> new IllegalArgumentException("Wertpapier " + req.symbol() + " gibt es nicht."));
		// Der Preis kommt aus der Datenbank, nie aus dem Request.
		Order order = new Order(instrument.getSymbol(), req.quantity(), instrument.getPrice(), currentUser(email).getId());
		return OrderDto.from(orders.save(order));
	}

	/** Für Admins: alle Orders, optional nach Symbol gefiltert. */
	public PageResponse<OrderDto> all(String symbol, Pageable pageable) {
		var page = (symbol == null) ? orders.findAll(pageable) : orders.findBySymbol(symbol, pageable);
		return PageResponse.from(page.map(OrderDto::from));
	}

	private AppUser currentUser(String email) {
		return users.findByEmail(email).orElseThrow();
	}
}
