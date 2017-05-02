package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
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

public class EditPlanActivity  extends AppCompatActivity {
// new
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

                alertDialogBuilder.setTitle("Löschen");
                alertDialogBuilder
                        .setMessage("Übung unwiderruflich löschen?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
                                    db.execSQL("DELETE FROM uebung WHERE name='"+exercises.get(toDel)+"'");

                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }

                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
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
                e_name.setText(exercises.get(toEdit));
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                e_name.setId(R.id.c_name);
                layout.addView(e_name);

                final EditText e_reps = new EditText(context);
                e_reps.setText(reps.get(toEdit));
                e_reps.setInputType(InputType.TYPE_CLASS_TEXT);
                e_reps.setId(R.id.c_reps);
                layout.addView(e_reps);

                final EditText e_sw = new EditText(context);
                e_sw.setText(sweight.get(toEdit));
                e_sw.setInputType(InputType.TYPE_CLASS_TEXT);
                e_sw.setId(R.id.c_sw);
                layout.addView(e_sw);
                alertDialogBuilder.setView(layout);

                alertDialogBuilder.setTitle("Bearbeiten");
                alertDialogBuilder
                        .setMessage("Übung bearbeiten:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
                                    db.execSQL("UPDATE uebung SET name='"+ e_name.getText().toString() +"' , reps='"+ e_reps.getText().toString() +"' , start='"+ e_sw.getText().toString() +"' , split='"+ split.get(toEdit) +"' WHERE name='"+ exercises.get(toEdit) +"' ");

                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }

                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
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
        exerciseButton.setText("Übung hinzufügen");
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearView);;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(exerciseButton, lp);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Übung hinzufügen");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText e_name = new EditText(context);
                e_name.setHint("Name");
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                e_name.setId(R.id.c_name);
                layout.addView(e_name);

                final EditText e_reps = new EditText(context);
                e_reps.setHint("Wiederholungen");
                e_reps.setInputType(InputType.TYPE_CLASS_TEXT);
                e_reps.setId(R.id.c_reps);
                layout.addView(e_reps);

                final EditText e_sw = new EditText(context);
                e_sw.setHint("Startgewicht");
                e_sw.setInputType(InputType.TYPE_CLASS_TEXT);
                e_sw.setId(R.id.c_sw);
                layout.addView(e_sw);

                final EditText e_split = new EditText(context);
                e_split.setHint("Split");
                e_split.setInputType(InputType.TYPE_CLASS_TEXT);
                e_split.setId(R.id.c_split);
                layout.addView(e_split);

                alertDialogBuilder.setView(layout);

                alertDialogBuilder
                        .setMessage("Neue Übung:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

                                    dataSource.open();
                                    Cursor c = db.rawQuery("SELECT plan_id FROM plan WHERE name='"+namePlan+"'", null);
                                    c.moveToFirst();
                                    String id = c.getString(c.getColumnIndex("plan_id"));
                                    db.execSQL("INSERT INTO uebung (plan_id, name, reps, start, split) VALUES ('"+id+"', '"+e_name.getText().toString()+"', '"+e_reps.getText().toString()+"', '"+e_sw.getText().toString()+"', '"+e_split.getText().toString().toUpperCase()+"')");

                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
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
            LinearLayout tempLL = new LinearLayout(this);

            createExerciseDeleteButton(i, tempLL);
            createExerciseEditButton(i, tempLL);
            TextView exerView = new TextView(this);
            exerView.setPadding(10,2,10,2);

            String toShow = exercises.get(i) + " Reps: " + reps.get(i) + " Startgewicht: " + sweight.get(i);
            exerView.setText(toShow);
            tempLL.addView(exerView);
            ll.addView(tempLL);
        }

        createExerciseButton();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
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
}
