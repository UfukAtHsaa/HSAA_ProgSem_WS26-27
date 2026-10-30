---
status: "angenommen"
date: "2026-10-30"
decision-makers: "Dozenten (Beispiel aus Vorlesung 3)"
---

# PostgreSQL als Datenbank

## Kontext und Problembeschreibung

Der Broker speichert Wertpapiere, Benutzer und Orders. Die Daten müssen einen
Neustart von `docker-compose` überstehen, und eine Order darf nur ganz oder gar
nicht gespeichert werden. Welche Datenbank nutzen wir?

## Entscheidungstreiber

* Daten überstehen einen Neustart (Q1)
* Transaktionen für zusammengehörige Änderungen
* Läuft als Container neben dem Backend, ohne Installation auf dem Rechner
* Team kennt SQL aus dem Studium

## Betrachtete Optionen

* PostgreSQL im Container
* H2 im Speicher
* MongoDB

## Entscheidungsergebnis

Gewählte Option: "PostgreSQL im Container", weil sie alle Treiber erfüllt und
Spring Data JPA sie ohne Zusatzaufwand unterstützt.

### Konsequenzen

* Gut, weil die Daten in einem Docker-Volume liegen und Neustarts überstehen.
* Gut, weil Transaktionen mit `@Transactional` direkt funktionieren.
* Schlecht, weil ein dritter Container startet und ein Passwort braucht (in `.env`, nicht im Repo).

### Bestätigung

`docker-compose down` und `up`, danach sind die Orders noch da. Die Tests laufen
mit H2 im Speicher, damit der Build keine Datenbank braucht.

## Vor- und Nachteile der Optionen

### H2 im Speicher

* Gut, weil nichts zu installieren ist.
* Schlecht, weil alle Daten beim Neustart weg sind. Darum nur im Test.

### MongoDB

* Neutral, weil auch als Container verfügbar.
* Schlecht, weil unsere Daten Beziehungen haben (Order gehört zu Benutzer) und das Team SQL besser kennt.
