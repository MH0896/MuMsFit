package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "MyPrefs", MODE_PRIVATE);

        String themeName = prefs.getString("Theme", "Default");
        if (themeName.equals("BlackTheme")) {
            setTheme(R.style.BlackTheme);
        } else if (themeName.equals("LightTheme")) {
            setTheme(R.style.LightTheme);
        }else if(themeName.equals("Default")){
            setTheme(R.style.AppTheme);
        }

        String appLanguage = prefs.getString("Language", "en-US");
        setLocale(appLanguage);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button save = (Button) findViewById(R.id.btn_settings_save);
        RadioGroup radioGroupTheme = (RadioGroup) findViewById(R.id.radioGroupTheme);

        if (themeName.equals("BlackTheme")) {
            radioGroupTheme.check(R.id.rbtn_darkTheme);
        } else if (themeName.equals("LightTheme")) {
            radioGroupTheme.check(R.id.rbtn_lightTheme);
        }else if(themeName.equals("Default")){
            radioGroupTheme.check(R.id.rbtn_defaultTheme);
        }

        RadioGroup radioGroupLanguage = (RadioGroup) findViewById(R.id.radioGroupLanguage);
        if(appLanguage.equals("de")){
            radioGroupLanguage.check(R.id.rbtn_lang_de);
        }else if(appLanguage.equals("en-US")){
            radioGroupLanguage.check(R.id.rbtn_lang_en);
        }

        final SharedPreferences.Editor editor = prefs.edit();

        save.setOnClickListener(this);
        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.rbtn_darkTheme:
                        editor.putString("Theme", "BlackTheme");
                        break;
                    case R.id.rbtn_lightTheme:
                        editor.putString("Theme", "LightTheme");
                        break;
                    case R.id.rbtn_defaultTheme:
                        editor.putString("Theme", "Default");
                        break;
                }
                editor.commit();
            }
        });

        radioGroupLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i){
                switch(i){
                    case R.id.rbtn_lang_de:
                        editor.putString("Language", "de");
                                break;
                    case R.id.rbtn_lang_en:
                        editor.putString("Language", "en-US");
                        break;
                }
                editor.commit();
            }
        });
    }

    public void setLocale(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = myLocale;
        res.updateConfiguration(config, dm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_settings_save:
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}