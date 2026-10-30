---
status: "erfüllt"
prioritaet: "Must"
sprint: "Beispiel (Vorlesung 3)"
quelle: "Eigene Idee"
adr: "ADR-0002"
---

# Order aufgeben

## Grobe Anforderung (Vorgabe)

Eigene Idee: Angemeldete Kunden kaufen Wertpapiere zum aktuellen Preis.

### Rahmenbedingungen und Qualität

* Der Preis kommt vom Server, nie vom Client.
* Fehler kommen im Format Problem Details (RFC 9457).

## User Story

Als Kundin möchte ich eine Order für ein Wertpapier aufgeben, damit ich es zum
aktuellen Preis kaufe.

## Akzeptanzkriterien

### Erwartetes Verhalten

* Gegeben ich bin angemeldet, wenn ich `POST /api/orders` mit Symbol und Menge schicke, dann bekomme ich 201 und einen `Location`-Header auf die neue Order.
* Gegeben ich schicke zusätzlich einen Preis, wenn die Order gespeichert wird, dann gilt trotzdem der Preis aus der Datenbank.
* Gegeben ich habe Orders aufgegeben, wenn ich `GET /api/orders` aufrufe, dann sehe ich nur meine eigenen.

### Fehlerfälle

* Gegeben die Menge ist 0 oder negativ, wenn ich die Order schicke, dann bekomme ich 400 mit Problem Details.
* Gegeben das Symbol gibt es nicht, wenn ich die Order schicke, dann bekomme ich 400 mit einer Meldung, welches Symbol fehlt.
* Gegeben ich bin nicht angemeldet, wenn ich die Order schicke, dann bekomme ich 401.
