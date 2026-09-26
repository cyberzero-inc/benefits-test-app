# Post-Quantum Cryptography Roadmap — Benefits Test Application

**Publisher:** Cyberzero (vendor of this application)
**Published:** 2026-09-26
**Applies to:** `com.quantanaut:benefits-test-app`, all 1.x releases
**Review cadence:** quarterly

This is a commitment, not a statement of intent. Each item names the mechanism,
the release that delivers it and the date it is due. An item with no named
mechanism or no date is not on this roadmap.

## Delivered

### R-0 — Authenticated encryption at rest · DELIVERED 1.0.0 (2026-09-26)
Benefits records are sealed with **AES-256-GCM**. Symmetric encryption at 256
bits is not broken by Grover's algorithm in any practical sense and is not part
of the post-quantum migration.

## Committed

### R-1 — Hybrid key agreement on the provider connection · TARGET 1.2.0 (2027-03-31)
Replace the classical **X25519** agreement on the provider connection with
**X25519MLKEM768**, the hybrid group specified in RFC 10024 for TLS 1.3.

The provider connection is the harvest-now-decrypt-later exposure in this
service: a recorded session can be decrypted once a cryptographically relevant
quantum computer exists. Hybrid keeps X25519 alongside ML-KEM-768, so a
weakness in either component does not break the handshake.

**Depends on R-2.** Blocked until the provider advertises the hybrid group.

### R-2 — Provider upgrade to NIST-final parameter sets · TARGET 1.1.0 (2026-12-31)
Upgrade **Bouncy Castle from 1.77 to 1.78 or later**.

1.77 predates the NIST-final parameter sets; ML-KEM on that release is the
pre-standard form. 1.78 is the first release carrying the final sets. The
vendor documents this as a drop-in upgrade with no API change.

This is a single version bump in `pom.xml` and is the prerequisite for R-1.

### R-3 — Post-quantum submission signatures · TARGET 2.0.0 (2027-09-30)
Move enrolment submission signing from **SHA256withRSA** to **ML-DSA-65**.

Deferred behind R-1 deliberately. Signatures protect against forgery at the
time of use, so a signature made today is not retrospectively broken by a
future quantum computer the way a recorded key exchange is. Key agreement moves
first because its exposure is retrospective.

Requires the benefits provider to accept ML-DSA submissions; not yet scheduled
on their side.

## Not committed

**Certificate chain.** The service presents a certificate issued by a public
CA. Post-quantum certificates are not available from any public CA on the
timeline above, so no date is given. Tracked, not committed.
