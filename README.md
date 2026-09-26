# Benefits Test Application

A small benefits enrolment service, used as a reference workload for
cryptographic posture assessment.

It is deliberately ordinary: a Spring Boot service that seals records, agrees a
key with a provider, signs submissions and opens a TLS connection. The point is
that its cryptography is **legible** — every mechanism is named at its call
site, and the versions it depends on are pinned — so a scan of this repository
and a scan of its built artifact should agree.

## What is where

| Path | |
|---|---|
| `src/main/java/.../EnrolmentCrypto.java` | record sealing, key agreement, signing, digest |
| `src/main/java/.../ProviderClient.java` | TLS 1.3 client, named cipher suites |
| `src/main/resources/application.yml` | server TLS configuration |
| `pom.xml` | pinned dependencies, SCM coordinates |
| `docs/PQC-ROADMAP.md` | migration commitments with mechanisms and dates |
| `SECURITY.md` | release signing and verification |

## Known posture

- Record encryption is **AES-256-GCM** and needs no migration.
- Key agreement is **X25519**, classical, and is the harvest-now-decrypt-later
  exposure. Hybrid is committed for 1.2.0 (R-1).
- **Bouncy Castle is pinned to 1.77**, which predates the NIST-final parameter
  sets. Upgrading to 1.78+ is committed for 1.1.0 (R-2) and is a drop-in change.
- Submission signing is **SHA256withRSA**, classical. ML-DSA-65 is committed
  for 2.0.0 (R-3), deliberately after key agreement.

## Build

```sh
mvn -q clean package
java -jar target/benefits-test-app-1.0.0.jar
```
