package com.example.hello.exchangerate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Antwort der Frankfurter-API, genau in deren Format. Verlässt dieses Paket nicht:
 * Ändert die API ihr Format, ändern wir nur hier etwas, nicht im ganzen Shop.
 *
 * Beispiel: {"amount":1.0,"base":"EUR","date":"2026-11-24","rates":{"USD":1.0963}}
 */
public record FrankfurterRates(BigDecimal amount, String base, LocalDate date, Map<String, BigDecimal> rates) {
}
