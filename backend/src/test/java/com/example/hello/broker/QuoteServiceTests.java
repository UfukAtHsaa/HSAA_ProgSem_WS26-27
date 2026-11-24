package com.example.hello.broker;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.hello.exchangerate.ExchangeRateService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests für QuoteService. Kein Spring, keine Datenbank, kein Netz.
 * Jeder Test gehört zu einem Akzeptanzkriterium aus docs/anforderungen/0002-preis-in-fremdwaehrung.md.
 */
@ExtendWith(MockitoExtension.class)
class QuoteServiceTests {

	@Mock
	private InstrumentRepository instruments;

	@Mock
	private ExchangeRateService exchangeRates;

	@InjectMocks
	private QuoteService service;

	private static final Instrument SAP = new Instrument("SAP", "SAP SE", new BigDecimal("231.40"));

	@Test
	@DisplayName("AK1: Preis wird mit dem Kurs umgerechnet und auf Cent gerundet")
	void quote_rateAvailable_returnsConvertedPriceRoundedToCents() {
		// Arrange
		when(instruments.findBySymbol("SAP")).thenReturn(Optional.of(SAP));
		when(exchangeRates.rate("EUR", "USD")).thenReturn(Optional.of(new BigDecimal("1.0963")));

		// Act
		QuoteDto quote = service.quote("SAP", "USD");

		// Assert: 231.40 * 1.0963 = 253.68382 -> 253.68
		assertThat(quote.price()).isEqualByComparingTo("253.68");
		assertThat(quote.currency()).isEqualTo("USD");
		assertThat(quote.rateAvailable()).isTrue();
	}

	@Test
	@DisplayName("AK2: Ohne Kurs bleibt der Euro-Preis, umgerechneter Preis fehlt")
	void quote_rateUnavailable_keepsEuroPriceAndMarksRateMissing() {
		when(instruments.findBySymbol("SAP")).thenReturn(Optional.of(SAP));
		when(exchangeRates.rate("EUR", "USD")).thenReturn(Optional.empty());

		QuoteDto quote = service.quote("SAP", "USD");

		assertThat(quote.priceEur()).isEqualByComparingTo("231.40");
		assertThat(quote.price()).isNull();
		assertThat(quote.rateAvailable()).isFalse();
	}

	@Test
	@DisplayName("Euro braucht keine externe API")
	void quote_currencyEur_doesNotCallExternalApi() {
		when(instruments.findBySymbol("SAP")).thenReturn(Optional.of(SAP));

		QuoteDto quote = service.quote("SAP", "eur");

		assertThat(quote.price()).isEqualByComparingTo("231.40");
		verify(exchangeRates, never()).rate(anyString(), anyString());
	}

	@Test
	@DisplayName("AK4: Unbekanntes Wertpapier gibt 404")
	void quote_unknownSymbol_throwsNotFound() {
		when(instruments.findBySymbol("XYZ")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.quote("XYZ", "USD"))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("XYZ");
	}

	@ParameterizedTest(name = "Währung \"{0}\" wird abgelehnt")
	@ValueSource(strings = { "", "US", "USDX", "12A", "€" })
	@DisplayName("AK3: Ungültige Währung gibt 400, ohne die API zu fragen")
	void quote_invalidCurrency_throwsIllegalArgument(String currency) {
		assertThatThrownBy(() -> service.quote("SAP", currency))
			.isInstanceOf(IllegalArgumentException.class);
		verify(exchangeRates, never()).rate(anyString(), anyString());
	}
}
