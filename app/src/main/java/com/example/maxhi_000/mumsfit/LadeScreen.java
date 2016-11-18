package com.example.maxhi_000.mumsfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LadeScreen extends AppCompatActivity {

    private static int zeit = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent i = new Intent(LadeScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },zeit);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }
}