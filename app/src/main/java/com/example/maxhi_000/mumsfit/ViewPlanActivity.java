package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewPlanActivity  extends AppCompatActivity {

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

            Cursor c = db.rawQuery("SELECT * FROM ["+ this.namePlan +"]", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    exercises.add(c.getString(c.getColumnIndex("exercise")));
                    reps.add(c.getString(c.getColumnIndex("reps")));
                    sweight.add(c.getString(c.getColumnIndex("start_weight")));
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
        Toast.makeText(context, exercises.size()+"", Toast.LENGTH_SHORT).show();

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
}
