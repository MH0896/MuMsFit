package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private String eingabe;

    private CustomListAdapter adapter;

    private TrainPlanDataSource dataSource;

    ArrayList<String> arrTblNames = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//why?!?!??!?!?!?!?!?!?
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
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.clearSelection();
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub

                    nr = 0;
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.contextual_menu, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // TODO Auto-generated method stub
                    switch (item.getItemId()) {

                        case R.id.item_delete:
                            nr = 0;
                            adapter.clearSelection();
                            mode.finish();
                            return true;
                    }
                    return false;
                }

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    // TODO Auto-generated method stub
                    if (checked) {
                        nr++;
                        adapter.setNewSelection(position, checked);
                    } else {
                        nr--;
                        adapter.removeSelection(position);
                    }
                    mode.setTitle(nr + " selected");

                }
            });

            ViewPlan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    // TODO Auto-generated method stub

                    ViewPlan.setItemChecked(position, !adapter.isPositionChecked(position));
                    return false;
                }
            });
        } finally {
            if (db != null)
                db.close();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Neuen Plan anlegen");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
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
                                    Bundle temp = new Bundle();
                                    temp.putString("param", eingabe);
                                    Intent i = new Intent(MainActivity.this, CreatePlan.class);
                                    i.putExtras(temp);
                                    startActivity(i);
                                    finish();
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

