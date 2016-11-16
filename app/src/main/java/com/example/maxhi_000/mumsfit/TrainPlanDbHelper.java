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

    public static final String COLUMN_Id = "_id";
    public static final String COLUMN_PLAN = "plan";
    public static final String COLUMN_TRAININGS = "trainings";
    public static final String COLUMN_DATE_CREATE = "date_create";
    public static final String COLUMN_DATE_LAST = "date_last";

    public static final String TABLE_DETAILS = "details";

    public String SQL_CREATE = "CREATE TABLE " + TABLE_DETAILS +
            "(" + COLUMN_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLAN + " TEXT NOT NULL, " +
            COLUMN_TRAININGS + " INTEGER NOT NULL,"+
            COLUMN_DATE_CREATE + " TEXT NOT NULL,"+
            COLUMN_DATE_LAST + " TEXT NOT NULL);";

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