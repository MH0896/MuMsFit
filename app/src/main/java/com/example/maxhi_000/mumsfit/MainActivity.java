package com.example.maxhi_000.mumsfit;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public final Context context = this;

    private CustomListAdapter adapter;

    private TrainPlanDataSource dataSource;

    public int nr = 0;
  
    public ArrayList<String> arrTblNames = new ArrayList<String>();
    public ArrayList<Integer> selected = new ArrayList<Integer>();

    public ListView ViewPlan;

    private boolean canClose = false;
    private static int zeit = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "MyPrefs", MODE_PRIVATE);

        String themeName = prefs.getString("Theme", "Default");
        if ("BlackTheme".equals(themeName)) {
            setTheme(R.style.BlackTheme);
        } else if ("LightTheme".equals(themeName)) {
            setTheme(R.style.LightTheme);
        }else if("Default".equals(themeName)){
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

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                   return enableDisableContextualMenu(nr, arrTblNames, menu);
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
                    return actionModeItemClicked(mode, item);
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
                    mode.setTitle(nr + " " + getResources().getString(R.string.actionMode_selected));

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
        addPlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.addPlan:
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setTitle(R.string.alert_createPlanTitle);
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setId(R.id.calabash);
            alertDialogBuilder.setView(input);

            alertDialogBuilder
                    .setMessage(R.string.alert_createPlanName)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String eingabe = input.getText().toString();
                            String returned = checkEingabe(eingabe);
                            if(returned == null) {
                                Toast.makeText(context, R.string.toast_errorEnterName, Toast.LENGTH_SHORT).show();
                            }else{
                                boolean exists = false;
                                SQLiteDatabase db = null;
                                try {
                                    db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                                    dataSource = new TrainPlanDataSource(context);
                                    dataSource.open();

                                    Cursor c = db.rawQuery("SELECT name FROM plan", null);

                                    if (c.moveToFirst()) {
                                        while (!c.isAfterLast()) {
                                            if (returned.equalsIgnoreCase(c.getString(c.getColumnIndex("name")))) {
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
                                    Plan newPlan = new Plan(returned);
                                    Bundle temp = new Bundle();
                                    temp.putString("param", newPlan.getName());
                                    Intent i = new Intent(MainActivity.this, CreatePlanActivity.class);
                                    i.putExtras(temp);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(context, R.string.toast_errorNameExists, Toast.LENGTH_SHORT).show();
                                }
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
            break;
        default:
            break;
        }

    }

    public boolean enableDisableContextualMenu(int nr, ArrayList<String> arrTblNames, Menu menu){
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
            return true;
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

    public boolean actionModeItemClicked(ActionMode mode, MenuItem item){
        switch (item.getItemId()) {
            case R.id.item_delete:
                deleteClick(selected);
                nr = 0;
                mode.finish();
                return true;
            case R.id.item_analyze:
                nr = 0;
                adapter.clearSelection();
                mode.finish();
                analyzeClick(selected);
                selected.clear();
                return true;
            case R.id.item_details:
                nr = 0;
                adapter.clearSelection();
                mode.finish();
                detailsClick(selected);
                selected.clear();
                return true;
            case R.id.item_edit:
                nr = 0;
                mode.finish();
                editClick(selected);
                return true;
            case R.id.item_select_all:
                for (int i=0; i < ViewPlan.getAdapter().getCount(); i++) {
                    ViewPlan.setItemChecked(i, true);
                    nr = i + 1;
                }
                return true;
            case R.id.item_share:
                if(isStoragePermissionGranted()) {
                    nr = 0;
                    mode.finish();
                    adapter.clearSelection();
                    shareClick(selected);
                    selected.clear();
                }
                return true;
            default:
                break;
        }
        return false;
    }

    public static String checkEingabe(String input){
        if(input.isEmpty()){
            return null;
        }
        String newInput = input.trim();
        if("".equals(newInput) || newInput.isEmpty()){
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
                settingsClick();
                return true;
            case R.id.item_import:
                showChooser();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (canClose) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, R.string.toast_exitApp, Toast.LENGTH_SHORT).show();
        canClose = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canClose = false;
            }
        }, zeit);

    }

    public void deleteClick(final ArrayList<Integer> items){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

        alertDialogBuilder.setTitle(R.string.alert_deletePlanTitle);

        alertDialogBuilder
                .setMessage(R.string.alert_deletePlanMessage)
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

    public void detailsClick(ArrayList<Integer> items){
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

    public void shareClick(ArrayList<Integer> items){
        String planName = arrTblNames.get(items.get(0));

        File savedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),planName+"_export.txt");
        if(!savedFile.exists()){
            try{
                savedFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(context, e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        OutputStream outStream;
        ArrayList<Uebung> uebung = new ArrayList<Uebung>();
        try {
            outStream = new FileOutputStream(savedFile);
            outStream.write(planName.getBytes());
            outStream.write("\n".getBytes());

            SQLiteDatabase db = null;
            try {
                db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
                dataSource = new TrainPlanDataSource(context);
                dataSource.open();

                Cursor c = db.rawQuery("SELECT uebung.name, uebung.reps, uebung.start, uebung.split " +
                        "FROM plan, uebung WHERE plan.plan_id = uebung.plan_id AND plan.name='"+
                        planName+"'", null);

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
            }finally {
                if (db != null)
                    db.close();
            }

            for(int i = 0; i < uebung.size(); i++){
                String out = uebung.get(i).getName() + "|" + uebung.get(i).getReps() + "|" +
                        uebung.get(i).getStart() + "|" + uebung.get(i).getSplit() + "\n";
                outStream.write(out.getBytes());
            }

            outStream.flush();
            outStream.close();
            Toast.makeText(context,R.string.saved,Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void editClick(final ArrayList<Integer> items){
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

    public void analyzeClick(ArrayList<Integer> items){
        SQLiteDatabase db = null;
        ArrayList<Uebung> uebung = new ArrayList<Uebung>();
        try {
            db = this.openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            dataSource = new TrainPlanDataSource(context);
            dataSource.open();

            Cursor c = db.rawQuery("SELECT uebung.name" +
                    " FROM plan, uebung WHERE plan.plan_id = uebung.plan_id AND plan.name='"+
                    arrTblNames.get(items.get(0))+"'", null);

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String name = (c.getString(c.getColumnIndex("name")));
                    uebung.add(new Uebung(name, "gh", 5, "sg"));
                    c.moveToNext();
                }
            }
            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }

        if(uebung.size() == 0){
            Toast.makeText(context, R.string.errorNoExcercisesAnalyse, Toast.LENGTH_SHORT).show();
        }else {
            Bundle temp = new Bundle();
            temp.putString("param", arrTblNames.get(items.get(0)));
            Intent i = new Intent(MainActivity.this, AnalyzePlanActivity.class);
            i.putExtras(temp);
            startActivity(i);
            finish();
        }
    }

    public void settingsClick(){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,R.string.toast_permissionsGranted, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,R.string.toast_noPermissions, Toast.LENGTH_LONG).show();
        }
    }

    public void importFile(Uri uri){
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> fileContent = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.add(line + System.getProperty("line.separator"));
            }
            inputStream.close();

            createNewPlan(fileContent);

        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void createNewPlan (ArrayList conts){
        SQLiteDatabase db = null;
        try {
            db = openOrCreateDatabase("plans.db", MODE_PRIVATE, null);
            TrainPlanDataSource dataSource = new TrainPlanDataSource(context);

            Plan plan = new Plan(conts.get(0).toString());

            dataSource.open();
            db.execSQL("INSERT INTO plan (name, date_create, date_last) " +
                    "VALUES ('"+plan.getName()+"','"+plan.getDateCreate()+
                    "', '"+plan.getDateLast()+"')");

            Cursor c = db.rawQuery("SELECT plan_id FROM plan WHERE name='" + plan.getName() + "'", null);
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("plan_id"));

            for(int i = 1; i < conts.size(); i++){
                String[] parts = conts.get(i).toString().split("|");
                db.execSQL("INSERT INTO uebung (plan_id, name, reps, start, split)" +
                        " VALUES ('"+id+"', '"+parts[0]+
                        "', '"+parts[1]+"', '"+
                        parts[2]+"', '"+
                        parts[3]+"')");
            }

            dataSource.close();
        }finally {
            if (db != null)
                db.close();
        }
    }

    private void showChooser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        try {
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.fileChooserTitle)),1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.toast_fileChooser, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data!= null) {
                // Get the URI of the selected file
                final Uri uri = data.getData();
                importFile(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
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