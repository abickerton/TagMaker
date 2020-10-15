package ch.agb.nfc.tags;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Ndef {
    public static NdefRecord createSmartPosterRecord(String text,
                                                           String[] uri, byte[] type) {
        final NdefRecord[] records = new NdefRecord[1 + uri.length];
        records[0] = createTextRecord(text);
        for (int i = 1; i < records.length; i++) {
            records[i] = createNdefRecord(uri[i - 1], type[i - 1]);
        }
        final NdefMessage nm = new NdefMessage(records);
        final NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_SMART_POSTER, new byte[0], nm.toByteArray());
        return record;
    }

    public static NdefRecord createSmartPosterRecord(String text,
                                                         String[] uri) {
        final NdefRecord[] records = new NdefRecord[1 + uri.length];
        records[0] = createTextRecord(text);
        for (int i = 1; i < records.length; i++) {
            records[i] = createTelRecord(uri[i - 1]);
        }
        final NdefMessage nm = new NdefMessage(records);
        final NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_SMART_POSTER, new byte[0], nm.toByteArray());
        return record;
    }

    public static NdefRecord createTelRecord(String phone) {
        return createNdefRecord(phone, (byte) 0x05);
    }

    public static NdefRecord createNdefRecord(String content, byte idCode) {
        final byte[] uriField = content.getBytes(Charset.forName("UTF-8"));
        final byte[] payload = new byte[uriField.length + 1];
        payload[0] = idCode;
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
                new byte[0], payload);
    }

    public static NdefRecord createTextRecord(String text) {
        final String lang = "en";
        final byte[] textBytes = text.getBytes();
        byte[] langBytes = null;
        try {
            langBytes = lang.getBytes("UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            Log.e("NFC-CREATE", "Unable to create NDEF record",e );
        }
        final int langLength = langBytes.length;
        final int textLength = textBytes.length;
        final byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], payload);
    }

    public static NdefRecord createUriRecord(String uri) {
        return createNdefRecord(uri, (byte) 0x00);
    }

    public static NdefMessage createVCard(String name, String phone,
                                        String email, String web) {
        String vcardcontent = "BEGIN:VCARD\r\nVERSION:2.1\r\nN:" + name
                + "\r\n";

        if (phone.length() > 3) {
            vcardcontent = vcardcontent + "TEL;CELL:" + phone + "\r\n";
        }

        if (email.length() > 6) {
            vcardcontent = vcardcontent + "EMAIL;INTERNET:" + email + "\r\n";
        }

        if (web.length() > 6) {
            vcardcontent = vcardcontent + "URL:" + web + "\r\n";
        }

        vcardcontent = vcardcontent + "END:VCARD\r\n";
        final NdefRecord ndef_records = NdefRecord.createMime("text/x-vCard",
                vcardcontent.getBytes());
        return new NdefMessage(ndef_records);
    }
}
