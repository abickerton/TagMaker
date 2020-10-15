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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final EditText uri = (EditText) findViewById(R.id.uri);
        uri.setText(Config.uri);
        final EditText name = (EditText) findViewById(R.id.name);
        name.setText(Config.name);
        final EditText phone = (EditText) findViewById(R.id.phone);
        phone.setText(Config.phone);
        final EditText email = (EditText) findViewById(R.id.email);
        email.setText(Config.email);
        final EditText web = (EditText) findViewById(R.id.web);
        web.setText(Config.web);

        final Button x = (Button) findViewById(R.id.ok);
        x.setOnClickListener(self -> finish());

    }

    @Override
    protected void onStop() {
        super.onStop();
        final EditText uri = (EditText) findViewById(R.id.uri);
        Config.uri = uri.getText().toString();
        final EditText name = (EditText) findViewById(R.id.name);
        Config.name = name.getText().toString();
        final EditText phone = (EditText) findViewById(R.id.phone);
        Config.phone = phone.getText().toString();
        final EditText email = (EditText) findViewById(R.id.email);
        Config.email = email.getText().toString();
        final EditText web = (EditText) findViewById(R.id.web);
        Config.web = web.getText().toString();
        final SharedPreferences settings = getSharedPreferences(
                Config.PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString("uri", Config.uri);
        editor.putString("name", Config.name);
        editor.putString("phone", Config.phone);
        editor.putString("web", Config.web);
        editor.putString("email", Config.email);
        editor.apply();
    }

}
