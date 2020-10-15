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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ch.agb.nfc.Hex;
import ch.agb.nfc.NfcUtil;
import ch.agb.nfc.tags.Mifare;

public class MainActivity extends Activity {

    public void clone(View view) {
        final Intent intent = new Intent(getApplicationContext(),
                CloneReadActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //log.LOG_TAG = Config.LOG_TAG;
        //log.appDebug = Tools.CheckDebuggable(this);
        //log.fullDebug = false; // Tools.isTestDevice(this);
        final NfcAdapter Nfc = NfcAdapter.getDefaultAdapter(this);
        if (Nfc == null) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.CantFindNFCAdapter), Toast.LENGTH_LONG)
                    .show();
        }
        if (Nfc != null) {
            if (!Nfc.isEnabled()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.EnabeNFCFirst), Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }
        }
        int RunCount = getRunCount(this);
        if (RunCount == 4) {
            showDialog(1);
        }
        if (RunCount < 6) {
            RunCount = RunCount + 1;
            setRunCount(this, RunCount);
        }
        final SharedPreferences settings = getSharedPreferences(
                Config.PREFS_NAME, 0);
        Config.uri = settings.getString("uri", getString(R.string.defaultUri));
        Config.phone = settings.getString("phone",
                getString(R.string.defaultPhone));
        Config.name = settings.getString("name",
                getString(R.string.defaultName));
        Config.email = settings.getString("email",
                getString(R.string.defaultEmail));
        Config.web = settings.getString("web", getString(R.string.defaultWeb));

        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_SEND)
                && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Config.uri = intent.getStringExtra(Intent.EXTRA_TEXT);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString("uri", Config.uri);
            editor.apply();
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        Dialog dialog;
        AlertDialog.Builder builder;
        if (id == 1) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.rate_text))
                    .setTitle(getResources().getString(R.string.menu_item_rate))
                    .setCancelable(false)
                    .setPositiveButton(
                            getResources().getString(R.string.menu_item_rate),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Do something here
                                    final Intent GooglePlayIntent = new Intent(
                                            Intent.ACTION_VIEW,
                                            Config.URI_APP_LINK);
                                    startActivity(GooglePlayIntent);
                                }
                            })
                    .setNegativeButton(
                            getResources().getString(android.R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // Do something here
                                }
                            });

            dialog = builder.create();
        } else {
            dialog = null;
        }
        return dialog;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            final String typ_karty = Mifare.getType(tag);
            Toast.makeText(getApplicationContext(),
                    getString(R.string.card_type) + typ_karty,
                    Toast.LENGTH_LONG).show();
            final Ndef ndef = Ndef.get(tag);
            Toast.makeText(getApplicationContext(),
                    "ID:" + Hex.toHexString(tag.getId()), Toast.LENGTH_LONG).show();
            if (ndef != null) {
                Toast.makeText(getApplicationContext(),
                        "Type:" + ndef.getType() + "size:" + ndef.getMaxSize(),
                        Toast.LENGTH_LONG).show();
            }
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.ABOUT:
            final AboutDialog aboutDialog = new AboutDialog(this);
            aboutDialog.setIcon(R.drawable.ic_launcher);
            aboutDialog.show();
            break;
        case R.id.menu_item_rate:
            showDialog(1);
            return true;
        case R.id.settings:
            final Intent settings = new Intent(getApplicationContext(),
                    SettingsActivity.class);
            startActivity(settings);
            break;
        }
        return true;
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

    public void ultraLight(View view) {
        final Intent intent = new Intent(getApplicationContext(),
                UltralightHEXEDIT.class);
        startActivity(intent);
    }

    public void writePhone(View view) {
        if ((Config.phone.length() != 0) && (Config.name.length() != 0)) {
            final NdefRecord[] ndef_name = new NdefRecord[1];
            final String[] uri = new String[] { Config.phone };
            ndef_name[0] = ch.agb.nfc.tags.Ndef.createSmartPosterRecord(Config.name,
                    uri);
            Config.nfc_payload = new NdefMessage(ndef_name);
            final Intent intent = new Intent(getApplicationContext(),
                    WriteNFCActivity.class);
            startActivity(intent);
        }
        else {
            final Intent settings = new Intent(getApplicationContext(),
                    SettingsActivity.class);
            startActivity(settings);
        }
    }

    public void writeSmartPoster(View view) {
        final NdefRecord[] ndef_name = new NdefRecord[1];
        int ile = 0;
        int gdzie = 0;

        if (Config.phone.length() > 3) {
            ile = ile + 1;
        }

        if (Config.uri.length() > 3) {
            ile = ile + 1;
        }

        if (Config.email.length() > 6) {
            ile = ile + 1;
        }
        if (Config.web.length() > 6) {
            ile = ile + 1;
        }

        final String[] uri = new String[ile];
        final byte[] type = new byte[ile];

        if (Config.phone.length() > 3) {
            uri[gdzie] = Config.phone;
            type[gdzie] = 0x05;
            gdzie = gdzie + 1;
        }

        if (Config.uri.length() > 3) {
            uri[gdzie] = Config.uri;
            type[gdzie] = 0x00;
            gdzie = gdzie + 1;
        }

        if (Config.email.length() > 6) {
            uri[gdzie] = Config.email;
            type[gdzie] = 0x06;
            gdzie = gdzie + 1;
        }
        if (Config.web.length() > 6) {
            uri[gdzie] = Config.web;
            type[gdzie] = 0x00;
            gdzie = gdzie + 1;
        }
        ndef_name[0] = ch.agb.nfc.tags.Ndef.createSmartPosterRecord(Config.name, uri,
                type);
        Config.nfc_payload = new NdefMessage(ndef_name);
        final Intent intent = new Intent(getApplicationContext(),
                WriteNFCActivity.class);
        startActivity(intent);
    }

    public void writeUri(View view) {
        if (Config.uri.length() != 0) {
            final NdefRecord ndef_records = NdefRecord.createUri(Config.uri);
            Config.nfc_payload = new NdefMessage(ndef_records);
            final Intent intent = new Intent(getApplicationContext(),
                    WriteNFCActivity.class);
            startActivity(intent);
        }
        else {
            final Intent settings = new Intent(getApplicationContext(),
                    SettingsActivity.class);
            startActivity(settings);
        }
    }

    public void writeVcard(View view) {
        Config.nfc_payload = ch.agb.nfc.tags.Ndef.createVCard(Config.name, Config.phone,
                Config.email, Config.web);
        final Intent intent = new Intent(getApplicationContext(),
                WriteNFCActivity.class);
        startActivity(intent);
    }

    private static final String PREF_RUNCOUNT = "run_count"; //$NON-NLS-1$

    private static int getRunCount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREF_RUNCOUNT, 0);
        return prefs.getInt(PREF_RUNCOUNT, 0);
    }

    private static void setRunCount(Context context, int RunCount) {
        final SharedPreferences.Editor prefs = context.getSharedPreferences(
                PREF_RUNCOUNT, 0).edit();
        prefs.putInt(PREF_RUNCOUNT, RunCount);
        prefs.apply();
    }
}
