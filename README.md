# Angular + Spring Boot, Hello World

A single-page Angular app that renders a greeting fetched from a Spring Boot REST
API. One command brings the whole stack up.

- **Frontend** — Angular 22, built to static files and served by nginx
- **Backend** — Spring Boot 4.1.1 on Java 21, exposing `GET /api/hello` and the broker example (see below)
- **Database** — PostgreSQL 17 in a container, data in the named volume `db-data`

## Quick start

Requires Docker with the Compose plugin (or the standalone `docker-compose`).

```bash
cp .env.example .env          # once: local passwords, gitignored
docker-compose up -d --build
```

Open <http://localhost> — you should see the greeting with its timestamp.

```bash
curl http://localhost/api/hello    # relative path, proxied by nginx
docker-compose down                # stop everything
```

## How a request flows

```
browser ──▶ frontend (nginx :80) ──▶ serves index.html + hashed JS/CSS
                     │
                     └──▶ /api/hello ──▶ backend (Spring Boot :8080)
```

The app calls the **relative** path `/api/hello`; nginx forwards anything under
`/api/` to the backend on the Compose network. That buys three things:

- the browser only talks to one origin, so **no CORS configuration exists anywhere**;
- the backend's port 8080 is never published to the host;
- nothing in the frontend knows where the backend lives.

To reach the backend directly while debugging:
`docker-compose exec frontend wget -qO- http://backend:8080/api/hello`.

The backend must answer `/api/hello` healthily before nginx starts, so there are
no startup races.

## Layout

```
docker-compose.yml
backend/                  Spring Boot API
  Dockerfile              maven:3.9-eclipse-temurin-21 → eclipse-temurin:21-jre-alpine
  docker/certs/           optional extra CAs for the build (see below)
  src/main/java/com/example/hello/
    Greeting.java         record(message, servedAt)
    GreetingController.java
frontend/                 Angular app
  Dockerfile              node:26-alpine → nginx:1.31-alpine
  nginx.conf              static hosting + /api reverse proxy
  src/app/
    app.ts/.html/.css     the page
    greeting.service.ts   the HTTP call
    greeting.ts           the response shape
```

## Everyday commands

| Command | What it does |
| --- | --- |
| `docker-compose up -d --build` | Build images and start both containers |
| `docker-compose logs -f` | Follow logs from both |
| `docker-compose ps` | Container status, including backend health |
| `docker-compose down` | Stop and remove containers |
| `docker-compose build --no-cache backend` | Rebuild one image from scratch |

### Tests

Both suites run inside the normal builds, so `docker-compose up --build` already
executes them. To run them on the host:

```bash
cd backend  && ./mvnw test              # 11 tests (H2 in memory), needs a JDK 21
cd frontend && npm install && npx ng test
```

### Local development

The containers are built the production way, so there is no hot reload. For live
reload, run each side on the host:

```bash
cd backend  && ./mvnw spring-boot:run    # http://localhost:8080, needs DB_URL, DB_PASSWORD, ADMIN_PASSWORD, KUNDE_PASSWORD
cd frontend && npm start                 # http://localhost:4200
```

`ng serve` needs a dev proxy, since the app calls the relative `/api/hello`:

```json
{ "/api": { "target": "http://localhost:8080", "secure": false } }
```

Start it with `npx ng serve --proxy-config proxy.conf.json`.

## Beispiel aus Vorlesung 3: Broker

Ein kleiner Wertpapier-Broker zeigt Spring Web, Spring Data und Spring Security.
Bewusst kein Shop: Das Muster übertragt ihr selbst auf euer Projekt.

| Folie | Datei |
| --- | --- |
| Der Weg eines Requests | `OrderController` → `OrderService` → `OrderRepository` → Postgres |
| Dependency Injection | Konstruktoren in `OrderService`, `OrderController` (kein `new`) |
| Controller | `broker/OrderController.java`, `broker/AdminOrderController.java` |
| Entity und DTO | `broker/Order.java`, `broker/OrderDto.java`, `broker/CreateOrderRequest.java` |
| Repository und Pageable | `broker/OrderRepository.java`, `broker/PageResponse.java` |
| Fehlerformat (404, 400) | `broker/NotFoundException.java`, `broker/ApiExceptionHandler.java` |
| Security-Filter | `security/SecurityConfig.java` |
| Benutzer und Passwort-Hash | `security/AppUser.java`, `security/DbUserDetailsService.java` |
| Beispieldaten | `broker/DemoData.java` (Passwörter aus `.env`) |
| Drei Aufrufe beweisen die Sperre | `http/broker.http`, `BrokerApiTests.java` |
| Test ohne Spring | `OrderServiceTests.java` (Mocks über den Konstruktor) |
| ADRs und Anforderungen | `docs/adr/`, `docs/anforderungen/` |

Java-Dateien liegen unter `backend/src/main/java/com/example/hello/`.

Zugänge nach dem Start (Passwörter aus eurer `.env`):

| Rolle | E-Mail |
| --- | --- |
| Admin | `admin@broker.local` |
| Kundin | `anna@example.org` |

Die Beispieldaten werden nur beim ersten Start angelegt. Wer die Passwörter in
der `.env` ändert, muss die Datenbank neu anlegen: `docker-compose down -v`.

Für `http/broker.http`: `http/http-client.private.env.json.example` nach
`http/http-client.private.env.json` kopieren und die Passwörter aus der `.env`
eintragen.

## Corporate TLS interception

If your network re-signs HTTPS, Maven Central arrives with your proxy CA's
certificate and the backend build fails with `PKIX path building failed`. Export
your root CA into `backend/docker/certs/` as `.pem` — the `Dockerfile` imports
everything found there into the JVM truststore before Maven runs.

The directory is committed empty and `*.pem` is gitignored, so on an unproxied
network this is a no-op with full certificate verification. Never commit a
corporate root CA. The frontend needs no equivalent: npm is not intercepted.
