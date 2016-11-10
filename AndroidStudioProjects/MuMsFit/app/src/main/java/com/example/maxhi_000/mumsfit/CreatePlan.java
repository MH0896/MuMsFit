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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class CreatePlan extends AppCompatActivity {

    final Context context = this;

    private String namePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creating_plan);

        Bundle params = getIntent().getExtras();
        this.namePlan = params.getString("param");

        //TextView text = (TextView) findViewById(R.id.planName);
        //text.setText(this.namePlan);
        setTitle(this.namePlan);

        createGUI();
        FloatingActionButton readyButton = (FloatingActionButton) findViewById(R.id.readyButton);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Speichern");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Wollen Sie den Plan speichern?\nDer Plan kann später noch bearbeitet werden.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db;
                                db = openOrCreateDatabase(
                                        getNamePlan()+".db"
                                        , SQLiteDatabase.CREATE_IF_NECESSARY
                                        , null
                                );
                                db.setVersion(1);
                                db.setLocale(Locale.getDefault());
                                //db.setLockingEnabled(true);

                                final String CREATE_TABLE_EXERCISES =
                                        "CREATE TABLE tbl_exercises ("
                                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                + "exercise_name TEXT"
                                                + "start_weight REAL"
                                                + "reps TEXT)" + ";";

                                db.execSQL(CREATE_TABLE_EXERCISES);

                                Intent i = new Intent(CreatePlan.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
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

        // set title
        alertDialogBuilder.setTitle("Abbrechen");

        // set dialog message
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
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public String getNamePlan() {
        return namePlan;
    }

    public void setNamePlan(String namePlan) {
        this.namePlan = namePlan;
    }

}


