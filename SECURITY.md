# Security Policy — Benefits Test Application

## Release signing

Every release artifact is signed with the Cyberzero release key. The public key
and its rotation history are published at [`docs/signing-key.asc`](docs/signing-key.asc).

| | |
|---|---|
| Key type | RSA-4096 |
| Digest | SHA-512 |
| Published | `docs/signing-key.asc` |
| Signature file | `benefits-test-app-<version>.jar.asc`, beside each release |

### Verifying a release

```sh
gpg --import docs/signing-key.asc
gpg --verify benefits-test-app-1.0.0.jar.asc benefits-test-app-1.0.0.jar
```

A signature made by a key that is no longer published should be treated as a
failure, not a warning. Rotation is announced in the release notes before the
first artifact signed with the new key.

## Cryptographic posture

The mechanisms this service uses are named at their call sites in
`EnrolmentCrypto` and `ProviderClient` rather than resolved from configuration
strings, so the source is the record of what is used.

Planned changes, with mechanisms and dates, are in
[`docs/PQC-ROADMAP.md`](docs/PQC-ROADMAP.md).

## Reporting

Report vulnerabilities to security@cyberzero.io. Do not open a public issue.
