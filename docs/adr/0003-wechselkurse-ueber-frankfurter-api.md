---
status: "angenommen"
date: "2026-11-27"
decision-makers: "Dozenten (Beispiel aus Vorlesung 5)"
---

# Wechselkurse über die Frankfurter-API, angebunden als HTTP Interface

## Kontext und Problembeschreibung

Kunden aus dem Ausland wollen sehen, was ein Wertpapier in ihrer Währung kostet.
Woher kommen die Kurse, und wie binden wir die fremde API an, ohne dass der
Broker kaputtgeht, wenn sie ausfällt?

## Entscheidungstreiber

* Kostenlos, ohne Anmeldung und ohne API-Key
* Broker bleibt benutzbar, wenn die API langsam oder weg ist
* Wenig Code, gut testbar, passt zu Spring Boot 4

## Betrachtete Optionen

* Frankfurter-API (Kurse der EZB), Client als HTTP Interface (`@HttpExchange`)
* Frankfurter-API, Client mit Spring Cloud OpenFeign (`@FeignClient`)
* Kurse selbst pflegen (Admin trägt sie ein)

## Entscheidungsergebnis

Gewählte Option: "Frankfurter-API mit HTTP Interface", weil die API ohne Key
auskommt und das HTTP Interface seit Spring Framework 6 eingebaut ist. Es
funktioniert wie Feign (nur ein Interface, Spring baut den Client), braucht aber
kein Spring Cloud. OpenFeign wird nur noch gewartet, nicht weiterentwickelt.

### Konsequenzen

* Gut, weil Adresse und Timeouts in `application.properties` stehen, nicht im Code.
* Gut, weil nur `ExchangeRateService` die API kennt. Ändert sie ihr Format, ändern wir eine Stelle.
* Schlecht, weil die EZB nur einmal am Werktag Kurse veröffentlicht. Für einen echten Broker zu ungenau.
* Schlecht, weil wir von einem fremden Dienst abhängen. Fällt er aus, zeigen wir nur Euro-Preise.

### Bestätigung

`QuoteApiTests` startet eine Fake-API, die gut antwortet, einen 500er liefert
oder 3 Sekunden braucht. In allen Fällen antwortet der Broker mit 200,
bei langsamer API nach höchstens 500 ms.

## Vor- und Nachteile der Optionen

### OpenFeign

* Gut, weil in vielen Tutorials und älteren Projekten verbreitet.
* Schlecht, weil zusätzlich Spring Cloud (eigene Versionsliste) nötig ist und das Projekt im Wartungsmodus ist.

### Kurse selbst pflegen

* Gut, weil keine Abhängigkeit nach außen.
* Schlecht, weil Kurse schnell veralten und ein Admin sie täglich pflegen müsste.
