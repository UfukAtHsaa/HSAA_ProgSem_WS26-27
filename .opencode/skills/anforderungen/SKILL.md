---
name: anforderung
description: Legt eine Anforderung (User Story mit Akzeptanzkriterien) in docs/anforderungen/ des Projekts an. Verwenden, wenn der Nutzer eine Anforderung, User Story, Akzeptanzkriterien, ein Epic-Detail oder das Ergebnis aus dem Refinement dokumentieren will, oder Begriffe wie "Anforderung", "User Story", "Acceptance Criteria", "Akzeptanzkriterium", "Requirement" nennt.
---

# Anforderung dokumentieren (User Story + Akzeptanzkriterien)

Diese Anleitung führt schrittweise durch die Dokumentation einer Anforderung.
Immer auf Deutsch, immer in `docs/anforderungen/`, eine Datei pro Anforderung.

## Grundregeln

- **Eine Anforderung = eine User Story.** Nie zwei Stories in einer Datei.
- **Fragen stellen statt raten.** Jede offene Frage wird über das `question`-Tool
  mit konkreten Optionen gestellt. Was der Nutzer nicht beantworten will, wird
  nicht erfunden, sondern als `offen` markiert und im Entwurf zur Freigabe vorgelegt.
- **Akzeptanzkriterien sind prüfbar.** Jedes Kriterium lässt sich mit "erfüllt"
  oder "nicht erfüllt" beantworten. Formulierungen wie "schnell" oder
  "benutzerfreundlich" ohne Maßstab werden zurückgefragt.
- **Fehlerfälle gehören dazu.** Mindestens ein Kriterium beschreibt, was passiert,
  wenn der Nutzer etwas falsch macht oder etwas schiefgeht.
- **Die Quelle bleibt sichtbar.** Anforderungen aus dem Refinement-Protokoll werden
  als solche gekennzeichnet, eigene Ideen des Teams ebenfalls. Streichen von
  Vorgaben aus dem Protokoll ist nicht erlaubt, Ergänzen schon.
- **Dateiname:** `NNNN-kurztitel-mit-bindestrichen.md`, Kleinbuchstaben, `.md`.
  `NNNN` ist die nächste freie vierstellige Nummer (0001, 0002, …).

## Ablauf

### Schritt 0 — Ordner vorbereiten

1. Prüfen, ob `docs/anforderungen/` existiert; falls nicht, anlegen.
2. Vorhandene Dateien auflisten (`glob: docs/anforderungen/*.md`), um die höchste
   Nummer zu bestimmen. Nächste Nummer = höchste + 1 (Startwert `0001`).
3. Existiert `docs/anforderungen/README.md` nicht, anlegen (siehe "Index").

### Schritt 1 — Grobe Anforderung und Rahmen (erster `question`-Aufruf)

Zuerst klären, was die Dozenten vorgegeben haben und was das Team selbst weiß.

1. **Grobe Anforderung** (Freitext, `custom`)
   - Welche Vorgabe stammt aus Vision, Epic oder Refinement-Protokoll? Optionen aus
     vorhandenen Epics bzw. dem Protokoll im Repo vorschlagen, falls dort etwas liegt.
   - Zusätzlich fragen, ob es eine `Eigene Idee des Teams` ist (dann Quelle = Team).
2. **Zusatzinformationen aus dem Refinement** (`multiple: true`, optional)
   - Antworten der Dozenten, Beispiele, Abgrenzungen ("gehört nicht dazu").
3. **Rahmenbedingungen** (`multiple: true`)
   - Technische oder organisatorische Vorgaben, z. B. Technikwahl, Abgabetermin,
     Abhängigkeit von anderen Anforderungen.
4. **Qualitätsanforderungen** (`multiple: true`)
   - Vorschläge: `Bedienbarkeit`, `Fehlertoleranz`, `Antwortzeit`, `Barrierefreiheit`,
     `Wartbarkeit`. Jede gewählte Qualität mit einem prüfbaren Maßstab hinterlegen
     (z. B. "Ergebnisliste erscheint in unter 1 Sekunde"), sonst als `offen` markieren.

### Schritt 1b — UI/UX-Überlegung (zweiter `question`-Aufruf)

Bevor die Story formuliert wird: Wie soll es für den Nutzer aussehen und ablaufen?
Das Ergebnis fließt in Story und Kriterien ein. Eine Skizze ist hilfreich, aber
nicht Pflicht.

5. **Beteiligte Seiten oder Dialoge** (`multiple: true`)
   - Aus der groben Anforderung ableiten, z. B. Liste, Detailseite, Formular.
6. **Zentrale Bedienelemente** — Wo sind die wichtigsten Aktionen (Buttons,
   Eingabefelder), und was ist die Hauptaktion pro Seite?
7. **Ablauf** — Wie kommt der Nutzer von A nach B? Mit 2–3 Varianten, wo es
   echte Alternativen gibt (z. B. Seiten oder endloses Scrollen, Auswahlliste oder
   Suchfeld, Filter ja oder nein und warum).
8. **Wo macht der Nutzer Fehler?** — Mögliche Stolperstellen sammeln; sie werden
   in Schritt 3 zu Fehlerfällen.

Gibt es eine Alternative mit Tragweite für die Architektur, auf den Skill `adr`
hinweisen.

### Schritt 2 — Die Story (dritter `question`-Aufruf)

9. **Rolle** — Wer braucht das? Aus dem Projekt 2–4 plausible Rollen vorschlagen
   (z. B. `Kunde`, `Shop-Betreiber`, `Administrator`), `custom` für weitere.
10. **Ziel** (Freitext) — Was will die Rolle tun? Optionen anbieten, die aus dem
   Epic oder Protokoll ableitbar sind; Formulierung: "ich möchte …".
11. **Nutzen** (Freitext) — Wozu? Formulierung: "damit …". Ohne Nutzen keine Story.

### Schritt 3 — Akzeptanzkriterien (vierter `question`-Aufruf)

12. **Erwartetes Verhalten** (`multiple: true`)
   - 3–5 prüfbare Kriterien im Format *Gegeben … Wenn … Dann …* vorschlagen.
13. **Fehlerfälle** (`multiple: true`)
   - 2–3 typische Fehler vorschlagen: leere Pflichtfelder, ungültige Werte,
     doppelte Einträge, keine Treffer, Backend nicht erreichbar.
   - Pro Fehlerfall: Was sieht der Nutzer, und wie kommt er weiter?
14. **UX-Feinheiten über die Vorgabe hinaus** (`multiple: true`, optional)
   - Details, die nicht gefordert waren, aber den Ablauf verbessern (z. B.
     Rückmeldung nach dem Speichern, Zustand der Seite in der URL, Trefferzahl,
     Filter zurücksetzen). Diese Punkte stellt das Team im Sprint Review vor.
15. **Priorität** (MoSCoW)
    - Optionen: `Must (Empfohlen)`, `Should`, `Could`, `Won't (diesmal nicht)`
16. **Sprint**
    - Optionen: `Sprint 1`, `Sprint 2`, `Sprint 3`, `Sprint 4`, `Sprint 5`
17. **Verknüpfte Architekturentscheidung**
    - Optionen: vorhandene ADRs aus `docs/adr/` anbieten oder `keine`.
    - Gibt es eine nötige Entscheidung ohne ADR: auf den Skill `adr` hinweisen.

### Schritt 4 — Entwurf vorlegen

Vor dem Schreiben den vollständigen Entwurf als Markdown im Chat zeigen
(kopierbar, in einem Code-Block) und mit **einem** `question`-Aufruf freigeben:

- `Speichern (Empfohlen)`
- `Entwurf bearbeiten` → der Nutzer nennt die Änderungen (Freitext)
- `Abbrechen`

Erst nach `Speichern` die Datei anlegen.

### Schritt 5 — Datei schreiben

1. Datei `docs/anforderungen/NNNN-kurztitel.md` erstellen.
2. Den Index `docs/anforderungen/README.md` um eine Zeile ergänzen.
3. Dem Nutzer den Pfad nennen. Hinweis: Die Datei gehört wie der Code ins
   Repository und wird mit committet. Beim Sprint Review gilt der Stand des
   letzten Commits.

## Vorlage

```markdown
---
status: "offen | in Arbeit | erfüllt"
prioritaet: "Must | Should | Could | Won't"
sprint: "Sprint N"
quelle: "Refinement-Protokoll | Eigene Idee | Review-Feedback"
adr: "ADR-NNNN oder keine"
---

# Kurztitel der Anforderung

## Grobe Anforderung (Vorgabe)

{Wortlaut aus Vision, Epic oder Refinement-Protokoll bzw. "Eigene Idee des Teams".}

### Rahmenbedingungen und Qualität

* {Rahmenbedingung oder Qualitätsanforderung mit prüfbarem Maßstab}

## UI/UX-Überlegung

* **Seiten und Dialoge:** {…}
* **Hauptaktion und Ablauf:** {…}
* **Entscheidung und Begründung:** {z. B. Seiten statt endlosem Scrollen, weil …}
* **Mögliche Fehlerstellen:** {…}

## User Story

Als {Rolle} möchte ich {Ziel}, damit {Nutzen}.

## Akzeptanzkriterien

### Erwartetes Verhalten

* Gegeben {Ausgangslage}, wenn {Aktion}, dann {Ergebnis}.
* Gegeben …, wenn …, dann ….

### Fehlerfälle

* Gegeben {Ausgangslage}, wenn {falsche Eingabe oder Fehler}, dann {Meldung und Weg weiter}.

## UX-Feinheiten (über die Vorgabe hinaus)

* {Detail und kurze Begründung, warum es für den Nutzer besser ist}

## Offene Punkte

* {Nur, was noch nicht geklärt ist. Sonst Abschnitt entfernen.}
```

Pflichtabschnitte: Grobe Anforderung, User Story, Akzeptanzkriterien (inkl. mindestens ein Fehlerfall).
Leere weitere Abschnitte entfernen.

## Index `docs/anforderungen/README.md`

```markdown
# Anforderungen

| Nr. | Titel | Prio | Sprint | Status | Datei |
| --- | --- | --- | --- | --- | --- |
| 0001 | Kurztitel | Must | Sprint 1 | offen | [0001-kurztitel.md](0001-kurztitel.md) |
```

Neue Zeilen unten anhängen, bei Statuswechsel die bestehende Zeile aktualisieren.
