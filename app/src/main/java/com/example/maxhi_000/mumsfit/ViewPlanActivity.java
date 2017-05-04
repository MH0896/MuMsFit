package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewPlanActivity  extends AppCompatActivity {
//new
    String namePlan;
    private TrainPlanDataSource dataSource;
    final Context context = this;
    ArrayList<String> exercises = new ArrayList<String>();
    ArrayList<String> reps = new ArrayList<String>();
    ArrayList<String> sweight = new ArrayList<String>();
    ArrayList<String> split = new ArrayList<String>();
    ArrayList<String> cweight = new ArrayList<String>();

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


        setContentView(R.layout.view_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        setTitle(this.namePlan);

        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT uebung.name, uebung.reps, uebung.start, uebung.split " +
                        "FROM plan, uebung WHERE plan.plan_id = uebung.plan_id", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    exercises.add(c.getString(c.getColumnIndex("name")));
                    reps.add(c.getString(c.getColumnIndex("reps")));
                    sweight.add(c.getString(c.getColumnIndex("start")));
                    split.add(c.getString(c.getColumnIndex("split")));
                    cweight.add("-");
                    c.moveToNext();
                }
            }
            dataSource.close();
            drawGUI();
        }finally {
            if (db != null)
                db.close();
        }
    }

    public void drawGUI(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearView);

        for(int i = 0; i < exercises.size(); i++){
            if(i == 0){
                TextView textView = new TextView(this);
                String sourceString = "<b>" + split.get(i) + "</b>";
                textView.setText(Html.fromHtml(sourceString));
                textView.setPadding(16, 5, 16, 2);
                ll.addView(textView);
            }
            else if(!split.get(i).equals(split.get(i-1))){
                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                ll.addView(v);

                TextView textView = new TextView(this);
                String sourceString = "<b>" + split.get(i) + "</b>";
                textView.setText(Html.fromHtml(sourceString));
                textView.setPadding(16, 5, 16, 2);
                ll.addView(textView);
            }else{
                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                ll.addView(v);
            }
            TextView exerView = new TextView(this);
            exerView.setPadding(10,2,10,2);
            String toShow = exercises.get(i) + " Reps: " + reps.get(i) + " Startgewicht: " + sweight.get(i);
            exerView.setText(toShow);
            ll.addView(exerView);
        }
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent i = new Intent(ViewPlanActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit_menu:
                Bundle temp = new Bundle();
                temp.putString("param", namePlan);
                Intent i = new Intent(ViewPlanActivity.this, EditPlanActivity.class);
                i.putExtras(temp);
                startActivity(i);
                finish();
                return true;
            case R.id.item_start_training:
                Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_details_menu:
                DetailsClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void DetailsClick(){
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            String trainings = "default";
            String date_create = "default";
            String date_last = "default";
            Cursor c = db.rawQuery("SELECT trainings,date_create,date_last FROM details WHERE plan='["+ namePlan +"]'", null);
            if(c.moveToFirst()) {
                trainings = c.getString(0);
                date_create = c.getString(1);
                date_last = c.getString(2);
                c.close();
            }

            dataSource.close();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setTitle(namePlan);

            alertDialogBuilder
                    .setMessage("Anzahl der Einheiten: "+trainings+"\nErstellt am: "+date_create+"\nZuletzt durchgeführt am: "+date_last)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } finally {
            if (db != null)
                db.close();
        }
    }
}
