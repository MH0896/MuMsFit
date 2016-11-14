package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrainPlanDbHelper extends SQLiteOpenHelper {

    private final static  String LOG_TAG = TrainPlanDbHelper.class.getSimpleName();

    public String tableName;
    public static final String DB_NAME = "plans.db";
    public static final int DB_VERSION = 1;

    public static String TABLE_PLAN = "testPlan";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Exercise = "exercise";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_START_WEIGHT = "start_weight";

    public String SQL_CREATE = "CREATE TABLE " + TABLE_PLAN +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_Exercise + " TEXT NOT NULL, " +
            COLUMN_REPS + " STRING NOT NULL,"+
            COLUMN_START_WEIGHT + "REAL NOT NULL);";

    public TrainPlanDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
