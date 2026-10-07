# CLAUDE.md

Project guidance for AI agents. opencode reaches this file through the pointer
in `AGENTS.md`; Claude Code reads it directly.

## What this is

A teaching/demo stack: an Angular single-page app that renders a greeting
fetched from a Spring Boot REST API, wired together by `docker-compose`.

| | |
| --- | --- |
| Frontend | Angular 22 → static files, served by nginx:80 |
| Backend | Spring Boot 4.1.1, Java 21, `GET /api/hello` |
| Database | none |
| Tests | JUnit (2 tests) + Angular/Vitest |

## Architecture

```
browser ──▶ nginx :80 ──▶ index.html + hashed JS/CSS
                │
                └──▶ /api/* ──▶ backend:8080 (Compose network only)
```

- The frontend always requests the **relative** path `/api/hello`. Never hard-code
  a host, port or absolute URL into the Angular app — that is what keeps CORS
  out of the project entirely.
- `backend` is **not published** to the host in `docker-compose.yml`. That is
  deliberate. To hit it directly:
  `docker-compose exec frontend wget -qO- http://backend:8080/api/hello`.
- The backend container exposes a health answer on `/api/hello`; nginx waits for
  it before starting. There are no startup races — do not add `depends_on`
  sleeps or restart loops to "fix" something that is not broken.
- Port 8080 only exists on the host when you run the backend with `./mvnw
  spring-boot:run` for local development.

## Conventions

**Backend**

- `Greeting` is a Java `record(message, servedAt)` — no getters, no Lombok.
- One controller, one endpoint. Keep new endpoints in their own `@RestController`.
- Time is returned as an ISO-8601 UTC string; the frontend formats it for display.

**Frontend**

- Components are standalone, single-file style: `app.ts` / `app.html` / `app.css`.
- All HTTP goes through a service (`greeting.service.ts`); components never call
  `HttpClient` directly.
- The response shape lives in its own type file (`greeting.ts`) shared by both.
- Formatting: Prettier (`frontend/.prettierrc`). There is **no linter** — run
  `npx prettier --write .` inside `frontend/` after editing.
- Tests use Vitest through `ng test` (jsdom, no browser needed).

**Docker**

- Images are built the production way. There is **no hot reload** in containers;
  live development means running the side you are editing on the host.
- Do not add a database service. The project intentionally has none.

## Gotchas

- **Corporate TLS interception.** Maven Central may arrive re-signed by a proxy
  CA and the build fails with `PKIX path building failed`. Put the root CA into
  `backend/docker/certs/` as `.pem`; the Dockerfile imports it into the JVM
  truststore before Maven runs. Those files are gitignored — never commit a
  corporate root CA. The npm registry is not intercepted, so the frontend is
  unaffected.
- **Dev proxy.** `ng serve` needs `proxy.conf.json`
  (`{ "/api": { "target": "http://localhost:8080", "secure": false } }`) because
  the app calls the relative `/api/hello`. The containers need no proxy — nginx
  does that job.
- **nginx config is load-bearing.** `frontend/nginx.conf` hosts the SPA *and*
  proxies `/api/`. Changing one without the other breaks the app.

## Architecture Decision Records

Significant choices (stack, proxy strategy, test runner, …) are recorded as ADRs
in `docs/adr/` in the MADR 4.0 format, file name `NNNN-kurztitel.md`.

Use the project skill for this — ask for "ein ADR erstellen". It walks through
the decision with structured options and writes the record plus the index in
`docs/adr/README.md`.

Rules:

- one decision per file; `NNNN` is four digits, assigned in sequence;
- ADRs are immutable once `angenommen` — supersede with a new record instead of
  editing an old one;
- ADRs are committed together with the code they describe.
