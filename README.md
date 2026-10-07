# Angular + Spring Boot, Hello World

A single-page Angular app that renders a greeting fetched from a Spring Boot REST
API. Everything runs with one `docker-compose up`.

- **Frontend** — Angular 22, built to static files and served by nginx
- **Backend** — Spring Boot 4.1.1 on Java 21, exposing `GET /api/hello`
- **Database** — none

## Quick start

Requires Docker with the Compose plugin (or the standalone `docker-compose`).

```bash
docker-compose up -d --build
```

Then open <http://localhost>.

You should see:

> **Angular & Spring Boot**
> **Hello, World!**
> *Served by the backend at Oct 1, 2026, 11:10:49 AM*

To check the API on its own — note the relative path, it is served through nginx:

```bash
curl http://localhost/api/hello
# {"message":"Hello, World!","servedAt":"2026-10-01T09:10:18.661325702Z"}
```

Stop everything with:

```bash
docker-compose down
```

## How a request flows

```
browser ──▶ frontend (nginx :80) ──▶ serves index.html + hashed JS/CSS
                     │
                     └──▶ /api/hello ──▶ backend (Spring Boot :8080)
```

The app requests the **relative** path `/api/hello`. nginx forwards anything under
`/api/` to the backend container on the Compose network, so:

- the browser only ever talks to one origin, so **no CORS configuration exists anywhere**;
- the backend's port 8080 is never published to the host;
- nothing in the frontend knows where the backend lives.

`backend` is deliberately not exposed. To call it directly while debugging, use
`docker-compose exec frontend wget -qO- http://backend:8080/api/hello`.

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

The backend waits for a healthy response from `/api/hello` before nginx starts, so
there are no startup races.

### Tests

Both suites run inside the normal builds, so `docker-compose up --build` already
executes them. To run them on the host:

```bash
cd backend  && ./mvnw test        # 2 tests, needs a JDK 21
cd frontend && npm install && npx ng test
```

### Local development

The containers are built the production way, so there is no hot reload. For live
reload, run each side on the host instead:

```bash
cd backend  && ./mvnw spring-boot:run      # http://localhost:8080
cd frontend && npm start                    # http://localhost:4200
```

For `ng serve` to reach the API you need a dev proxy, since the app calls the
relative `/api/hello`. Add a `proxy.conf.json`:

```json
{ "/api": { "target": "http://localhost:8080", "secure": false } }
```

and start it with `npx ng serve --proxy-config proxy.conf.json`.

## Corporate TLS interception

Some corporate networks re-sign HTTPS in transit. Maven Central then arrives with
a certificate signed by your organisation's proxy CA, and the Maven build fails
with:

```
PKIX path building failed: unable to find valid certification path to requested target
```

The backend `Dockerfile` imports any `.crt` / `.pem` files it finds in
`backend/docker/certs/` into the JVM truststore before Maven runs. Export your
organisation's root CA into that directory and it just works:

```bash
# example, from the Windows certificate store
Get-ChildItem Cert:\CurrentUser\Root | Where-Object { $_.Subject -match 'Zscaler' } | ForEach-Object {
    [IO.File]::WriteAllText("backend/docker/certs/$($_.Thumbprint.Substring(0,8)).pem",
        "-----BEGIN CERTIFICATE-----`n" +
        [Convert]::ToBase64String($_.RawData, 'InsertLineBreaks') +
        "`n-----END CERTIFICATE-----`n")
}
```

`docker/certs/` is committed empty and the `*.crt` / `*.pem` files are gitignored,
so on an unproxied network the step is a no-op and Maven Central is used directly
with full certificate verification. A corporate root CA should not be committed.

The frontend needs no equivalent: the npm registry is not intercepted, so `npm ci`
works as-is.