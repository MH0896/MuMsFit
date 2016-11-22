package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreatePlan extends AppCompatActivity {

    final Context context = this;

    String namePlan;

    List<String> splits = new ArrayList<String>();
    List<String> exercise = new ArrayList<String>();
    List<String> reps = new ArrayList<String>();
    List<String> start_weight = new ArrayList<String>();
    List<String> e_split = new ArrayList<String>();
    String currentPlan = "";

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
                                            "start_weight" + " TEXT NOT NULL, " +
                                            "split" + " TEXT NOT NULL);";
                                    db.execSQL(CREATE_NEW_TABLE);
                                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                    db.execSQL("INSERT INTO details (plan, trainings, date_create, date_last) VALUES ('"+forDB+"' , 0,'"+date+"', '-')");

                                    for(int i  = 0; i < exercise.size(); i++){
                                        db.execSQL("INSERT INTO " + namePlan + " (exercise, reps, start_weight, split) VALUES ('"+exercise.get(i)+"', '"+reps.get(i)+"', '"+start_weight.get(i)+"', '"+e_split.get(i)+"')");
                                    }

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
                input.setId(R.id.calabash);

                alertDialogBuilder
                        .setMessage("Name des Trainingstages:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String eingabe = input.getText().toString().toUpperCase();
                                String returned = checkEingabe(eingabe);
                                if(returned == null){
                                    Toast.makeText(context, "Bitte einen Namen eingeben", Toast.LENGTH_SHORT).show();
                                }else {
                                    splits.add(eingabe);
                                    redrawGUI();
                                    dialog.cancel();
                                }
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

    public void createExerciseButton(String t_plan){
        Button exerciseButton = new Button(this);
        exerciseButton.setText("Übung hinzufügen");
        final String temp_plan = t_plan;
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
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

                alertDialogBuilder.setView(layout);

                alertDialogBuilder
                        .setMessage("Neue Übung:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                e_split.add(temp_plan);
                                exercise.add(e_name.getText().toString());
                                reps.add(e_reps.getText().toString());
                                start_weight.add(e_sw.getText().toString());
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

    public void createDeleteButton(final int toDel, LinearLayout tempLL){
        TextView delete = new TextView(this);
        String sourceString = "<b>X</b>";
        delete.setText(Html.fromHtml(sourceString));
        delete.setGravity(Gravity.LEFT);
        delete.setPadding(5,1,5,1);
        delete.setTextSize(pxFromDp(12, context));

        tempLL.addView(delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                exercise.remove(toDel);
                reps.remove(toDel);
                start_weight.remove(toDel);
                e_split.remove(toDel);
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
        deleteS.setTextSize(pxFromDp(12, context));

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
            while(k < exercise.size() && toTest){
                if(currentPlan.equals((e_split.get(k)))){
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

            for(int j = 0; j < exercise.size(); j++){
                tempLL = new LinearLayout(this);
                if(currentPlan.equals(e_split.get(j))){
                    createDeleteButton(j, tempLL);
                    TextView exerView = new TextView(this);
                    exerView.setPadding(10,2,10,2);

                    String toShow = exercise.get(j) + " Reps: " + reps.get(j) + " Startgewicht: " + start_weight.get(j);
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

    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public String checkEingabe(String input){
        if(input.isEmpty()){
            return null;
        }
        String newInput = input.trim();
        if(newInput == "" || newInput.isEmpty()){
            return null;
        }

        return newInput;
    }

}


