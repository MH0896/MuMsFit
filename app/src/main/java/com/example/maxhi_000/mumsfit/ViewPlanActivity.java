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

    String namePlan;
    private TrainPlanDataSource dataSource;
    final Context context = this;

    static ArrayList<Uebung> uebung = new ArrayList<Uebung>();

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
                    "FROM plan, uebung WHERE plan.plan_id = uebung.plan_id AND plan.name='"+
                    this.namePlan+"'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String name = (c.getString(c.getColumnIndex("name")));
                    String reps = (c.getString(c.getColumnIndex("reps")));
                    String start = (c.getString(c.getColumnIndex("start")));
                    String split = (c.getString(c.getColumnIndex("split")));
                    uebung.add(new Uebung(name, reps, start, split));
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

    public static  boolean alreadyIn(String split, ArrayList<String> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(split)){
                return true;
            }
        }
        return false;
    }

    public static void createOrderOfUebung(){
        ArrayList<String> splits = new ArrayList<String>();
        for(int i = 0; i < uebung.size(); i++){
            if (i==0){
                splits.add(uebung.get(i).getSplit());
            }
            else if(!uebung.get(i).getSplit().equals(uebung.get(i-1).getSplit())){
                boolean result = alreadyIn(uebung.get(i).getSplit(), splits);
                if(result){
                    boolean breakOut = false;
                    int count = 0;
                    while(!breakOut) {
                        if (uebung.get(i).getSplit().equals(uebung.get(count).getSplit())) {
                            breakOut = true;
                        }else{
                            count++;
                        }
                    }
                    Uebung temp = uebung.get(i);
                    uebung.remove(i);
                    uebung.add(count, temp);
                }
                else{
                    splits.add(uebung.get(i).getSplit());
                }
            }
        }
    }

    public void drawGUI(){
        createOrderOfUebung();
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearView);

        for(int i = 0; i < uebung.size(); i++){
            if(i == 0){
                TextView textView = new TextView(this);
                String sourceString = "<b>" + uebung.get(i).getSplit() + "</b>";
                textView.setText(Html.fromHtml(sourceString));
                textView.setPadding(16, 5, 16, 2);
                ll.addView(textView);
            }
            else if(!uebung.get(i).getSplit().equals(uebung.get(i-1).getSplit())){
                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                ll.addView(v);

                TextView textView = new TextView(this);
                String sourceString = "<b>" + uebung.get(i).getSplit() + "</b>";
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
            String toShow = uebung.get(i).getName() + " Reps: " + uebung.get(i).getReps() +
                    " Startgewicht: " + uebung.get(i).getStart();
            exerView.setText(toShow);
            ll.addView(exerView);
        }
    }

    public void removeUebungen(){
        int temp = uebung.size();
        for(int i = 0; i<temp; i++){
            uebung.remove(0);
        }
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        removeUebungen();
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
                EditClick();
                return true;
            case R.id.item_start_training:
                StartClick();
                return true;
            case R.id.item_details_menu:
                DetailsClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void DetailsClick() {
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            String date_create = "default";
            String date_last = "default";
            Cursor c = db.rawQuery("SELECT date_create,date_last FROM plan WHERE name='" + namePlan + "'", null);
            if (c.moveToFirst()) {
                date_create = c.getString(0);
                date_last = c.getString(1);
                c.close();
            }

            dataSource.close();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setTitle(namePlan);

            alertDialogBuilder
                    .setMessage("Erstellt am: " + date_create + "\nZuletzt durchgeführt am: " + date_last)
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

    public void EditClick() {
        removeUebungen();
        Bundle temp = new Bundle();
        temp.putString("param", namePlan);
        Intent i = new Intent(ViewPlanActivity.this, EditPlanActivity.class);
        i.putExtras(temp);
        startActivity(i);
        finish();
    }

    public void StartClick(){
        removeUebungen();
        Bundle t = new Bundle();
        t.putString("param", namePlan);
        Intent in = new Intent(ViewPlanActivity.this, PerformTrainPlanActivity.class);
        in.putExtras(t);
        startActivity(in);
        finish();
    }
}
