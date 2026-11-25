package com.example.hello.broker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.hello.exchangerate.ExchangeRateService;

/** Rechnet den Preis eines Wertpapiers in eine andere Währung um. */
@Service
public class QuoteService {

	private static final String EUR = "EUR";

	private final InstrumentRepository instruments;
	private final ExchangeRateService exchangeRates;

	public QuoteService(InstrumentRepository instruments, ExchangeRateService exchangeRates) {
		this.instruments = instruments;
		this.exchangeRates = exchangeRates;
	}

	public QuoteDto quote(String symbol, String currency) {
		String target = normalizeCurrency(currency);
		Instrument instrument = instruments.findBySymbol(symbol)
			.orElseThrow(() -> new NotFoundException("Wertpapier " + symbol + " gibt es nicht."));
		BigDecimal priceEur = instrument.getPrice();

		if (target.equals(EUR)) {
			return new QuoteDto(symbol, priceEur, EUR, priceEur, true);
		}
		return exchangeRates.rate(EUR, target)
			.map(rate -> new QuoteDto(symbol, priceEur, target, convert(priceEur, rate), true))
			.orElseGet(() -> new QuoteDto(symbol, priceEur, target, null, false));
	}

	private static String normalizeCurrency(String currency) {
		String code = currency == null ? "" : currency.trim().toUpperCase(Locale.ROOT);
		// DEMO 3: Zeile auskommentieren -> QuoteServiceTests (AK3) und QuoteApiTests werden rot.
		requireCurrencyCode(code);
		return code;
	}

	private static void requireCurrencyCode(String code) {
		if (!code.matches("[A-Z]{3}")) {
			throw new IllegalArgumentException("Währung muss ein Code aus drei Buchstaben sein, z. B. USD.");
		}
	}

	private static BigDecimal convert(BigDecimal amount, BigDecimal rate) {
		BigDecimal result = amount.multiply(rate);
		// DEMO 2: Zeile auskommentieren -> der Unit-Test für AK1 wird rot. Der Integrationstest nicht. Warum?
		result = result.setScale(2, RoundingMode.HALF_UP);
		return result;
	}
}
