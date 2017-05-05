package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

        Button save = (Button) findViewById(R.id.btn_settings_save);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupTheme);

        if (themeName.equals("BlackTheme")) {
            radioGroup.check(R.id.rbtn_darkTheme);
        } else if (themeName.equals("LightTheme")) {
            radioGroup.check(R.id.rbtn_lightTheme);
        }else if(themeName.equals("Default")){
            radioGroup.check(R.id.rbtn_defaultTheme);
        }

        final SharedPreferences.Editor editor = prefs.edit();

        save.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.rbtn_darkTheme:
                    editor.putString("Theme", "BlackTheme");

                    Toast.makeText(context, "Dark", Toast.LENGTH_SHORT).show();
                    break;
                    case R.id.rbtn_lightTheme:
                    editor.putString("Theme", "LightTheme");

                    Toast.makeText(context, "Light", Toast.LENGTH_SHORT).show();
                    break;
                    case R.id.rbtn_defaultTheme:
                    editor.putString("Theme", "Default");

                    Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show();
                    break;
                }
                editor.commit();
            }
        });
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