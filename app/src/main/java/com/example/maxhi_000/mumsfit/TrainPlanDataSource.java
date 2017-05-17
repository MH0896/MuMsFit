package com.example.maxhi_000.mumsfit;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrainPlanDataSource {
    private final static  String LOG_TAG = TrainPlanDataSource.class.getSimpleName();

    private TrainPlanDbHelper dbHelper;
    private boolean opened = false;
    private boolean closed = false;

    public TrainPlanDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new TrainPlanDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        dbHelper.getWritableDatabase();
//        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
        opened = true;
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
        closed = true;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isClosed() {
        return closed;
    }
}