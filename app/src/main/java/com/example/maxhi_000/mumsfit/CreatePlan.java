package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.interpolator.linear;

public class CreatePlan extends AppCompatActivity {

    final Context context = this;

    String namePlan;

    List<String> splits = new ArrayList<String>();
    List<String> exercise = new ArrayList<String>();
    List<String> reps = new ArrayList<String>();
    List<String> start_weight = new ArrayList<String>();
    List<String> e_split = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creating_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        setTitle(this.namePlan);

        createSplitButton();
        FloatingActionButton readyButton = (FloatingActionButton) findViewById(R.id.readyButton);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Speichern");
                alertDialogBuilder
                        .setMessage("Wollen Sie den Plan speichern?\nDer Plan kann später noch bearbeitet werden.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);

                                    TrainPlanDataSource dataSource = new TrainPlanDataSource(context);
                                    String forDB = "[" + namePlan + "]";

                                    dataSource.open();
                                    String CREATE_NEW_TABLE = "CREATE TABLE " + forDB +
                                            "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            "exercise" + " TEXT NOT NULL, " +
                                            "reps" + " TEXT NOT NULL, "+
                                            "start_weight" + " REAL NOT NULL);";
                                    db.execSQL(CREATE_NEW_TABLE);
                                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                    db.execSQL("INSERT INTO " + TrainPlanDbHelper.TABLE_DETAILS + " (plan, trainings, date_create, date_last) VALUES ('"+forDB+"' , 0,'"+date+"', '-')");
                                    dataSource.close();
                                }finally {
                                    if (db != null)
                                        db.close();
                                }



                                Intent i = new Intent(CreatePlan.this, MainActivity.class);
                                startActivity(i);
                                finish();
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

    public void createSplitButton(){
        Button splitButton = new Button(this);
        splitButton.setText("Trainingstag hinzufügen");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(splitButton, lp);
        splitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Trainingstag hinzufügen");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(input);

                alertDialogBuilder
                        .setMessage("Name des Trainingstages:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                splits.add(input.getText().toString());
                                redrawGUI();
                                dialog.cancel();
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

        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(exerciseButton, lp);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Trainingstag hinzufügen");

                final EditText e_name = new EditText(context);
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(e_name);

                final EditText e_reps = new EditText(context);
                e_reps.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(e_name);

                final EditText e_sw = new EditText(context);
                e_name.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(e_name);

                alertDialogBuilder
                        .setMessage("Neue Übung:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                splits.add(e_name.getText().toString());
                                redrawGUI();
                                dialog.cancel();
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

    public void redrawGUI(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        if(((LinearLayout) ll).getChildCount() > 0)
            ((LinearLayout) ll).removeAllViews();

        for(int i = 0; i < splits.size(); i++){
            TextView textView = new TextView(this);
            textView.setText(splits.get(i));
            ll.addView(textView);

            //insert exercises here..

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

        alertDialogBuilder.setTitle("Abbrechen");
        alertDialogBuilder
                .setMessage("Plan anlegen wirklich beenden? \nAlle Änderungen gehen verloren")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(CreatePlan.this, MainActivity.class);
                        startActivity(i);
                        finish();
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

    public String getNamePlan() {
        return namePlan;
    }

    public void setNamePlan(String namePlan) {
        this.namePlan = namePlan;
    }

}


