package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Locale;

public class AnalyzePlanActivity extends AppCompatActivity{

    public String namePlan;
    public final Context context = this;
    private TrainPlanDataSource dataSource;

    public static ArrayList<Uebung> uebung = new ArrayList<Uebung>();
    public static ArrayList<String> splits = new ArrayList<String>();

    public static String current_split;
    public static ArrayList<Uebung> split_uebung = new ArrayList<Uebung>();

    public ArrayAdapter dataAdapter_uebung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "MyPrefs", MODE_PRIVATE);

        String themeName = prefs.getString("Theme", "Default");
        if ("BlackTheme".equals(themeName)) {
            setTheme(R.style.BlackTheme);
        } else if ("LightTheme".equals(themeName)) {
            setTheme(R.style.LightTheme);
        }else if("Default".equals(themeName)){
            setTheme(R.style.AppTheme);
        }

        String appLanguage = prefs.getString("Language", "en-US");
        setLocale(appLanguage);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        setTitle(this.namePlan);

        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT uebung.uebung_id, uebung.plan_id, uebung.name, uebung.reps, uebung.start, uebung.split " +
                    "FROM plan, uebung WHERE plan.plan_id = uebung.plan_id AND plan.name='"+
                    this.namePlan+"'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String uid = (c.getString(c.getColumnIndex("uebung_id")));
                    String pid = (c.getString(c.getColumnIndex("plan_id")));
                    String name = (c.getString(c.getColumnIndex("name")));
                    String reps = (c.getString(c.getColumnIndex("reps")));
                    String start = (c.getString(c.getColumnIndex("start")));
                    String split = (c.getString(c.getColumnIndex("split")));
                    boolean temp = alreadyIn(split, splits);
                    if(!temp){
                        splits.add(split);
                    }
                    uebung.add(new Uebung(Integer.parseInt(uid), Integer.parseInt(pid), name, reps, Double.parseDouble(start), split));
                    c.moveToNext();
                }
            }
            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }

        current_split = splits.get(0);

        Spinner spinner_splits = (Spinner) findViewById(R.id.spinner_split);
        ArrayAdapter dataAdapter_split = new ArrayAdapter(this,android.R.layout.simple_spinner_item, splits);
        dataAdapter_split.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_splits.setAdapter(dataAdapter_split);
        spinner_splits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                current_split = selected;
                createSplitUebung();
                dataAdapter_uebung.notifyDataSetChanged();
                createGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //wird niemals aufgerufen
            }
        });


        createSplitUebung();
        Spinner spinner_uebung = (Spinner) findViewById(R.id.spinner_uebung);
        dataAdapter_uebung = new ArrayAdapter(this,android.R.layout.simple_spinner_item, split_uebung);
        dataAdapter_uebung.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_uebung.setAdapter(dataAdapter_uebung);
        spinner_uebung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //wird niemals aufgerufen
            }
        });

        createGraph();
    }

    public void createGraph(){
        Uebung test = (Uebung) ( (Spinner) findViewById(R.id.spinner_uebung) ).getSelectedItem();
        ArrayList<Gewicht> gewichte = new ArrayList<Gewicht>();
        int maxY =0;
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT gewicht.gewicht FROM plan, gewicht, uebung WHERE " +
                    "uebung.name='"+test.getName()+"' AND uebung.uebung_id=gewicht.uebung_id " +
                    "AND plan.name='"+this.namePlan+"' AND plan.plan_id=uebung.plan_id", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String temp = c.getString(c.getColumnIndex("gewicht"));
                    gewichte.add(new Gewicht(0, Double.parseDouble(temp)));
                    if(Double.parseDouble(temp) > maxY){
                        double d = Double.parseDouble(temp)+1;
                        maxY =  (int) Math.round(d);
                    }
                    c.moveToNext();
                }
            }

            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }

        int maxX = gewichte.size()+1;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        series.appendData(new DataPoint(0, test.getStart()), true, gewichte.size()+2);
        for (int i = 0; i < gewichte.size(); i++){
            series.appendData(new DataPoint((i+1), gewichte.get(i).getGewicht()), true, gewichte.size()+2);
        }

        graph.getGridLabelRenderer().setHorizontalAxisTitle(getResources().getString(R.string.analyze_graphHorizontal));
        graph.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.analyze_graphVertical));
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(pxFromDp(18,context));
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(pxFromDp(18,context));
        graph.getGridLabelRenderer().setPadding((int)pxFromDp(25,context));

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxY);

        graph.addSeries(series);
    }

    public static void createSplitUebung() {
        int temp = split_uebung.size();
        for(int i = 0; i < temp; i++){
            split_uebung.remove(0);
        }
        for(int j = 0; j < uebung.size(); j++){
            if(uebung.get(j).getSplit().equals(current_split)){
                split_uebung.add(uebung.get(j));
            }
        }
    }

    public static  boolean alreadyIn(String split, ArrayList<String> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(split)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        removeUebungen();
        Intent i = new Intent(AnalyzePlanActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public static void removeUebungen(){
        int temp = uebung.size();
        for(int i = 0; i<temp; i++){
            uebung.remove(0);
        }
        temp = splits.size();
        for(int i = 0; i<temp; i++){
            splits.remove(0);
        }
        temp = split_uebung.size();
        for(int i = 0; i<temp; i++){
            split_uebung.remove(0);
        }
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