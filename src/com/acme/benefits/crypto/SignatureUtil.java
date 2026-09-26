package com.acme.benefits.crypto;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import sun.misc.BASE64Encoder;

/**
 * Signs the nightly carrier extract.
 *
 * Created: 09 Sep 1999  R. Whitfield
 *
 * The carrier will not accept anything but SHA1withRSA. We raised this with
 * them in 2011 and again in 2018; the ticket is still open on their side.
 */
public class SignatureUtil {

    private static final String KEYSTORE_PATH = "/opt/benefits/conf/benefits.jks";
    private static final String KEYSTORE_PASS = "changeit";
    private static final String KEY_ALIAS = "benefits-extract";

    public static String signExtract(byte[] extract) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream in = new FileInputStream(KEYSTORE_PATH);
        try {
            ks.load(in, KEYSTORE_PASS.toCharArray());
        } finally {
            in.close();
        }

        PrivateKey key = (PrivateKey) ks.getKey(KEY_ALIAS, KEYSTORE_PASS.toCharArray());

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(extract);
        return new BASE64Encoder().encode(sig.sign());
    }

    public static boolean verifyExtract(byte[] extract, byte[] signature) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream in = new FileInputStream(KEYSTORE_PATH);
        try {
            ks.load(in, KEYSTORE_PASS.toCharArray());
        } finally {
            in.close();
        }

        Certificate cert = ks.getCertificate(KEY_ALIAS);
        PublicKey pub = cert.getPublicKey();

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(pub);
        sig.update(extract);
        return sig.verify(signature);
    }

    /** Legacy DSA path. Kept for the 1999 archive extracts. */
    public static boolean verifyArchive(byte[] data, byte[] signature, PublicKey pub) throws Exception {
        Signature sig = Signature.getInstance("SHA1withDSA");
        sig.initVerify(pub);
        sig.update(data);
        return sig.verify(signature);
    }
}
