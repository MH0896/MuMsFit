package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private String eingabe;

    private CustomListAdapter adapter;

    private TrainPlanDataSource dataSource;

    ArrayList<String> arrTblNames = new ArrayList<String>();
    ArrayList<Integer> selected = new ArrayList<Integer>();

    FloatingActionButton addPlan;
    ListView ViewPlan;

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
    }

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
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT name FROM plan", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    arrTblNames.add(c.getString(c.getColumnIndex("name")));
                    c.moveToNext();
                }
            }
            dataSource.close();

            ViewPlan = (ListView) findViewById(R.id.viewPlans);
            adapter = new CustomListAdapter(MainActivity.this, R.layout.custom_list, R.id.textView, arrTblNames);
            ViewPlan.setAdapter(adapter);

            ViewPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle temp = new Bundle();
                    temp.putString("param", arrTblNames.get(position));
                    Intent i = new Intent(MainActivity.this, ViewPlanActivity.class);
                    i.putExtras(temp);
                    startActivity(i);
                    finish();
                }
            });

            ViewPlan.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            ViewPlan.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                private int nr = 0;

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    MenuItem item;
                    if(nr == 1 && arrTblNames.size()==1){
                        item = menu.findItem(R.id.item_delete);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_share);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_select_all);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_edit);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_details);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_analyze);
                        item.setVisible(true);
                    }
                    else if (nr == 1){
                        item = menu.findItem(R.id.item_delete);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_share);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_select_all);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_edit);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_details);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_analyze);
                        item.setVisible(true);
                        return true;
                    } else if(nr > 1 && nr!=arrTblNames.size()) {
                        item = menu.findItem(R.id.item_details);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_analyze);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_edit);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_delete);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_share);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_select_all);
                        item.setVisible(true);
                        return true;
                    }else if(nr == arrTblNames.size()){
                        item = menu.findItem(R.id.item_select_all);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_details);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_analyze);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_edit);
                        item.setVisible(false);
                        item = menu.findItem(R.id.item_delete);
                        item.setVisible(true);
                        item = menu.findItem(R.id.item_share);
                        item.setVisible(true);
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.clearSelection();
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    nr = 0;
                    selected.clear();
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.contextual_menu, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_delete:
                            DeleteClick(selected);
                            nr = 0;
                            mode.finish();
                            return true;
                        case R.id.item_analyze:
                            nr = 0;
                            adapter.clearSelection();
                            mode.finish();
                            AnalyzeClick(selected);
                            selected.clear();
                            return true;
                        case R.id.item_details:
                            nr = 0;
                            adapter.clearSelection();
                            mode.finish();
                            DetailsClick(selected);
                            selected.clear();
                            return true;
                        case R.id.item_edit:
                            nr = 0;
                            mode.finish();
                            EditClick(selected);
                            return true;
                        case R.id.item_select_all:
                            for (int i=0; i < ViewPlan.getAdapter().getCount(); i++) {
                                ViewPlan.setItemChecked(i, true);
                                nr = i + 1;
                            }
                            return true;
                        case R.id.item_share:
                            nr = 0;
                            adapter.clearSelection();
                            mode.finish();
                            ShareClick(selected);
                            selected.clear();
                            return true;
                    }
                    return false;
                }

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    if (checked) {
                        nr++;
                        adapter.setNewSelection(position, checked);
                        selected.add(position);
                    } else {
                        nr--;
                        adapter.removeSelection(position);
                        int i = selected.indexOf(position);
                        selected.remove(i);
                    }
                    mode.setTitle(nr + " selected");

                    mode.invalidate();
                }
            });

            ViewPlan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    ViewPlan.setItemChecked(position, !adapter.isPositionChecked(position));
                    return false;
                }
            });
        } finally {
            if (db != null)
                db.close();
        }

        addPlan = (FloatingActionButton) findViewById(R.id.addPlan);
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setId(R.id.u_view);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle(R.string.alert_createPlanTitle); //String: Create a new plan
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setId(R.id.calabash);
                alertDialogBuilder.setView(input);

                alertDialogBuilder
                        .setMessage(R.string.alert_createPlanName) //String: Name of your plan
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eingabe = input.getText().toString();
                                boolean exists = false;
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                                    dataSource = new TrainPlanDataSource(context);
                                    dataSource.open();

                                    Cursor c = db.rawQuery("SELECT name FROM plan", null);

                                    if (c.moveToFirst()) {
                                        while (!c.isAfterLast()) {
                                            if (eingabe.equalsIgnoreCase(c.getString(c.getColumnIndex("name")))) {
                                                exists = true;
                                            }
                                            c.moveToNext();
                                        }
                                    }
                                    dataSource.close();
                                } finally {
                                    if (db != null)
                                        db.close();
                                }

                                if (!exists) {
                                    String returned = checkEingabe(eingabe);
                                    if(returned == null){
                                        Toast.makeText(context, R.string.toast_errorEnterName, Toast.LENGTH_SHORT).show(); //String: Please enter a name
                                    }else {
                                        Plan newPlan = new Plan(returned);
                                        Bundle temp = new Bundle();
                                        temp.putString("param", newPlan.getName());
                                        Intent i = new Intent(MainActivity.this, CreatePlanActivity.class);
                                        i.putExtras(temp);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(context, R.string.toast_errorNameExists, Toast.LENGTH_SHORT).show(); //String: Name already exists
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_startscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                SettingsClick();
                return true;
        }
        return false;
    }

    private boolean canClose = false;
    private static int zeit = 2000;

    @Override
    public void onBackPressed() {
        if (canClose) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, R.string.toast_exitApp, Toast.LENGTH_SHORT).show(); //String: Press again to exit
        canClose = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canClose = false;
            }
        }, zeit);

    }
    //details ansehen
    public void DeleteClick(final ArrayList<Integer> items){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

        alertDialogBuilder.setTitle(R.string.alert_deletePlanTitle); //String: delete for sure?

        alertDialogBuilder
                .setMessage(R.string.alert_deletePlanMessage) //String: really delete permanently?
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = null;
                            try{
                                db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                                dataSource = new TrainPlanDataSource(context);
                                dataSource.open();

                                for(int i = 0; i < items.size(); i++){
                                    String temp = arrTblNames.get(items.get(i));

                                    Cursor c = db.rawQuery("SELECT plan_id FROM plan", null);
                                    c.moveToFirst();
                                    String id = c.getString(c.getColumnIndex("plan_id"));

                                    db.execSQL("DELETE FROM plan WHERE name='"+temp+"'");
                                    db.execSQL("DELETE FROM uebung WHERE plan_id='"+id+"'");

                                    c = db.rawQuery("SELECT uebung_id FROM uebung", null);

                                    if (c.moveToFirst()) {
                                        while (!c.isAfterLast()) {
                                            String temps = c.getString(c.getColumnIndex("uebung_id"));
                                            db.execSQL("DELETE FROM gewicht WHERE uebung_id='"+temps+"'");
                                            c.moveToNext();
                                        }
                                    }
                                }

                                dataSource.close();
                            }finally {
                                if (db != null)
                                    db.close();
                            }
                            finish();
                            startActivity(getIntent());
                        }})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DetailsClick(ArrayList<Integer> items){
        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            String date_create = "default";
            String date_last = "default";
            Cursor c = db.rawQuery("SELECT date_create,date_last FROM plan WHERE name='"+ arrTblNames.get(items.get(0)) +"'", null);
            if(c.moveToFirst()) {
                date_create = c.getString(0);
                date_last = c.getString(1);
                c.close();
            }

            dataSource.close();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setTitle(arrTblNames.get(items.get(0)));

            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.alert_planInfoCreated) + " " + date_create + "\n" + getResources().getString(R.string.alert_planInfoExecuted) + " " + date_last)
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

    public void ShareClick(ArrayList<Integer> items){
        //Aufruf Zeile 166
    }

    public void EditClick(final ArrayList<Integer> items){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle(R.string.alert_editPlanTitle);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(arrTblNames.get(items.get(0)));
        input.setId(R.id.calabash);
        alertDialogBuilder.setView(input);

        alertDialogBuilder
                .setMessage(R.string.alert_editPlanMessage)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = input.getText().toString();
                        String returned = checkEingabe(temp);
                        if(returned == null){
                            Toast.makeText(context, R.string.toast_errorEnterName, Toast.LENGTH_SHORT).show();
                        }else {
                            SQLiteDatabase db = null;
                            try {
                                db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                                dataSource = new TrainPlanDataSource(context);
                                dataSource.open();
                                db.execSQL("UPDATE plan SET name='"+returned+"' WHERE name='"+arrTblNames.get(items.get(0))+"'");

                                dataSource.close();
                            }finally {
                                if (db != null)
                                    db.close();
                            }
                            finish();
                            startActivity(getIntent());
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

    public void AnalyzeClick(ArrayList<Integer> items){
        //Aufruf Zeile 139
    }

    public void SettingsClick(){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
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