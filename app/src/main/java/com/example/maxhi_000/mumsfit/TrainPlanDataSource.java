package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrainPlanDataSource {
// new
    private final static  String LOG_TAG = TrainPlanDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private TrainPlanDbHelper dbHelper;


    private String[] columns = {
            TrainPlanDbHelper.COLUMN_DATE_CREATE,
            TrainPlanDbHelper.COLUMN_DATE_LAST,
            TrainPlanDbHelper.COLUMN_START,
            TrainPlanDbHelper.COLUMN_REPS,
            TrainPlanDbHelper.COLUMN_GEWICHT,
            TrainPlanDbHelper.COLUMN_GID,
            TrainPlanDbHelper.COLUMN_PId,
            TrainPlanDbHelper.COLUMN_PNAME,
            TrainPlanDbHelper.COLUMN_UID,
            TrainPlanDbHelper.COLUMN_UNAME,
            TrainPlanDbHelper.COLUMN_SPLIT
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

 /*   public TrainPlan createTrainPlan(String plan, int trainings, String date_create, String date_last){
        ContentValues values = new ContentValues();
        values.put(TrainPlanDbHelper.COLUMN_PLAN, plan);
        values.put(TrainPlanDbHelper.COLUMN_TRAININGS, trainings);
        values.put(TrainPlanDbHelper.COLUMN_DATE_CREATE, date_create);
        values.put(TrainPlanDbHelper.COLUMN_DATE_LAST, date_last);
        long insertId = database.insert(TrainPlanDbHelper.TABLE_DETAILS, null, values);
        Cursor cursor = database.query(TrainPlanDbHelper.TABLE_DETAILS, columns,
                TrainPlanDbHelper.COLUMN_Id + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        TrainPlan trainingsPlan = cursorToTrainPlan(cursor);
        cursor.close();
        return trainingsPlan;
    } */

  /*  private TrainPlan cursorToTrainPlan(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_Id);
        int idPlan = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_PLAN);
        int idTrainings = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_TRAININGS);
        int idDate_create = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_DATE_CREATE);
        int idDate_last = cursor.getColumnIndex(TrainPlanDbHelper.COLUMN_DATE_LAST);

        String plan = cursor.getString(idPlan);
        Integer trainings = cursor.getInt(idTrainings);
        String date_create = cursor.getString(idDate_create);
        long id = cursor.getLong(idIndex);
        String date_last = cursor.getString(idDate_last);

        TrainPlan tempplan = new TrainPlan(plan, trainings, date_create, date_last, id);
        return tempplan;
    }*/

}