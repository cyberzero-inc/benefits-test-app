package com.acme.benefits.util;

import java.io.OutputStream;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Outbound connection to the carrier clearing house.
 *
 * Created:  22 Apr 1999  R. Whitfield
 * Modified: 05 May 2009  M. Okamoto  - forced SSLv3 after the carrier upgrade
 * Modified: 17 Oct 2015  unknown     - added TLSv1 to the enabled list
 *
 * The carrier's load balancer still negotiates SSLv3. Removing it from the
 * enabled protocols breaks the nightly run; see BEN-2288.
 */
public class CarrierConnection {

    private static final String CARRIER_HOST = "clearing.carrier-example.net";
    private static final int CARRIER_PORT = 443;

    private static final String[] PROTOCOLS = { "SSLv3", "TLSv1" };

    private static final String[] CIPHERS = {
        "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
        "SSL_RSA_WITH_RC4_128_MD5",
        "SSL_RSA_WITH_RC4_128_SHA",
        "TLS_RSA_WITH_AES_128_CBC_SHA"
    };

    public SSLSocket open() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSLv3");
        ctx.init(null, null, null);

        SSLSocketFactory factory = ctx.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket(CARRIER_HOST, CARRIER_PORT);
        socket.setEnabledProtocols(PROTOCOLS);
        socket.setEnabledCipherSuites(CIPHERS);
        socket.startHandshake();
        return socket;
    }

    public void send(byte[] extract) throws Exception {
        SSLSocket socket = open();
        try {
            OutputStream out = socket.getOutputStream();
            out.write(extract);
            out.flush();
        } finally {
            socket.close();
        }
    }
}
