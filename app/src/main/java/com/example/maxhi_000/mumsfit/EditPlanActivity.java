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
import java.util.Locale;

public class EditPlanActivity  extends AppCompatActivity {

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

        String appLanguage = prefs.getString("Language", "en-US");
        setLocale(appLanguage);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_plan);

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
                    uebung.add(new Uebung(name, reps, Double.parseDouble(start), split));
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

    public void createExerciseDeleteButton(final int toDel, LinearLayout tempLL){
        TextView delete = new TextView(this);
        String sourceString = "<b>X</b>";
        delete.setText(Html.fromHtml(sourceString));
        delete.setGravity(Gravity.LEFT);
        delete.setPadding(5,1,5,1);
        delete.setTextSize(pxFromDp(9, context));

        tempLL.addView(delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle(R.string.alert_deleteExerciseTitle);
                alertDialogBuilder
                        .setMessage(R.string.alert_deleteExerciseMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
                                    db.execSQL("DELETE FROM uebung WHERE name='"+uebung.get(toDel).getName()+"'");

                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }
                                removeUebungen();
                                finish();
                                startActivity(getIntent());
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

    public void createExerciseEditButton(final int toEdit, LinearLayout tempLL){
        TextView delete = new TextView(this);
        String sourceString = "<b>B</b>";
        delete.setText(Html.fromHtml(sourceString));
        delete.setGravity(Gravity.LEFT);
        delete.setPadding(5,1,5,1);
        delete.setTextSize(pxFromDp(9, context));

        tempLL.addView(delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText e_name = new EditText(context);
                e_name.setText(uebung.get(toEdit).getName());
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                e_name.setId(R.id.c_name);
                layout.addView(e_name);

                final EditText e_reps = new EditText(context);
                e_reps.setText(uebung.get(toEdit).getReps());
                e_reps.setInputType(InputType.TYPE_CLASS_TEXT);
                e_reps.setId(R.id.c_reps);
                layout.addView(e_reps);

                final EditText e_sw = new EditText(context);
                e_sw.setText(uebung.get(toEdit).getStart()+"");
                e_sw.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                e_sw.setId(R.id.c_sw);
                layout.addView(e_sw);
                alertDialogBuilder.setView(layout);

                alertDialogBuilder.setTitle(R.string.alert_editExerciseTitle);
                alertDialogBuilder
                        .setMessage(R.string.alert_editExerciseMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = checkEingabe(e_name.getText().toString());
                                String reps = checkEingabe(e_reps.getText().toString());
                                try{
                                    Double start = Double.parseDouble(e_sw.getText().toString());

                                    if(name == null || reps == null){
                                        Toast.makeText(context, R.string.errorEnterAll, Toast.LENGTH_SHORT).show();
                                    }else {
                                        SQLiteDatabase db = null;
                                        try {
                                            db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
                                            db.execSQL("UPDATE uebung SET name='" + name + "' , reps='" + reps + "' , start='" + start + "' , split='" + uebung.get(toEdit).getSplit() + "' WHERE name='" + uebung.get(toEdit).getName() + "' ");

                                            dataSource.close();
                                        } finally {
                                            if (db != null)
                                                db.close();
                                        }
                                    }
                                    removeUebungen();
                                    finish();
                                    startActivity(getIntent());
                                } catch (Exception e){
                                    Toast.makeText(context, R.string.errorEnterStart, Toast.LENGTH_SHORT).show();
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

    public void createExerciseButton(){
        Button exerciseButton = new Button(this);
        exerciseButton.setText(R.string.alert_addExerciseTitle);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearView);;
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
                e_sw.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                e_sw.setId(R.id.c_sw);
                layout.addView(e_sw);

                final EditText e_split = new EditText(context);
                e_split.setHint(R.string.hint_split);
                e_split.setInputType(InputType.TYPE_CLASS_TEXT);
                e_split.setId(R.id.c_split);
                layout.addView(e_split);

                alertDialogBuilder.setView(layout);

                alertDialogBuilder
                        .setMessage(R.string.alert_addExerciseMessage)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = checkEingabe(e_name.getText().toString());
                                String reps = checkEingabe(e_reps.getText().toString());
                                String split = checkEingabe(e_split.getText().toString());
                                try{
                                        Double start = Double.parseDouble(e_sw.getText().toString());

                                        if(name == null || reps == null || split == null){
                                            Toast.makeText(context, R.string.errorEnterAll, Toast.LENGTH_SHORT).show();
                                        }else{
                                            SQLiteDatabase db = null;
                                            try {
                                                db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                                TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

                                                dataSource.open();
                                                Cursor c = db.rawQuery("SELECT plan_id FROM plan WHERE name='"+namePlan+"'", null);
                                                c.moveToFirst();
                                                String id = c.getString(c.getColumnIndex("plan_id"));
                                                db.execSQL("INSERT INTO uebung (plan_id, name, reps, start, split) VALUES ('"+id+"', '"+name+"', '"+reps+"', '"+start+"', '"+split.toUpperCase()+"')");

                                                dataSource.close();
                                            }finally {
                                                if (db != null)
                                                    db.close();
                                        }
                                        removeUebungen();
                                        finish();
                                        startActivity(getIntent());
                                            dialog.cancel();
                                        }
                                    } catch (Exception e){
                                        Toast.makeText(context, R.string.errorEnterStart, Toast.LENGTH_SHORT).show();
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

    public static boolean alreadyIn(String split, ArrayList<String> list){
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(split)) {
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
            LinearLayout tempLL = new LinearLayout(this);

            createExerciseDeleteButton(i, tempLL);
            createExerciseEditButton(i, tempLL);
            TextView exerView = new TextView(this);
            exerView.setPadding(10,2,10,2);

            String reps = getResources().getString(R.string.hint_reps)+": ";
            String weight = getResources().getString(R.string.hint_weight)+": ";
            String toShow = uebung.get(i).getName() + reps + uebung.get(i).getReps()
                    + weight + uebung.get(i).getStart();

            exerView.setText(toShow);
            tempLL.addView(exerView);
            ll.addView(tempLL);
        }

        createExerciseButton();
    }

    public static void removeUebungen(){
        int temp = uebung.size();
        for(int i = 0; i<temp; i++){
            uebung.remove(0);
        }
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        removeUebungen();
        Bundle temp = new Bundle();
        temp.putString("param", this.namePlan);
        Intent i = new Intent(EditPlanActivity.this, ViewPlanActivity.class);
        i.putExtras(temp);
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
