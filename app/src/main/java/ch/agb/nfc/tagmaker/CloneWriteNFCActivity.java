/*
  Copyright (C) 2014 Mateusz Szafraniec
  This file is part of NFCTagMaker.

  NFCTagMaker is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  NFCTagMaker is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with NFCTagMaker; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

  Ten plik jest częścią NFCTagMaker.

  NFCTagMaker jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
  i/lub modyfikować na warunkach Powszechnej Licencji Publicznej GNU,
  wydanej przez Fundację Wolnego Oprogramowania - według wersji 2 tej
  Licencji lub (według twojego wyboru) którejś z późniejszych wersji.

  Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
  użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
  gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
  ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
  Powszechnej Licencji Publicznej GNU.

  Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
  Powszechnej Licencji Publicznej GNU (GNU General Public License);
  jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
  Place, Fifth Floor, Boston, MA  02110-1301  USA
 */
package ch.agb.nfc.tagmaker;

import ch.agb.nfc.NfcUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CloneWriteNFCActivity extends Activity {

    @Override
    protected void onCreate(Bundle sis) {
        super.onCreate(sis);
        setContentView(R.layout.activity_write_nfc);
        final TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText(getString(R.string.PlaceCloneTag));
        setResult(0);
        final Button b = (Button) findViewById(R.id.cancel_nfc_write_button);
        b.setOnClickListener(self -> {
            NfcUtil.nfc_disable(getApplicationContext(),
                    CloneWriteNFCActivity.this);
            finish();
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            int success = 0;
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                try {
                    ndef.connect();
                    ndef.writeNdefMessage(Config.nfc_payload);
                    ndef.close();
                    success = 1;
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Exception: CloneWrite" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                final NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(Config.nfc_payload);
                        format.close();
                        success = 1;
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                        //log.e("Exception: CloneWrite" + e.toString());
                        Toast.makeText(getApplicationContext(),
                                "Exception: CloneWrite" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }

            if (success == 1) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.Success), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.Failed), Toast.LENGTH_SHORT).show();
            }
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcUtil.nfc_disable(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcUtil.nfc_enable(this, this, getClass());
    }
}
