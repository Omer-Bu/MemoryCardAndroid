package com.memorycardgame.views.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;


import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.memorycardgame.R;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        CardView engLang =findViewById(R.id.engCv);
        CardView hebrew =findViewById(R.id.hebrew);
        engLang.setOnClickListener(view -> setLocale("en"));
        hebrew.setOnClickListener(view -> setLocale("iw"));

    }

    //change the app language with this code
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
        //overridePendingTransition(0, 0);

    }
}