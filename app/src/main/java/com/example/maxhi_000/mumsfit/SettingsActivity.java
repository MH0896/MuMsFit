package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Moritz on 02.05.2017.
 */

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
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        Button setDarkTheme = (Button) findViewById(R.id.btn_dark);
        Button setLightTheme = (Button) findViewById(R.id.btn_light);
        Button save = (Button) findViewById(R.id.btn_settings_save);

        setDarkTheme.setOnClickListener(this);
        setLightTheme.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (v.getId()) {
            case R.id.btn_dark:
                editor.putString("Theme", "BlackTheme");

                Toast.makeText(context, "Dark", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_light:
                editor.putString("Theme", "LightTheme");

                Toast.makeText(context, "Light", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_settings_save:
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
        editor.commit();
    }
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}