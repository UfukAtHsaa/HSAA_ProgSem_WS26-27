# syntax=docker/dockerfile:1

# ---------- build ----------
# The maven image ships its own Maven, so the wrapper is only needed when
# running ./mvnw on the host.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

# Some corporate networks re-sign TLS in transit, so the certificates Maven
# Central is served with no longer chain to a CA the container trusts and the
# build fails with a PKIX error. Drop your organisation's root CA (as .crt or
# .pem) into docker/certs/ and it gets imported into the JVM truststore for this
# build. The directory is committed empty, so on an unproxied network this is a
# no-op and Maven Central is used directly.
COPY docker/certs/ /tmp/extra-certs/
RUN set -eu; \
    for cert in /tmp/extra-certs/*.crt /tmp/extra-certs/*.pem; do \
      [ -f "$cert" ] || continue; \
      echo "trusting extra CA: $(basename "$cert")"; \
      keytool -importcert -cacerts -noprompt -storepass changeit \
        -alias "extra-$(basename "$cert")" -file "$cert"; \
    done; \
    rm -rf /tmp/extra-certs

# Resolve dependencies against the pom alone. This layer is cached until
# pom.xml actually changes, so editing Java sources never re-downloads anything.
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp clean package

# ---------- runtime ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring \
    && chown -R spring:spring /app
USER spring:spring

COPY --from=build /workspace/target/hello-*.jar /app/hello.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/hello.jar"]