package com.example.hello.exchangerate;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * Die einzige Stelle im Backend, die mit der fremden API spricht (Adapter).
 * Nach außen gibt es nur "Kurs da" oder "Kurs nicht da", keine HTTP-Fehler.
 */
@Service
public class ExchangeRateService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

	private final FrankfurterClient client;

	public ExchangeRateService(FrankfurterClient client) {
		this.client = client;
	}

	/** Kurs von einer Währung in eine andere, z. B. EUR nach USD. Leer, wenn die API gerade nicht hilft. */
	public Optional<BigDecimal> rate(String from, String to) {
		try {
			FrankfurterRates response = client.latest(from, to);
			if (response == null || response.rates() == null) {
				return Optional.empty();
			}
			return Optional.ofNullable(response.rates().get(to));
		}
		catch (RestClientException e) {
			// Timeout, API down, 4xx/5xx: alles landet hier. Loggen, aber den Shop nicht abstürzen lassen.
			log.warn("Wechselkurs {} -> {} nicht verfügbar: {}", from, to, e.getMessage());
			// DEMO 1: Fallback abschalten. return auskommentieren und throw einkommentieren.
			// Dann wird jeder API-Fehler zu einer 500 im Shop, und QuoteApiTests zeigt es.
			return Optional.empty();
			// throw e;
		}
	}
}
