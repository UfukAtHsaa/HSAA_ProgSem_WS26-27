package com.example.hello.broker;

import java.math.BigDecimal;

/**
 * Preis eines Wertpapiers in einer anderen Währung.
 * rateAvailable = false heißt: Der Kurs fehlt gerade, price ist dann leer, priceEur gilt weiter.
 */
public record QuoteDto(String symbol, BigDecimal priceEur, String currency, BigDecimal price, boolean rateAvailable) {
}
