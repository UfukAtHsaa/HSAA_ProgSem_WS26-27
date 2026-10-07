# syntax=docker/dockerfile:1

# ---------- build ----------
FROM node:26-alpine AS build

WORKDIR /app

# Install from the lockfile only, so this layer survives source-only changes.
# Note: node_modules is left out of the build context, so `npm ci` is the only
# way dependencies are ever installed.
COPY package.json package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci

COPY . .
RUN --mount=type=cache,target=/root/.npm npm run build

# ---------- runtime ----------
FROM nginx:1.31-alpine AS runtime

# Angular's application builder writes the browser bundle to dist/<app>/browser.
COPY --from=build /app/dist/frontend/browser/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80