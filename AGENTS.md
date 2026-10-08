# AGENTS.md

Project instructions for AI agents. opencode loads this file automatically.

## External file loading

**CRITICAL:** when you encounter a file reference such as `@CLAUDE.md`,
`@docs/...` or `@rules/...` anywhere in this repository, use the Read tool to
load it on a need-to-know basis.

Read **`CLAUDE.md`** early in the session — it holds the architecture, the
conventions and the operational gotchas that are not obvious from the code.

## Build and run

| Task | Command |
| --- | --- |
| Full stack | `docker-compose up -d --build` → <http://localhost> |
| Backend on host | `cd backend && ./mvnw spring-boot:run` → <http://localhost:8080> |
| Frontend on host | `cd frontend && npm start` → <http://localhost:4200> |
| Follow logs | `docker-compose logs -f` |
| Stop | `docker-compose down` |

## Verify before finishing

```bash
cd backend  && ./mvnw test                    # 2 tests, needs JDK 21
cd frontend && npm install && npx ng test     # vitest via `ng test`
```

`docker-compose up --build` already runs both suites — treat a successful
`docker-compose up -d --build` plus a green `docker-compose ps` as the final
check for a change that touches either container.

No linter is configured. `frontend/.prettierrc` exists; format with
`cd frontend && npx prettier --write .` when touching frontend files.

## Where things live

- `backend/src/main/java/com/example/hello/` — REST API (`Greeting`, `GreetingController`)
- `frontend/src/app/` — Angular components, `greeting.service.ts`
- `frontend/nginx.conf` — static hosting **and** the `/api` reverse proxy
- `docker-compose.yml` — service wiring; `backend` is deliberately not published
- `.opencode/skills/adr/` — ADR skill; it writes records into `docs/adr/`
- `docs/adr/` — Architecture Decision Records (create on first use)
- `.opencode/skills/anforderung/` — Skill `anforderung`; legt User Stories mit Akzeptanzkriterien an
- `docs/anforderungen/` — Anforderungen, eine Datei pro User Story (create on first use)

Architecture details, conventions and gotchas: see **`CLAUDE.md`**.
