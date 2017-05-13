package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePlanActivity extends AppCompatActivity {

    final Context context = this;

    List<String> splits = new ArrayList<String>();
    String currentPlan = "";

    List<Uebung> uebungen = new ArrayList<Uebung>();

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

        setContentView(R.layout.creating_plan);

        Bundle params = getIntent().getExtras();
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        final Plan newPlan = new Plan(params.getString("param"), date, "-");

        setTitle(newPlan.getName());

        createSplitButton();
        FloatingActionButton readyButton = (FloatingActionButton) findViewById(R.id.readyButton);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle(R.string.alert_savePlanTitle);
                alertDialogBuilder
                        .setMessage(R.string.alert_savePlanMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insertPlan(newPlan);

                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

                                    dataSource.open();
                                    Cursor c = db.rawQuery("SELECT plan_id FROM plan WHERE name='"+newPlan.getName()+"'", null);
                                    c.moveToFirst();
                                    String id = c.getString(c.getColumnIndex("plan_id"));

                                    for(int i = 0; i<uebungen.size(); i++){
                                        insertUebung(uebungen.get(i), id);
                                    }
                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }

                                Intent i = new Intent(CreatePlanActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void createSplitButton(){
        Button splitButton = new Button(this);
        splitButton.setText(R.string.alert_addSplitTitle);

        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(splitButton, lp);
        splitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle(R.string.alert_addSplitTitle);

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(input);
                input.setId(R.id.calabash);

                alertDialogBuilder
                        .setMessage(R.string.alert_addSplitMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String eingabe = input.getText().toString().toUpperCase();
                                String returned = checkEingabe(eingabe);
                                if(returned == null){
                                    Toast.makeText(context, R.string.toast_errorEnterName, Toast.LENGTH_SHORT).show();
                                }else {
                                    splits.add(returned);
                                    redrawGUI();
                                    dialog.cancel();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void createExerciseButton(String t_plan){
        Button exerciseButton = new Button(this);
        exerciseButton.setText(R.string.alert_addExerciseTitle);
        final String temp_plan = t_plan;
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(exerciseButton, lp);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle(R.string.alert_addExerciseTitle);

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText e_name = new EditText(context);
                e_name.setHint(R.string.hint_name);
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                e_name.setId(R.id.c_name);
                layout.addView(e_name);

                final EditText e_reps = new EditText(context);
                e_reps.setHint(R.string.hint_reps);
                e_reps.setInputType(InputType.TYPE_CLASS_TEXT);
                e_reps.setId(R.id.c_reps);
                layout.addView(e_reps);

                final EditText e_sw = new EditText(context);
                e_sw.setHint(R.string.hint_weight);
                e_sw.setInputType(InputType.TYPE_CLASS_TEXT);
                e_sw.setId(R.id.c_sw);
                layout.addView(e_sw);

                alertDialogBuilder.setView(layout);

                alertDialogBuilder
                        .setMessage(R.string.alert_addExerciseMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uebungen.add(new Uebung(e_name.getText().toString(),
                                        e_reps.getText().toString(),
                                        e_sw.getText().toString(), temp_plan));
                                redrawGUI();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void createDeleteButton(final int toDel, LinearLayout tempLL){
        TextView delete = new TextView(this);
        String sourceString = "<b>X</b>";
        delete.setText(Html.fromHtml(sourceString));
        delete.setGravity(Gravity.LEFT);
        delete.setPadding(5,1,5,1);
        delete.setTextSize(pxFromDp(9, context));

        tempLL.addView(delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                uebungen.remove(toDel);
                redrawGUI();
            }
        });
    }

    public void createSplitDelete(final int splitDel, LinearLayout deleteLL){
        TextView deleteS = new TextView(this);
        String sourceString = "<b>X</b>";
        deleteS.setText(Html.fromHtml(sourceString));
        deleteS.setGravity(Gravity.LEFT);
        deleteS.setPadding(5,1,5,1);
        deleteS.setTextSize(pxFromDp(11, context));

        deleteLL.addView(deleteS);
        deleteS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                splits.remove(splitDel);
                redrawGUI();
            }
        });
    }

    public void redrawGUI(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        if(((LinearLayout) ll).getChildCount() > 0)
            ((LinearLayout) ll).removeAllViews();

        for(int i = 0; i < splits.size(); i++){
            currentPlan = splits.get(i);
            boolean toTest = true;
            int k = 0;
            while(k < uebungen.size() && toTest){
                if(currentPlan.equals((uebungen.get(k).getSplit()))){
                    toTest = false;
                }
                k++;
            }
            if(toTest){
                LinearLayout deleteLL = new LinearLayout(this);
                createSplitDelete(i, deleteLL);
                TextView textView = new TextView(this);
                String sourceString = "<b>" + splits.get(i) + "</b>";
                textView.setText(Html.fromHtml(sourceString));
                textView.setPadding(8, 5, 16, 2);
                deleteLL.addView(textView);
                ll.addView(deleteLL);
            }else {
                TextView textView = new TextView(this);
                String sourceString = "<b>" + splits.get(i) + "</b>";
                textView.setText(Html.fromHtml(sourceString));
                textView.setPadding(16, 5, 16, 2);
                ll.addView(textView);
            }
            LinearLayout tempLL = null;

            for(int j = 0; j < uebungen.size(); j++){
                tempLL = new LinearLayout(this);
                if(currentPlan.equals(uebungen.get(j).getSplit())){
                    createDeleteButton(j, tempLL);
                    TextView exerView = new TextView(this);
                    exerView.setPadding(10,2,10,2);

                    String reps = getResources().getString(R.string.hint_reps)+": ";
                    String weight = getResources().getString(R.string.hint_weight)+": ";
                    String toShow = uebungen.get(j).getName() + reps + uebungen.get(j).getReps()
                            + weight + uebungen.get(j).getStart();
                    exerView.setText(toShow);
                    tempLL.addView(exerView);
                    ll.addView(tempLL);

                    View v = new View(this);
                    v.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                    ));
                    v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                    ll.addView(v);
                }
            }
            createExerciseButton(currentPlan);

            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            ll.addView(v);
        }
        createSplitButton();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle(R.string.alert_cancelAddingTitle);
        alertDialogBuilder
                .setMessage(R.string.alert_cancelAddingMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(CreatePlanActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public static String checkEingabe(String input){
        if(input.isEmpty()){
            return null;
        }
        String newInput = input.trim();
        if(newInput == "" || newInput.isEmpty()){
            return null;
        }

        return newInput;
    }

    public void insertUebung(Uebung u, String id){
        SQLiteDatabase db = null;
        try {
            db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

            db.execSQL("INSERT INTO uebung (plan_id, name, reps, start, split)" +
                    " VALUES ('"+id+"', '"+u.getName()+
                    "', '"+u.getReps()+"', '"+
                    u.getStart()+"', '"+
                    u.getSplit()+"')");

            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }
    }

    public void insertPlan(Plan plan){
        SQLiteDatabase db = null;
        try {
            db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

            dataSource.open();
            db.execSQL("INSERT INTO plan (name, date_create, date_last) " +
                    "VALUES ('"+plan.getName()+"','"+plan.getDate_create()+
                    "', '"+plan.getDate_last()+"')");

            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }
    }

    public String selectDate(String name){
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT date_last FROM plan WHERE name='"+name+"'", null);
            c.moveToFirst();
            String datum = c.getString(c.getColumnIndex("date_last"));

            dataSource.close();
            return datum;
        }finally {
            if (db != null)
                db.close();
        }
    }

    public String selectReps(String name){
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT reps FROM uebung WHERE name='"+name+"'", null);
            c.moveToFirst();
            String reps = c.getString(c.getColumnIndex("reps"));

            dataSource.close();
            return reps;
        }finally {
            if (db != null)
                db.close();
        }
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


