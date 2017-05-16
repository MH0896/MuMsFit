package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrainPlanDbHelper extends SQLiteOpenHelper {

    private final static  String LOG_TAG = TrainPlanDbHelper.class.getSimpleName();

    public static final String DB_NAME = "plans.db";
    public static final int DB_VERSION = 1;

    public static final String COLUMN_PId = "plan_id";
    public static final String COLUMN_PNAME = "name";
    public static final String COLUMN_DATE_CREATE = "date_create";
    public static final String COLUMN_DATE_LAST = "date_last";

    public static final String TABLE_PLAN = "plan";

    public static final String COLUMN_UID = "uebung_id";
    public static final String COLUMN_UNAME = "name";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_SPLIT = "split";

    public static final String TABLE_UEBUNG = "uebung";

    public static final String COLUMN_GID = "gewicht_id";
    public static final String COLUMN_GEWICHT = "gewicht";

    public static final String TABLE_GEWICHT = "gewicht";

    public String SQL_CREATE_PLAN = "CREATE TABLE " + TABLE_PLAN +
            "(" + COLUMN_PId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PNAME + " TEXT NOT NULL, " +
            COLUMN_DATE_CREATE + " TEXT NOT NULL, "+
            COLUMN_DATE_LAST + " TEXT NOT NULL);";

    public String SQL_CREATE_UEBUNG = "CREATE TABLE " + TABLE_UEBUNG +
            "(" + COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PId+ " TEXT NOT NULL, " +
            COLUMN_UNAME + " TEXT NOT NULL, "+
            COLUMN_REPS + " TEXT NOT NULL, "+
            COLUMN_START + " REAL NOT NULL, "+
            COLUMN_SPLIT + " TEXT NOT NULL);";

    public String SQL_CREATE_GEWICHT = "CREATE TABLE " + TABLE_GEWICHT +
            "(" + COLUMN_GID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_UID+ " TEXT NOT NULL, " +
            COLUMN_GEWICHT + " REAL NOT NULL);";

    private boolean created = false;

    public TrainPlanDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_PLAN + " angelegt.");
            db.execSQL(SQL_CREATE_PLAN);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_UEBUNG + " angelegt.");
            db.execSQL(SQL_CREATE_UEBUNG);
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_GEWICHT + " angelegt.");
            db.execSQL(SQL_CREATE_GEWICHT);
            created = true;
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        created = true;
    }

    public boolean isCreated() {
        return created;
    }
}