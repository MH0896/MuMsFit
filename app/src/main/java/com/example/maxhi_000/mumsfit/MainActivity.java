package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private String eingabe;

    private CustomListAdapter adapter;

    private TrainPlanDataSource dataSource;

    ArrayList<String> arrTblNames = new ArrayList<String>();
    ArrayList<Integer> selected = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = null;
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                c.moveToNext();
                c.moveToNext();
                c.moveToNext();
                while (!c.isAfterLast()) {
                    arrTblNames.add(c.getString(c.getColumnIndex("name")));
                    c.moveToNext();
                }
            }
            dataSource.close();

            final ListView ViewPlan = (ListView) findViewById(R.id.viewPlans);
            adapter = new CustomListAdapter(MainActivity.this, R.layout.custom_list, R.id.textView, arrTblNames);
            ViewPlan.setAdapter(adapter);

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
                            adapter.clearSelection();
                            mode.finish();
                            EditClick(selected);
                            selected.clear();
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

        FloatingActionButton addPlan = (FloatingActionButton) findViewById(R.id.addPlan);
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Neuen Plan anlegen");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setId(R.id.calabash);
                alertDialogBuilder.setView(input);

                alertDialogBuilder
                        .setMessage("Name Ihres Planes:")
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

                                    Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                                    if (c.moveToFirst()) {
                                        c.moveToNext();
                                        c.moveToNext();
                                        c.moveToNext();
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
                                        Toast.makeText(context, "Bitte einen Namen eingeben", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Bundle temp = new Bundle();
                                        temp.putString("param", eingabe);
                                        Intent i = new Intent(MainActivity.this, CreatePlanActivity.class);
                                        i.putExtras(temp);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(context, "Name schon vergeben. Bitte wählen Sie einen anderen!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contextual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private boolean canClose = false;
    private static int zeit = 2000;

    @Override
    public void onBackPressed() {
        if (canClose) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, "Erneut Klicken, um zu beenden!", Toast.LENGTH_SHORT).show();
        canClose = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canClose = false;
            }
        }, zeit);

    }

    public void DeleteClick(final ArrayList<Integer> items){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

        alertDialogBuilder.setTitle("Wirklich löschen?");

        alertDialogBuilder
                .setMessage("Wollen Sie wirklich unwiderruflich löschen?")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = null;
                            try{
                                db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                                dataSource = new TrainPlanDataSource(context);
                                dataSource.open();

                                for(int i = 0; i < items.size(); i++){
                                    db.execSQL("DROP TABLE IF EXISTS ["+arrTblNames.get(items.get(i)) +"]");
                                    db.delete("details", "plan = ?", new String[] { "["+ arrTblNames.get(items.get(i)) +"]" });
                                }

                                dataSource.close();
                            }finally {
                                if (db != null)
                                    db.close();
                            }
                            finish();
                            startActivity(getIntent());
                        }})
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
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

            String trainings = "default";
            String date_create = "default";
            String date_last = "default";
            Cursor c = db.rawQuery("SELECT trainings,date_create,date_last FROM details WHERE plan='["+ arrTblNames.get(items.get(0)) +"]'", null);
            if(c.moveToFirst()) {
                trainings = c.getString(0);
                date_create = c.getString(1);
                date_last = c.getString(2);
                c.close();
            }

            dataSource.close();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setTitle(arrTblNames.get(items.get(0)));

            alertDialogBuilder
                    .setMessage("Anzahl der Einheiten: "+trainings+"\nErstellt am: "+date_create+"\nZuletzt durchgeführt am: "+date_last)
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

    public void EditClick(ArrayList<Integer> items){
        //Aufruf Zeile 153
    }

    public void AnalyzeClick(ArrayList<Integer> items){
        //Aufruf Zeile 139
    }
}