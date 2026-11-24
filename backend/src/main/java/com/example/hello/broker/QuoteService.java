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
		if (!code.matches("[A-Z]{3}")) {
			throw new IllegalArgumentException("Währung muss ein Code aus drei Buchstaben sein, z. B. USD.");
		}
		return code;
	}

	private static BigDecimal convert(BigDecimal amount, BigDecimal rate) {
		return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
	}
}
