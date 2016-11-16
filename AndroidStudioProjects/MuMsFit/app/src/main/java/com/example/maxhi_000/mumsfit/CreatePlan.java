package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CreatePlan extends AppCompatActivity {

    final Context context = this;

    String namePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creating_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        setTitle(this.namePlan);

        createGUI();
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

                                    dataSource.open();
                                    String CREATE_NEW_TABLE = "CREATE TABLE " + namePlan +
                                            "(" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            "exercise" + " TEXT NOT NULL, " +
                                            "reps" + " TEXT NOT NULL, "+
                                            "start_weight" + " REAL NOT NULL);";
                                    db.execSQL(CREATE_NEW_TABLE);

                                   // db.execSQL("INSERT INTO " + TABLE_DETAILS+ "(NAME, A, B, SCALEFACTOR, FEASTING ) VALUES ('Everest 1830',6377276.30,6356075.4,0.9996,500000)");
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

    public void createGUI(){

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


