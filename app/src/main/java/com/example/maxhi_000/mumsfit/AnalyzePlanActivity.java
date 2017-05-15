package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;

public class AnalyzePlanActivity extends AppCompatActivity{

    String namePlan;
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
        setContentView(R.layout.analyze_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        setTitle(this.namePlan);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
        });
        graph.getGridLabelRenderer().setHorizontalAxisTitle(getResources().getString(R.string.analyze_graphHorizontal));
        graph.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.analyze_graphVertical));
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(pxFromDp(18,context));
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(pxFromDp(18,context));
        graph.getGridLabelRenderer().setPadding((int)pxFromDp(25,context));

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent i = new Intent(AnalyzePlanActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public void setLocale(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = myLocale;
        res.updateConfiguration(config, dm);
    }
}
