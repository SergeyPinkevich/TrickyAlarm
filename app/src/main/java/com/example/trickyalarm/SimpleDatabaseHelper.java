package com.example.trickyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class SimpleDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarms.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "ALARMS";
    private Context mContext;

    SimpleDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
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
            insertAlarm(db, 0, System.currentTimeMillis() + 10000000, 25, 1, 1, 1, 1, 1, 0, 0, 0, 5);
            insertAlarm(db, 1, System.currentTimeMillis() + 28900000, 10, 0, 1, 0, 1, 0, 1, 1, 0, 5);
            insertAlarm(db, 1, System.currentTimeMillis() + 1239878497, 10, 1, 1, 0, 0, 0, 1, 0, 1, 5);
        }
    }

    public void insertAlarm(SQLiteDatabase db, int isEnable, long time, int bias,
                            int onMonday, int onTuesday, int onWednesday,
                            int onThursday, int onFriday, int onSaturday,
                            int onSunday, int isRepeated, int repeatInterval) {
        ContentValues alarmValues = new ContentValues();
        alarmValues.put("IS_ENABLE", isEnable);
        alarmValues.put("TIME", time);
        alarmValues.put("BIAS", bias);
        alarmValues.put("ON_MONDAY", onMonday);
        alarmValues.put("ON_TUESDAY", onTuesday);
        alarmValues.put("ON_WEDNESDAY", onWednesday);
        alarmValues.put("ON_THURSDAY", onThursday);
        alarmValues.put("ON_FRIDAY", onFriday);
        alarmValues.put("ON_SATURDAY", onSaturday);
        alarmValues.put("ON_SUNDAY", onSunday);
        alarmValues.put("IS_REPEATED", isRepeated);
        alarmValues.put("REPEAT_INTERVAL", repeatInterval);
        db.insert(TABLE_NAME, null, alarmValues);
    }

    public void addAlarm(SQLiteDatabase db, Alarm alarm) {
        insertAlarm(db, booleanToInt(alarm.isEnable()), alarm.getTime().getTime().getTime(), alarm.getBias(),
                booleanToInt(alarm.isOnMonday()), booleanToInt(alarm.isOnTuesday()), booleanToInt(alarm.isOnWednesday()),
                        booleanToInt(alarm.isOnThursday()), booleanToInt(alarm.isOnFriday()), booleanToInt(alarm.isOnSaturday()),
                booleanToInt(alarm.isOnSunday()), booleanToInt(alarm.isRepeated()), alarm.getRepeatInterval());
    }

    public void updateAlarm(SQLiteDatabase db, Alarm alarm, int position) {
        
    }

    public int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }
}
