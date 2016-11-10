package com.example.maxhi_000.mumsfit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private String eingabe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
//das ist ein beispiel und das stinkt
        List plansList = new ArrayList<String>();
        for (int i = 0; i < 10; i++)
        {
            plansList.add("plan"+i);
        }
      //  ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,plansList);
        final ListView ViewPlan = (ListView) findViewById(R.id.viewPlans);

      //  ViewPlan.setAdapter(adapter);

        ListAdapter adapter = new CustomListAdapter(MainActivity.this , R.layout.custom_list , plansList);
        ViewPlan.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Neuen Plan anlegen");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogBuilder.setView(input);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Name Ihres Planes:")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eingabe = input.getText().toString();
                                Bundle temp = new Bundle();
                                temp.putString("param", eingabe);
                                Intent i = new Intent(MainActivity.this, CreatePlan.class);
                                i.putExtras(temp);
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
    public void onBackPressed(){
        if(canClose){
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
        },zeit);

    }
}

