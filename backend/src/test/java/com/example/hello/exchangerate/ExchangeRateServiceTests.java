package com.example.hello.exchangerate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Der Adapter übersetzt die fremde API in "Kurs da" oder "Kurs nicht da". */
class ExchangeRateServiceTests {

	private final FrankfurterClient client = mock(FrankfurterClient.class);
	private final ExchangeRateService service = new ExchangeRateService(client);

	@Test
	void rate_apiAnswers_returnsRate() {
		when(client.latest("EUR", "USD"))
			.thenReturn(new FrankfurterRates(BigDecimal.ONE, "EUR", LocalDate.now(), Map.of("USD", new BigDecimal("1.0963"))));

		assertThat(service.rate("EUR", "USD")).contains(new BigDecimal("1.0963"));
	}

	@Test
	void rate_apiNotReachable_returnsEmptyInsteadOfThrowing() {
		when(client.latest("EUR", "USD")).thenThrow(new ResourceAccessException("Timeout"));

		assertThat(service.rate("EUR", "USD")).isEmpty();
	}

	@Test
	void rate_currencyMissingInResponse_returnsEmpty() {
		when(client.latest("EUR", "USD"))
			.thenReturn(new FrankfurterRates(BigDecimal.ONE, "EUR", LocalDate.now(), Map.of()));

		assertThat(service.rate("EUR", "USD")).isEmpty();
	}
}
