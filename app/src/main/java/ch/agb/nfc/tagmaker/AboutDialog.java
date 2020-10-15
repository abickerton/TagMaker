/*
  Copyright (C) 2014 Mateusz Szafraniec
  This file is part of Burze nad Polską.

  Burze nad Polską is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Burze nad Polską is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Burze nad Polską; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

  Ten plik jest częścią Burze nad Polską.

  Burze nad Polską jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
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

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutDialog extends Dialog {
    private static int mIcon;
    private static String mInfo;
    private static String mLegal;
    private final Context mContext;

    public AboutDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about);
        TextView tv = findViewById(R.id.legal_text);
        if (StringUtils.isNotBlank(mLegal)) {
            tv.setText(Html.fromHtml(mLegal));
        }
        else {
            tv.setText(Html.fromHtml(readRawTextFile(R.raw.legal,
                    mContext)));
        }
        tv = findViewById(R.id.info_text);
        if (StringUtils.isNotBlank(mInfo)) {
            tv.setText(Html.fromHtml(mInfo));
        }
        else {
            tv.setText(Html.fromHtml(
                    readRawTextFile(R.raw.info, mContext)
                    + getAppVersion(mContext)));
        }
        tv.setLinkTextColor(Color.WHITE);
        final ImageView iv = findViewById(R.id.aboutIcon);
        iv.setImageResource(mIcon);
        Linkify.addLinks(tv, Linkify.ALL);
        this.setTitle(mContext.getString(R.string.action_about));

    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public void setInfo(String info) {
        mInfo = info;
    }

    public void setLegal(String legal) {
        mLegal = legal;
    }

    private static String readRawTextFile(int id, Context mContext) {
        final InputStream inputStream = mContext.getResources()
                .openRawResource(id);
        final InputStreamReader in = new InputStreamReader(inputStream);
        final BufferedReader buf = new BufferedReader(in);
        String line;
        final StringBuilder text = new StringBuilder();
        try {
            while ((line = buf.readLine()) != null) {
                text.append(line);
            }
        }
        catch (final IOException e) {
            return null;
        }
        return text.toString();
    }

    private static String getAppVersion(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }
        catch (final PackageManager.NameNotFoundException ex) {
            throw new RuntimeException("Could not get package name: " + ex);
        }
    }
}
