package com.example.trickyalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class SimpleDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarms";
    private static final int DB_VERSION = 1;

    SimpleDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateDatabase(sqLiteDatabase, i, i1);
    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE ALARMS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "IS_ENABLE INTEGER, "
                        + "TIME TEXT, "
                        + "BIAS INTEGER, "
                        + "ON_MONDAY INTEGER, "
                        + "ON_TUESDAY INTEGER, "
                        + "ON_WEDNESDAY INTEGER, "
                        + "ON_THURSDAY INTEGER, "
                        + "ON_FRIDAY INTEGER, "
                        + "ON_SATURDAY INTEGER, "
                        + "ON_SUNDAY INTEGER, "
                        + "IS_REPEATED INTEGER, "
                        + "REPEAT_INTERVAL INTEGER);");
        } else {

        }
    }
}
