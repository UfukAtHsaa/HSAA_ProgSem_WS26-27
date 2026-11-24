package com.example.hello.broker;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.hello.exchangerate.FakeFrankfurterApi;
import com.example.hello.exchangerate.FakeFrankfurterApi.Mode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integrationstest: echte App, echter HTTP-Client, aber eine Fake-API statt Internet.
 * Beweist, dass der Shop benutzbar bleibt, wenn die API langsam oder kaputt ist.
 */
@SpringBootTest
@AutoConfigureMockMvc
class QuoteApiTests {

	private static final FakeFrankfurterApi FAKE_API = new FakeFrankfurterApi();

	@DynamicPropertySource
	static void useFakeApi(DynamicPropertyRegistry registry) {
		registry.add("spring.http.serviceclient.frankfurter.base-url", FAKE_API::baseUrl);
		// eigene Datenbank, damit dieser Test die anderen nicht stört
		registry.add("spring.datasource.url", () -> "jdbc:h2:mem:quote;DB_CLOSE_DELAY=-1");
	}

	@Autowired
	private MockMvc mvc;

	@Test
	void quote_apiAnswers_returnsConvertedPrice() throws Exception {
		FAKE_API.mode(Mode.OK);

		mvc.perform(get("/api/instruments/SAP/quote?currency=USD"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.price").value(254.54)) // 231.40 * 1.1
			.andExpect(jsonPath("$.rateAvailable").value(true));
	}

	@Test
	void quote_apiReturns500_stillAnswers200WithEuroPrice() throws Exception {
		FAKE_API.mode(Mode.ERROR);

		mvc.perform(get("/api/instruments/SAP/quote?currency=USD"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.priceEur").value(231.40))
			.andExpect(jsonPath("$.rateAvailable").value(false));
	}

	@Test
	void quote_apiTooSlow_givesUpAfterTimeout() throws Exception {
		FAKE_API.mode(Mode.SLOW);
		long start = System.nanoTime();

		mvc.perform(get("/api/instruments/SAP/quote?currency=USD"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.rateAvailable").value(false));

		// Fake braucht 3 s, read-timeout ist 500 ms: Wir dürfen nicht auf die API warten.
		assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(Duration.ofSeconds(2));
	}

	@Test
	void quote_invalidCurrency_is400() throws Exception {
		mvc.perform(get("/api/instruments/SAP/quote?currency=US"))
			.andExpect(status().isBadRequest());
	}
}
