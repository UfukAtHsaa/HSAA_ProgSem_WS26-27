---
status: "angenommen"
date: "2026-10-30"
decision-makers: "Dozenten (Beispiel aus Vorlesung 3)"
---

# Basic Auth ohne CSRF-Schutz

## Kontext und Problembeschreibung

Orders und der Admin-Bereich brauchen eine Anmeldung. Wie melden sich Clients
an, und brauchen wir dann den CSRF-Schutz von Spring Security?

## Entscheidungstreiber

* Einfach umzusetzen und zu erklären
* Sperre im Backend, auch beim direkten Aufruf der API (A2)
* Testbar mit der `.http`-Datei

## Betrachtete Optionen

* Basic Auth, CSRF-Schutz aus
* Login mit Session-Cookie, CSRF-Schutz an
* Token (JWT)

## Entscheidungsergebnis

Gewählte Option: "Basic Auth, CSRF-Schutz aus", weil sie am einfachsten ist und
für das Projekt reicht. CSRF-Angriffe nutzen Cookies, die der Browser von selbst
mitschickt. Bei Basic Auth ohne Session gibt es so ein Cookie nicht.

### Konsequenzen

* Gut, weil ein Header pro Request reicht und keine Session nötig ist.
* Schlecht, weil das Passwort bei jedem Request mitgeht. Nur über HTTPS sicher.
* Schlecht, weil das Frontend sich das Passwort merken muss.

### Bestätigung

`BrokerApiTests` prüft 401 ohne Login, 403 als Kundin und 200 als Admin.

## Vor- und Nachteile der Optionen

### Login mit Session-Cookie

* Gut, weil das Passwort nur einmal übertragen wird.
* Schlecht, weil wir dann CSRF-Schutz brauchen und Angular den Token mitschicken muss.

### Token (JWT)

* Gut, weil verbreitet in großen Systemen.
* Schlecht, weil deutlich mehr Code und Konzepte als für das Projekt nötig.
