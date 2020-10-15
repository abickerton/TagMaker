package ch.agb.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;

public class NfcUtil {
    static public void nfc_disable(Context context, Activity activity) {
        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        adapter.disableForegroundDispatch(activity);
    }

    @SuppressWarnings("rawtypes")
    public static void nfc_enable(Context context, Activity activity, Class cls) {
        // Register for any NFC event (only while we're in the foreground)

        final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        final PendingIntent pending_intent = PendingIntent.getActivity(context,
                0, new Intent(context, cls)
        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        adapter.enableForegroundDispatch(activity, pending_intent, null, null);
    }

}
