---
name: adr
description: Legt ein Architecture Decision Record (ADR) im MADR-4.0-Format in docs/adr/ des Projekts an. Verwenden, wenn der Nutzer ein ADR erstellen will, eine Architektur-, Technologie- oder Designentscheidung dokumentieren möchte, oder Begriffe wie "ADR", "Architecture Decision Record", "Entscheidungsprotokoll", "decision record", "Entscheidung aufschreiben" nennt.
---

# ADR erstellen (MADR 4.0)

Diese Anleitung führt schrittweise durch die Erstellung eines Architecture Decision
Records. Immer auf Deutsch, immer im MADR-4.0-Format, immer in `docs/adr/`.

## Grundregeln

- **Ein ADR = eine Entscheidung.** Nie zwei Entscheidungen in einer Datei.
- **Fragen stellen statt raten.** Jede offene Frage wird über das `question`-Tool
  mit konkreten Optionen gestellt. Nur was der Nutzer nicht beantworten will oder
  was eindeutig aus dem Projekt hervorgeht, wird selbst ergänzt — und dann im
  Entwurf zur Freigabe vorgelegt.
- **ADRs sind unveränderlich.** Nach `angenommen` wird nichts mehr umgeschrieben
  außer `status` und `date`. Eine spätere Änderung erzeugt ein neues ADR, das das
  alte als `ersetzt durch ADR-NNNN` markiert.
- **Dateiname:** `NNNN-kurztitel-mit-bindestrichen.md`, Kleinbuchstaben, `.md`.
  `NNNN` ist die nächste freie vierstellige Nummer (0001, 0002, …).

## Ablauf

### Schritt 0 — ADR-Ordner vorbereiten

1. Prüfen, ob `docs/adr/` existiert; falls nicht, anlegen.
2. Vorhandene ADRs auflisten (`glob: docs/adr/*.md`), um die höchste Nummer zu
   bestimmen. Die nächste Nummer = höchste + 1 (Startwert `0001`, falls leer).
3. Existiert `docs/adr/README.md` nicht, anlegen (siehe „Index" unten).

### Schritt 1 — Erste Fragerunde (alle Fragen in einem `question`-Aufruf)

Drei voneinander unabhängige Fragen in einem einzigen Aufruf des `question`-Tools
platzieren (`multiple: false`, außer bei Mehrfachauswahl):

1. **Status**
   - Optionen: `vorgeschlagen (proposed)`, `angenommen (accepted)`,
     `abgelehnt (rejected)`, `veraltet (deprecated)`
   - Empfohlene erste Option: `vorgeschlagen (proposed) (Empfohlen)`

2. **Betrachtete Optionen** (`multiple: true`)
   - 3–5 plausible Alternativen aus Kontext, Codebase und vorheriger Konversation
     vorschlagen (z. B. Framework-, Datenbank-, Architektur-, Bibliotheksauswahl),
     jede mit kurzer Beschreibung im `description`-Feld.
   - `custom` liefert automatisch „Type your own answer" für weitere Optionen —
     das zusätzliche „Other"-Angebot nicht selbst hinzufügen.
   - Mindestens 2 Optionen vorschlagen.

3. **Entscheidungstreiber** (`multiple: true`)
   - Qualitätsziele, Constraints oder Anforderungen aus dem Projekt ableiten
     (z. B. „Wartbarkeit", „geringe Einführungszeit", „kein zusätzlicher
     Deploy-Schritt", „Team-Kenntnisse").
   - Mindestens 2, maximal 5 Treiber anbieten.

### Schritt 2 — Entscheidung (zweiter `question`-Aufruf)

4. **Gewählte Option**
   - Exakt die Titel aus Frage 2 als Optionen anbieten, in ihrer ursprünglichen
     Reihenfolge. Erste Option als `(Empfohlen)` markieren, wenn aus dem Kontext
     eine klar bevorzugte hervorgeht — sonst keine Empfehlung.

5. **Kurzbegründung der Wahl** (Freitext, `custom` nutzt „Type your own answer")
   - Optionen anbieten wie `Erfüllt alle Entscheidungstreiber`, `Geringster
     Aufwand`, `Beste Skalierbarkeit`, `Team-Erfahrung` — die Begründung wird
     später als „weil …" übernommen.

6. **Konsequenzen** (`multiple: true`)
   - Jeweils 2–3 positive und 2–3 negative Folgen aus Option und Kontext als
     Vorschlag anbieten, markiert mit `(+)` bzw. `(-)`.
   - Der Nutzer kreuzt an, was zutrifft, und kann eigene ergänzen.

Falls der Status `ersetzt durch …` gewählt wird, zusätzlich nach der ersetzten
ADR-Nummer fragen.

### Schritt 3 — Entwurf vorlegen

Vor dem Schreiben den vollständigen ADR-Entwurf als Markdown im Chat zeigen
(kopierbar, in einem Code-Block) und mit **einem** `question`-Aufruf freigeben:

- `Speichern (Empfohlen)`
- `Entwurf bearbeiten` → der Nutzer nennt die Änderungen (Freitext)
- `Abbrechen`

Erst nach `Speichern` die Datei anlegen.

### Schritt 4 — Datei schreiben

1. Datei `docs/adr/NNNN-kurztitel.md` erstellen.
2. Den Index `docs/adr/README.md` um eine Zeile ergänzen.
3. Dem Nutzer den finalen Pfad nennen und darauf hinweisen, dass die Änderung
   erst nach einem Neustart von opencode bzw. beim nächsten Commit wirksam ist —
   ADRs gehören wie der Code ins Repository.

## Vorlage (MADR 4.0, Deutsch)

```markdown
---
status: "vorgeschlagen | angenommen | abgelehnt | veraltet | ersetzt durch ADR-NNNN"
date: "YYYY-MM-DD"
decision-makers: "Namen der Beteiligten"
consulted: "Namen der Befragten"
informed: "Namen der Informierten"
---

# Kurztitel der Entscheidung

## Kontext und Problembeschreibung

{2–4 Sätze: Welches Problem motiviert die Entscheidung? Scope explizit benennen.
Ggf. Frage formulieren oder Links zu Issues/Boards ergänzen.}

## Entscheidungstreiber

* {Treiber 1, z. B. Qualitätsziel, Constraint, Anforderung}
* {Treiber 2}

## Betrachtete Optionen

* {Option 1}
* {Option 2}
* {Option 3}

## Entscheidungsergebnis

Gewählte Option: "{Option X}", weil {Begründung laut Frage 5}.

### Konsequenzen

* Gut, weil {positive Folge}
* Schlecht, weil {negative Folge}

### Bestätigung

{Wie sichergestellt wird, dass die Entscheidung umgesetzt ist — z. B. Architektur-Review, Test, Lint-Regel, manuelle Checkliste.}

## Vor- und Nachteile der Optionen

### {Option 1}

* Gut, weil {Argument}
* Neutral, weil {Argument}
* Schlecht, weil {Argument}

### {Option 2}

* Gut, weil {Argument}
* Schlecht, weil {Argument}

## Weitere Informationen

{Verwandte ADRs, Links, Zeitpunkt der erneuten Überprüfung.}
```

Pflichtabschnitte sind: Kontext und Problembeschreibung, Betrachtete Optionen,
Entscheidungsergebnis. Alle weiteren Abschnitte entfernen, wenn sie leer blieben —
aber nie ein Pflichtfeld le lassen.

## Index `docs/adr/README.md`

```markdown
# Architecture Decision Records

| Nr. | Titel | Status | Datum | Datei |
| --- | --- | --- | --- | --- |
| 0001 | Kurztitel | vorgeschlagen | 2026-10-07 | [0001-kurztitel.md](0001-kurztitel.md) |
```

Neue Zeilen unten anhängen, bestehende Zeile bei Statuswechsel aktualisieren.

## Beispielausgabe

```markdown
---
status: "angenommen"
date: "2026-10-07"
decision-makers: "Max Mustermann"
---

# PostgreSQL statt MongoDB als Primärdatenbank

## Kontext und Problembeschreibung

Die Anwendung benötigt relationsartige Daten mit Transaktionen zwischen
Bestellung und Bestand. Welche Datenbank erfüllt das ohne Zusatzaufwand?

## Entscheidungstreiber

* Datenintegrität durch Transaktionen
* Team hat Erfahrung mit SQL

## Betrachtete Optionen

* PostgreSQL
* MongoDB
* MySQL

## Entscheidungsergebnis

Gewählte Option: "PostgreSQL", weil es alle Entscheidungstreiber abdeckt und
JSONB für seltene NoSQL-Fälle mitbringt.

### Konsequenzen

* Gut, weil Transaktionen und Joins nativ unterstützt sind.
* Schlecht, weil ein weiteres Container-Image ins Compose-Setup kommt.

### Bestätigung

Schema-Migrationen laufen über Flyway; ein Architektur-Review prüft die
Datenmodell-Änderungen.

## Vor- und Nachteile der Optionen

### PostgreSQL

* Gut, weil volle SQL-Unterstützung inkl. JSONB.
* Neutral, weil das Team MySQL kennt, die Umstellung aber gering ist.

### MongoDB

* Gut, weil flexible Schemata.
* Schlecht, weil Transaktionen und Joins umständlich sind.
```
