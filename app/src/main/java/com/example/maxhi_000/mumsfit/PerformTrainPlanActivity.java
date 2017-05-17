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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class PerformTrainPlanActivity extends AppCompatActivity{

    String namePlan;
    private TrainPlanDataSource dataSource;
    final Context context = this;

    static ArrayList<Uebung> uebung = new ArrayList<Uebung>();
    static ArrayList<String> splits = new ArrayList<String>();

    static String current_split;
    static ArrayList<Uebung> split_uebung = new ArrayList<Uebung>();

    ArrayAdapter dataAdapter_uebung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "MyPrefs", MODE_PRIVATE);

        String themeName = prefs.getString("Theme", "Default");
        if (themeName.equals("BlackTheme")) {
            setTheme(R.style.BlackTheme);
        }else if (themeName.equals("LightTheme")) {
            setTheme(R.style.LightTheme);
        }else if(themeName.equals("Default")){
            setTheme(R.style.AppTheme);
        }

        String appLanguage = prefs.getString("Language", "en-US");
        setLocale(appLanguage);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.perform_plan);

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
                fillGUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                fillGUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_okay = (Button) findViewById(R.id.button_ok);
        btn_okay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText input_weight = (EditText) findViewById(R.id.input_weight);
                double newGewicht = Double.parseDouble(input_weight.getText().toString());
                SQLiteDatabase db = null;
                try {
                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                    dataSource = new TrainPlanDataSource(context);
                    dataSource.open();

                    Uebung test = (Uebung) ( (Spinner) findViewById(R.id.spinner_uebung) ).getSelectedItem();

                    db.execSQL("INSERT INTO gewicht (uebung_id, gewicht)" +
                            " VALUES ('"+test.getUebungID()+"', '"+newGewicht+"')");

                    dataSource.close();
                }finally {
                    if (db != null)
                        db.close();
                }
                input_weight.setText("");
                fillGUI();
            }
        });
        fillGUI();
    }

    public void fillGUI(){
        Uebung test = (Uebung) ( (Spinner) findViewById(R.id.spinner_uebung) ).getSelectedItem();
        TextView text_reps = (TextView) findViewById(R.id.textView_reps);
        text_reps.setText(getString(R.string.hint_reps) + ": "+ test.getReps());

        String gewicht = "";
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT gewicht.gewicht FROM plan, gewicht, uebung WHERE " +
                    "uebung.name='"+test.getName()+"' AND uebung.uebung_id=gewicht.uebung_id " +
                    "AND plan.name='"+this.namePlan+"' AND plan.plan_id=uebung.plan_id", null);

            if (c.moveToLast()) {
                gewicht = c.getString(c.getColumnIndex("gewicht"));
            } else{
                gewicht = test.getStart()+"";
            }
            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }

        EditText input_weight = (EditText) findViewById(R.id.input_weight);
        input_weight.setHint(gewicht);
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

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        removeUebungen();
        Bundle temp = new Bundle();
        temp.putString("param", this.namePlan);
        Intent i = new Intent(PerformTrainPlanActivity.this, ViewPlanActivity.class);
        i.putExtras(temp);
        startActivity(i);
        finish();
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
