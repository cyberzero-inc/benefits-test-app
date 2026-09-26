package com.quantanaut.benefits;

import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Outbound connection to the benefits provider.
 *
 * The protocol and the cipher suites are named here rather than left to the
 * platform default, so the transport this service actually negotiates can be
 * read from the source instead of inferred from the runtime.
 */
@Component
public class ProviderClient {

    /** Suites offered to the provider, in preference order. */
    private static final String[] CIPHER_SUITES = {
        "TLS_AES_256_GCM_SHA384",
        "TLS_AES_128_GCM_SHA256",
    };

    /**
     * Opens the provider connection.
     *
     * TLS 1.3 with a classical X25519 group. The hybrid group is not offered
     * on this release; see R-1 in docs/PQC-ROADMAP.md.
     */
    public SSLSocket connect(String host, int port) throws Exception {
        SSLContext context = SSLContext.getInstance("TLSv1.3");
        context.init(null, null, null);

        SSLSocketFactory factory = context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setEnabledProtocols(new String[] { "TLSv1.3" });
        socket.setEnabledCipherSuites(CIPHER_SUITES);
        return socket;
    }
}
