# Extra trusted certificate authorities for the Maven build

Some corporate networks intercept TLS and re-sign traffic. When that happens,
Maven Central is served with a certificate that chains to your organisation's
proxy CA rather than a public CA, and the Maven build inside Docker fails with:

```
PKIX path building failed: unable to find valid certification path to requested target
```

If you hit that, export your organisation's root CA as PEM and drop it in this
directory. `../Dockerfile` imports every `.crt` and `.pem` here into the JVM
truststore before Maven runs.

**This directory is intentionally committed empty.** Anyone building this project
on a normal network gets stock Maven Central with full certificate verification
and needs to do nothing.

The `.crt` / `.pem` files themselves are gitignored, because a corporate root CA
is not something to commit to a shared repository.