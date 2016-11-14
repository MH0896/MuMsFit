package com.example.maxhi_000.mumsfit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TrainPlanDataSource {

    private final static  String LOG_TAG = TrainPlanDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private TrainPlanDbHelper dbHelper;


    private String[] columns = {
            TrainPlanDbHelper.COLUMN_ID,
            TrainPlanDbHelper.COLUMN_Exercise,
            TrainPlanDbHelper.COLUMN_REPS,
            TrainPlanDbHelper.COLUMN_START_WEIGHT
    };

    public TrainPlanDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new TrainPlanDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public TrainPlan createTrainPlan(String exercise, String reps, double start_weight){
        ContentValues values = new ContentValues();
        values.put(TrainPlanDbHelper.COLUMN_Exercise, exercise);
        values.put(TrainPlanDbHelper.COLUMN_REPS, reps);
        values.put(TrainPlanDbHelper.COLUMN_START_WEIGHT, start_weight);

        long insertId = database.insert(TrainPlanDbHelper.TABLE_PLAN, null, values);

        Cursor cursor = database.query(TrainPlanDbHelper.TABLE_PLAN, columns,
                TrainPlanDbHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        TrainPlan trainingsPlan = cursorToTrainPlan(cursor);
        cursor.close();

        return trainingsPlan;
    }

    private TrainPlan cursorToTrainPlan(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_ID);
        int idExercise = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_Exercise);
        int idReps = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_REPS);
        int idStart_weight = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_START_WEIGHT);

        String exercise = cursor.getString(idExercise);
        String reps = cursor.getString(idReps);
        double start_weight = cursor.getDouble(idStart_weight);
        long id = cursor.getLong(idIndex);

        TrainPlan plan = new TrainPlan(exercise, reps, start_weight, id);
        return plan;
    }

    public List<TrainPlan> getAllTrainPlan(){
        List<TrainPlan> tpList = new ArrayList<>();

        Cursor cursor = database.query(TrainPlanDbHelper.TABLE_PLAN, columns,
                null, null, null, null, null);

        cursor.moveToFirst();
        TrainPlan tempPlan;
        while(!cursor.isAfterLast()){
            tempPlan = cursorToTrainPlan(cursor);
            tpList.add(tempPlan);
            cursor.moveToNext();
        }

        cursor.close();
        return tpList;
    }

}
