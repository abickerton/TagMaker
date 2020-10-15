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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import ch.agb.nfc.Hex;
import ch.agb.nfc.NfcUtil;
import ch.agb.nfc.tags.Mifare;

public class UltralightHEXEDIT extends Activity {
    private boolean card_write;
    private static String errortext;
    private static final int PICK_FILE = 1;
    private static final int SAVE_NEW_FILE = 2;
    private static final File defaultFile = new File(
            Environment.getExternalStorageDirectory(), "nfctag.bin");

    public void exportTag(File file) throws IOException {
        final EditText et00 = findViewById(R.id.editText00);
        final EditText et01 = findViewById(R.id.editText01);
        final EditText et02 = findViewById(R.id.editText02);
        final EditText et03 = findViewById(R.id.editText03);
        final EditText et04 = findViewById(R.id.editText04);
        final EditText et05 = findViewById(R.id.editText05);
        final EditText et06 = findViewById(R.id.editText06);
        final EditText et07 = findViewById(R.id.editText07);
        final EditText et08 = findViewById(R.id.editText08);
        final EditText et09 = findViewById(R.id.editText09);
        final EditText et0A = findViewById(R.id.editText0A);
        final EditText et0B = findViewById(R.id.editText0B);
        final EditText et0C = findViewById(R.id.editText0C);
        final EditText et0D = findViewById(R.id.editText0D);
        final EditText et0E = findViewById(R.id.editText0E);
        final EditText et0F = findViewById(R.id.editText0F);

        if (!file.exists()) {
            if(!file.createNewFile()){
                throw new IOException("Could not create file");
            }
        }
        try ( FileOutputStream fos = new FileOutputStream(file) ){
            fos.write(Hex.toByteArray(et00.getText().toString()));
            fos.write(Hex.toByteArray(et01.getText().toString()));
            fos.write(Hex.toByteArray(et02.getText().toString()));
            fos.write(Hex.toByteArray(et03.getText().toString()));
            fos.write(Hex.toByteArray(et04.getText().toString()));
            fos.write(Hex.toByteArray(et05.getText().toString()));
            fos.write(Hex.toByteArray(et06.getText().toString()));
            fos.write(Hex.toByteArray(et07.getText().toString()));
            fos.write(Hex.toByteArray(et08.getText().toString()));
            fos.write(Hex.toByteArray(et09.getText().toString()));
            fos.write(Hex.toByteArray(et0A.getText().toString()));
            fos.write(Hex.toByteArray(et0B.getText().toString()));
            fos.write(Hex.toByteArray(et0C.getText().toString()));
            fos.write(Hex.toByteArray(et0D.getText().toString()));
            fos.write(Hex.toByteArray(et0E.getText().toString()));
            fos.write(Hex.toByteArray(et0F.getText().toString()));
            fos.flush();
            Toast.makeText(getApplicationContext(), getString(R.string.done),
                    Toast.LENGTH_SHORT).show();
        } catch (final IOException ex ) {
            Log.w("NDEF-WRITE", "exportTag: ", ex );
            Toast.makeText(getApplicationContext(),
                    "Can't write file" + ex, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void importTag(File file) {
        byte[] buffer = readFromFile(file);
        if( buffer == null ){
           return;
        }
        final EditText et00 = findViewById(R.id.editText00);
        et00.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 0, 4)));
        final EditText et01 = findViewById(R.id.editText01);
        et01.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 4, 8)));
        final EditText et02 = findViewById(R.id.editText02);
        et02.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 8, 12)));
        final EditText et03 = findViewById(R.id.editText03);
        et03.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 12, 16)));
        final EditText et04 = findViewById(R.id.editText04);
        et04.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 16, 20)));
        final EditText et05 = findViewById(R.id.editText05);
        et05.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 20, 24)));
        final EditText et06 = findViewById(R.id.editText06);
        et06.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 24, 28)));
        final EditText et07 = findViewById(R.id.editText07);
        et07.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 28, 32)));
        final EditText et08 = findViewById(R.id.editText08);
        et08.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 32, 36)));
        final EditText et09 = findViewById(R.id.editText09);
        et09.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 36, 40)));
        final EditText et0A = findViewById(R.id.editText0A);
        et0A.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 40, 44)));
        final EditText et0B = findViewById(R.id.editText0B);
        et0B.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 44, 48)));
        final EditText et0C = findViewById(R.id.editText0C);
        et0C.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 48, 52)));
        final EditText et0D = findViewById(R.id.editText0D);
        et0D.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 52, 56)));
        final EditText et0E = findViewById(R.id.editText0E);
        et0E.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 56, 60)));
        final EditText et0F = findViewById(R.id.editText0F);
        et0F.setText(Hex.toHexString(Arrays.copyOfRange(buffer, 60, 64)));
        Toast.makeText(getApplicationContext(), getString(R.string.done),Toast.LENGTH_SHORT).show();
    }

    private byte[] readFromFile( File file ){
        if( !file.exists() ){
            Toast.makeText(getApplicationContext(),getString(R.string.FileNotFound) + file.toString(),
                    Toast.LENGTH_LONG).show();
            return null;
        }
        try ( FileInputStream fis = new FileInputStream(file) ){
            final byte[] buffer = new byte[64];
            int read = fis.read(buffer, 0, 64);
            Log.d("IMPORT", "read FromFile: "+read);
            return buffer;
        } catch (final IOException ex) {
            Toast.makeText(getApplicationContext(),
                    "IOException" + ex + " " + file.toString(),
                    Toast.LENGTH_LONG).show();
            Log.e("IMPORT", "Unable to read file "+file.getAbsolutePath(), ex);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case PICK_FILE: {
            if (resultCode == RESULT_OK && data != null
                    && data.getData() != null) {
                final String theFilePath = data.getData().getPath();
                final File file = new File(theFilePath);
                importTag(file);
            }
        }
        break;
        case SAVE_NEW_FILE: {
            if (resultCode == RESULT_OK && data != null
                    && data.getData() != null) {
                final String theFilePath = data.getData().getPath();
                final File file = new File(theFilePath);
                try {
                    exportTag(file);
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultralight);
        final Button read = findViewById(R.id.read);
        read.setOnClickListener(self -> {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.readHEX), Toast.LENGTH_LONG).show();
            card_write = false;
        });
        final Button write = findViewById(R.id.write);
        write.setOnClickListener(self -> {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.writeHEX), Toast.LENGTH_LONG).show();
            card_write = true;
        });

        final Button export = findViewById(R.id.export_button);
        export.setOnClickListener(self -> {
            final Intent intent = new Intent(
                    "org.openintents.action.PICK_FILE");
            intent.putExtra(Intent.EXTRA_TITLE, "A Custom Title"); // optional
            try {
                startActivityForResult(intent, SAVE_NEW_FILE);
            }
            catch (final RuntimeException rr) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.OIFile), Toast.LENGTH_LONG)
                        .show();
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.SavingTo)
                        + defaultFile.toString(), Toast.LENGTH_LONG)
                        .show();
                try {
                    exportTag(defaultFile);
                }
                catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        final Button import_button = findViewById(R.id.import_button);
        import_button.setOnClickListener(self -> {
            try {
                final Intent intent = new Intent(
                        "org.openintents.action.PICK_FILE");
                startActivityForResult(intent, PICK_FILE);
            } catch (final RuntimeException rr) {
              Toast.makeText(getApplicationContext(),getString(R.string.OIFile), Toast.LENGTH_LONG).show();
              importTag(defaultFile);
            }
        });

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
            if ("Ultralight".equalsIgnoreCase(typ_karty) || "Ultralight C".equalsIgnoreCase(typ_karty)) {
                final EditText et00 = findViewById(R.id.editText00);
                final EditText et01 = findViewById(R.id.editText01);
                final EditText et02 = findViewById(R.id.editText02);
                final EditText et03 = findViewById(R.id.editText03);
                final EditText et04 = findViewById(R.id.editText04);
                final EditText et05 = findViewById(R.id.editText05);
                final EditText et06 = findViewById(R.id.editText06);
                final EditText et07 = findViewById(R.id.editText07);
                final EditText et08 = findViewById(R.id.editText08);
                final EditText et09 = findViewById(R.id.editText09);
                final EditText et0A = findViewById(R.id.editText0A);
                final EditText et0B = findViewById(R.id.editText0B);
                final EditText et0C = findViewById(R.id.editText0C);
                final EditText et0D = findViewById(R.id.editText0D);
                final EditText et0E = findViewById(R.id.editText0E);
                final EditText et0F = findViewById(R.id.editText0F);

                if (!card_write) {

                    byte[] buffer = readpage(tag, 0);
                    et00.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 1);
                    et01.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 2);
                    et02.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 3);
                    et03.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 4);
                    et04.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 5);
                    et05.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 6);
                    et06.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 7);
                    et07.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 8);
                    et08.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 9);
                    et09.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 10);
                    et0A.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 11);
                    et0B.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 12);
                    et0C.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 13);
                    et0D.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 14);
                    et0E.setText(Hex.toHexString(buffer));
                    buffer = readpage(tag, 15);
                    et0F.setText(Hex.toHexString(buffer));
                }
                else {
                    final byte[] buffer00 = Hex.toByteArray(et00.getText().toString());
                    final byte[] buffer01 = Hex.toByteArray(et01.getText().toString());
                    final byte[] buffer02 = Hex.toByteArray(et02.getText().toString());
                    final byte[] buffer03 = Hex.toByteArray(et03.getText().toString());
                    final byte[] buffer04 = Hex.toByteArray(et04.getText().toString());
                    final byte[] buffer05 = Hex.toByteArray(et05.getText().toString());
                    final byte[] buffer06 = Hex.toByteArray(et06.getText().toString());
                    final byte[] buffer07 = Hex.toByteArray(et07.getText().toString());
                    final byte[] buffer08 = Hex.toByteArray(et08.getText().toString());
                    final byte[] buffer09 = Hex.toByteArray(et09.getText().toString());
                    final byte[] buffer0A = Hex.toByteArray(et0A.getText().toString());
                    final byte[] buffer0B = Hex.toByteArray(et0B.getText().toString());
                    final byte[] buffer0C = Hex.toByteArray(et0C.getText().toString());
                    final byte[] buffer0D = Hex.toByteArray(et0D.getText().toString());
                    final byte[] buffer0E = Hex.toByteArray(et0E.getText().toString());
                    final byte[] buffer0F = Hex.toByteArray(et0F.getText().toString());

                    final CheckBox cb00 = findViewById(R.id.checkBox00);
                    final CheckBox cb01 = findViewById(R.id.checkBox01);
                    final CheckBox cb02 = findViewById(R.id.checkBox02);
                    final CheckBox cb03 = findViewById(R.id.checkBox03);
                    final CheckBox cb04 = findViewById(R.id.checkBox04);
                    final CheckBox cb05 = findViewById(R.id.checkBox05);
                    final CheckBox cb06 = findViewById(R.id.checkBox06);
                    final CheckBox cb07 = findViewById(R.id.checkBox07);
                    final CheckBox cb08 = findViewById(R.id.checkBox08);
                    final CheckBox cb09 = findViewById(R.id.checkBox09);
                    final CheckBox cb0A = findViewById(R.id.checkBox0A);
                    final CheckBox cb0B = findViewById(R.id.checkBox0B);
                    final CheckBox cb0C = findViewById(R.id.checkBox0C);
                    final CheckBox cb0D = findViewById(R.id.checkBox0D);
                    final CheckBox cb0E = findViewById(R.id.checkBox0E);
                    final CheckBox cb0F = findViewById(R.id.checkBox0F);
                    if (cb00.isChecked()) {
                        writeUltralightTagByPage(tag, 0, buffer00);
                    }
                    if (cb01.isChecked()) {
                        writeUltralightTagByPage(tag, 1, buffer01);
                    }
                    if (cb02.isChecked()) {
                        writeUltralightTagByPage(tag, 2, buffer02);
                    }
                    if (cb03.isChecked()) {
                        writeUltralightTagByPage(tag, 3, buffer03);
                    }
                    if (cb04.isChecked()) {
                        writeUltralightTagByPage(tag, 4, buffer04);
                    }
                    if (cb05.isChecked()) {
                        writeUltralightTagByPage(tag, 5, buffer05);
                    }
                    if (cb06.isChecked()) {
                        writeUltralightTagByPage(tag, 6, buffer06);
                    }
                    if (cb07.isChecked()) {
                        writeUltralightTagByPage(tag, 7, buffer07);
                    }
                    if (cb08.isChecked()) {
                        writeUltralightTagByPage(tag, 8, buffer08);
                    }
                    if (cb09.isChecked()) {
                        writeUltralightTagByPage(tag, 9, buffer09);
                    }
                    if (cb0A.isChecked()) {
                        writeUltralightTagByPage(tag, 10, buffer0A);
                    }
                    if (cb0B.isChecked()) {
                        writeUltralightTagByPage(tag, 11, buffer0B);
                    }
                    if (cb0C.isChecked()) {
                        writeUltralightTagByPage(tag, 12, buffer0C);
                    }
                    if (cb0D.isChecked()) {
                        writeUltralightTagByPage(tag, 13, buffer0D);
                    }
                    if (cb0E.isChecked()) {
                        writeUltralightTagByPage(tag, 14, buffer0E);
                    }
                    if (cb0F.isChecked()) {
                        writeUltralightTagByPage(tag, 15, buffer0F);
                    }
                }
                Toast.makeText(getApplicationContext(),
                        getString(R.string.done), Toast.LENGTH_SHORT).show();
                final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

            }
            else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.card_not_supported),
                        Toast.LENGTH_LONG).show();
            }
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

    public byte[] readpage(Tag tag, int page) {
        try (final MifareUltralight mifare = MifareUltralight.get(tag)){
            mifare.connect();
            final byte[] buffer = mifare.readPages(page);
            return Arrays.copyOf(buffer, 4);
        } catch (final Exception ex ) {
            errortext = getString(R.string.exReadingPage) + page + " " + ex;
            Log.e("NFC-MIFARE-UL", errortext, ex );
            Toast.makeText(getApplicationContext(), errortext, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void writeUltralightTagByPage(Tag tag, int page, byte[] data) {
        try (MifareUltralight ultralight = MifareUltralight.get(tag)){
            ultralight.connect();
            ultralight.writePage(page, data);
        } catch (final Exception ex ) {
            errortext = getString(R.string.exWritingPage) + page + " " + ex;
            Log.e("NFC-MIFARE-UL", errortext, ex );
            Toast.makeText(getApplicationContext(), errortext,
                    Toast.LENGTH_SHORT).show();
        }
    }

}
