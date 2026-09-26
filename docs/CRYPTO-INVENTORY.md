# Cryptographic Inventory — Benefits Test Application 1.0.0

Declared by the vendor. Each entry names where the mechanism is used, so a
finding can be checked against the line that produced it.

| Mechanism | Use | Where | State | Post-quantum |
|---|---|---|---|---|
| AES-256-GCM | Record encryption at rest | `EnrolmentCrypto.sealRecord` | Default | Not applicable — symmetric |
| X25519 | Key agreement with provider | `EnrolmentCrypto.agreeRecordKey` | Default | **No** — classical, retrospective exposure |
| ML-KEM-768 | Key encapsulation | `EnrolmentCrypto.generateKemKeyPair` | Available, not default | Yes — pre-standard on BC 1.77 |
| SHA256withRSA | Submission signature | `EnrolmentCrypto.signSubmission` | Default | No — classical |
| SHA-256 | Audit log digest | `EnrolmentCrypto.auditDigest` | Default | Not applicable — hash |
| TLS 1.3 | Provider transport | `ProviderClient.connect` | Default | Protocol; group is classical |
| TLS_AES_256_GCM_SHA384 | Negotiated suite | `ProviderClient.CIPHER_SUITES` | Preferred | Not applicable — symmetric |

## Dependencies providing cryptography

| Library | Version | Role |
|---|---|---|
| `org.bouncycastle:bcprov-jdk18on` | 1.77 | Provider for every mechanism above |
| `org.bouncycastle:bcpkix-jdk18on` | 1.77 | Certificate and PKCS handling |
| `org.springframework.security:spring-security-crypto` | managed | Password encoding |

`ML-KEM` is reachable on 1.77 in its pre-standard form. The NIST-final
parameter sets arrive in 1.78; see R-2 in [`PQC-ROADMAP.md`](PQC-ROADMAP.md).
