package com.quantanaut.benefits;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;

/**
 * Cryptography used by enrolment.
 *
 * Every mechanism below is named at its call site rather than resolved from a
 * configuration string, so what this service uses can be read from the source.
 * Records at rest are sealed with AES-GCM; the record key is agreed with the
 * benefits provider; enrolment submissions are signed for non-repudiation.
 */
@Service
public class EnrolmentCrypto {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final SecureRandom random = new SecureRandom();

    /** Seals a benefits record. AES-256-GCM: authenticated, quantum-resistant at 256 bits. */
    public byte[] sealRecord(SecretKey recordKey, byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        byte[] nonce = new byte[12];
        random.nextBytes(nonce);
        cipher.init(Cipher.ENCRYPT_MODE, recordKey, new GCMParameterSpec(128, nonce));
        return cipher.doFinal(plaintext);
    }

    /**
     * Agrees the record key with the benefits provider.
     *
     * X25519 only. This is the classical key agreement the migration replaces:
     * a recorded session can be decrypted once a cryptographically relevant
     * quantum computer exists. Tracked as R-1 in docs/PQC-ROADMAP.md.
     */
    public KeyAgreement agreeRecordKey() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("X25519", "BC");
        KeyPair ephemeral = kpg.generateKeyPair();
        KeyAgreement agreement = KeyAgreement.getInstance("X25519", "BC");
        agreement.init(ephemeral.getPrivate());
        return agreement;
    }

    /**
     * Post-quantum key encapsulation, behind a flag.
     *
     * Available on this build and not yet the default: the provider pinned in
     * pom.xml is 1.77, which predates the NIST-final parameter sets. Tracked as
     * R-2 in docs/PQC-ROADMAP.md.
     */
    public KeyPair generateKemKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-KEM", "BC");
        return kpg.generateKeyPair();
    }

    /** Signs an enrolment submission. RSA-3072 with SHA-256; classical. */
    public byte[] signSubmission(java.security.PrivateKey key, byte[] submission) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(key);
        signature.update(submission);
        return signature.sign();
    }

    /** Integrity digest for the audit log. */
    public byte[] auditDigest(byte[] entry) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
        return digest.digest(entry);
    }
}
