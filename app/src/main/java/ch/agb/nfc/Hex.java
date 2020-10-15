package ch.agb.nfc;

public class Hex {
    private static final char[] HEXCHARS = "0123456789ABCDEF".toCharArray();
    
    public static byte[] toByteArray(String s) {
        if (s != null) {
            final int len = s.length();
            final byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                        .digit(s.charAt(i + 1), 16));
            }
            return data;
        }
        else {
            return null;
        }
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEXCHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEXCHARS[v & 0x0F];
        }
        return new String(hexChars);
    }
}
