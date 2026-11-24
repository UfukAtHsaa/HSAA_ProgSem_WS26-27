---
status: "erfüllt"
prioritaet: "Should"
sprint: "Beispiel (Vorlesung 5)"
quelle: "Eigene Idee"
adr: "ADR-0003"
---

# Preis in fremder Währung

## Grobe Anforderung (Vorgabe)

Eigene Idee: Kunden sehen den Preis eines Wertpapiers auch in ihrer Währung.
Die Kurse kommen aus einer externen API.

### Rahmenbedingungen und Qualität

* Ist die externe API langsam oder nicht erreichbar, bleibt der Broker benutzbar.
* Fehler kommen im Format Problem Details (RFC 9457).

## User Story

Als Kundin aus der Schweiz möchte ich den Preis eines Wertpapiers in Franken
sehen, damit ich einschätzen kann, was es mich kostet.

## Akzeptanzkriterien

### Erwartetes Verhalten

* AK1: Gegeben die API liefert einen Kurs, wenn ich `GET /api/instruments/SAP/quote?currency=CHF` aufrufe, dann sehe ich Euro-Preis und umgerechneten Preis, auf Cent gerundet.

### Fehlerfälle

* AK2: Gegeben die API ist langsam (über 2 s) oder nicht erreichbar, wenn ich den Preis abrufe, dann bekomme ich trotzdem 200 mit dem Euro-Preis und `rateAvailable: false`.
* AK3: Gegeben die Währung ist kein Code aus drei Buchstaben, wenn ich den Preis abrufe, dann bekomme ich 400 mit Problem Details.
* AK4: Gegeben das Wertpapier gibt es nicht, wenn ich den Preis abrufe, dann bekomme ich 404.

## Tests

| AK | Unit-Test (`QuoteServiceTests`) | Integrationstest (`QuoteApiTests`) |
| --- | --- | --- |
| AK1 | `quote_rateAvailable_returnsConvertedPriceRoundedToCents` | `quote_apiAnswers_returnsConvertedPrice` |
| AK2 | `quote_rateUnavailable_keepsEuroPriceAndMarksRateMissing` | `quote_apiReturns500_...`, `quote_apiTooSlow_givesUpAfterTimeout` |
| AK3 | `quote_invalidCurrency_throwsIllegalArgument` (5 Fälle) | `quote_invalidCurrency_is400` |
| AK4 | `quote_unknownSymbol_throwsNotFound` | (offen, gute Übung) |
