package com.example.trickyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class SimpleDatabaseHelper extends SQLiteOpenHelper {

    private static SimpleDatabaseHelper sInstance;

    private static final String DB_NAME = "alarms.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "ALARMS";

    public static synchronized SimpleDatabaseHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new SimpleDatabaseHelper(context.getApplicationContext());
        return sInstance;
    }

    private SimpleDatabaseHelper(Context context) {
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
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IS_ENABLE INTEGER, "
                    + "TIME INTEGER, "
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
        }
    }

    public void addAlarm(SQLiteDatabase db, Alarm alarm) {
        ContentValues alarmValues = getContentValues(alarm);
        db.insert(TABLE_NAME, null, alarmValues);
    }

    public void updateAlarm(SQLiteDatabase db, Alarm alarm, int position) {
        ContentValues alarmValues = getContentValues(alarm);
        db.update(TABLE_NAME, alarmValues, "_id = ?", new String[] {Integer.toString(position)});
    }

    public void deleteAlarm(SQLiteDatabase db, Alarm alarm, int position) {
        db.delete(TABLE_NAME, "_id = ?", new String[] {Integer.toString(position)});
    }

    public ContentValues getContentValues(Alarm alarm) {
        // Convert Alarm to AlarmDatabase
        AlarmDatabase alarmDatabase = new AlarmDatabase(alarm);

        // Insert AlarmDatabase into DB
        ContentValues alarmValues = new ContentValues();
        alarmValues.put("IS_ENABLE", alarmDatabase.getIsEnable());
        alarmValues.put("TIME", alarmDatabase.getTime());
        alarmValues.put("BIAS", alarmDatabase.getBias());
        alarmValues.put("ON_MONDAY", alarmDatabase.getOnMonday());
        alarmValues.put("ON_TUESDAY", alarmDatabase.getOnTuesday());
        alarmValues.put("ON_WEDNESDAY", alarmDatabase.getOnWednesday());
        alarmValues.put("ON_THURSDAY", alarmDatabase.getOnThursday());
        alarmValues.put("ON_FRIDAY", alarmDatabase.getOnFriday());
        alarmValues.put("ON_SATURDAY", alarmDatabase.getOnSaturday());
        alarmValues.put("ON_SUNDAY", alarmDatabase.getOnSunday());
        alarmValues.put("IS_REPEATED", alarmDatabase.getIsRepeated());
        alarmValues.put("REPEAT_INTERVAL", alarmDatabase.getRepeatInterval());

        return alarmValues;
    }
}
