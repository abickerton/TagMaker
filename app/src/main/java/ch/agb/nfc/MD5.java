package ch.agb.nfc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static final String digest(final String s) {
        try {
            // Create MD5 Hash
            final MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            final byte messageDigest[] = digest.digest();

            // Create Hex String
            final StringBuffer hexString = new StringBuffer();
            for (final byte element : messageDigest) {
                String h = Integer.toHexString(0xFF & element);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
