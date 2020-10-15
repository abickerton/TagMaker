package ch.agb.nfc.tags;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;

public class Mifare {
    public static String getType(Tag tag) {
        String type = "Unknown";
        for (final String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                final MifareClassic mifareTag = MifareClassic.get(tag);
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                final MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
            }
        }
        return type;
    }
}
