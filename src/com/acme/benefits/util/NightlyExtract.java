package com.acme.benefits.util;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Vector;

import com.acme.benefits.crypto.CryptoUtil;
import com.acme.benefits.crypto.SignatureUtil;

/**
 * Nightly carrier extract.
 *
 * Created:  22 Apr 1999  R. Whitfield
 *
 * Runs from cron at 02:15. Writes the extract, checksums it, signs it and
 * pushes it to the clearing house.
 */
public class NightlyExtract {

    private static final String OUT_DIR = "/opt/benefits/extract/";

    public void run(Vector records) throws Exception {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < records.size(); i++) {
            buf.append(records.elementAt(i).toString());
            buf.append("\n");
        }

        byte[] extract = buf.toString().getBytes();

        String checksum = CryptoUtil.fileChecksum(extract);
        String signature = SignatureUtil.signExtract(extract);

        String stamp = new Date().toString().replace(' ', '_');
        FileOutputStream out = new FileOutputStream(OUT_DIR + "extract_" + stamp + ".dat");
        try {
            out.write(extract);
        } finally {
            out.close();
        }

        System.out.println("extract checksum: " + checksum);
        System.out.println("extract signature: " + signature);

        new CarrierConnection().send(extract);
    }
}
