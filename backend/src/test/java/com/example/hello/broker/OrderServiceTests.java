package com.example.hello.broker;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.hello.security.AppUser;
import com.example.hello.security.AppUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit-Test ohne Spring und ohne Datenbank. Das geht, weil der Service seine
 * Helfer über den Konstruktor bekommt: Wir reichen einfach Fakes (Mocks) hinein.
 */
class OrderServiceTests {

	private final OrderRepository orders = mock(OrderRepository.class);
	private final InstrumentRepository instruments = mock(InstrumentRepository.class);
	private final AppUserRepository users = mock(AppUserRepository.class);
	private final OrderService service = new OrderService(orders, instruments, users);

	@Test
	void priceComesFromTheServerNotFromTheClient() {
		when(instruments.findBySymbol("SAP")).thenReturn(Optional.of(new Instrument("SAP", "SAP SE", new BigDecimal("231.40"))));
		when(users.findByEmail("anna@example.org")).thenReturn(Optional.of(new AppUser("anna@example.org", "hash", "KUNDE")));
		when(orders.save(any(Order.class))).thenAnswer(call -> call.getArgument(0));

		OrderDto order = service.place(new CreateOrderRequest("SAP", 3), "anna@example.org");

		assertThat(order.price()).isEqualByComparingTo("231.40");
		assertThat(order.quantity()).isEqualTo(3);
	}

	@Test
	void unknownSymbolIsRejected() {
		when(instruments.findBySymbol("XYZ")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.place(new CreateOrderRequest("XYZ", 1), "anna@example.org"))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
