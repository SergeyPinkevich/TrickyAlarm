package com.example.trickyalarm;

import android.content.ContentValues;
import android.content.Context;
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
    private static final String TABLE_NAME = "ALARMS";
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
            Calendar calendar = Calendar.getInstance();
            insertAlarm(db, false, calendar, 10, true, true, true, true, true, false, false, true, 5);
        }
    }

    public void insertAlarm(SQLiteDatabase db, boolean isEnable, Calendar time, int bias,
                            boolean onMonday, boolean onTuesday, boolean onWednesday,
                            boolean onThursday, boolean onFriday, boolean onSaturday,
                            boolean onSunday, boolean isRepeated, int repeatInterval) {
        ContentValues alarmValues = new ContentValues();
        alarmValues.put("IS_ENABLE", booleanToInt(isEnable));
        alarmValues.put("TIME", time.toString());
        alarmValues.put("BIAS", bias);
        alarmValues.put("ON_MONDAY", booleanToInt(onMonday));
        alarmValues.put("ON_TUESDAY", booleanToInt(onTuesday));
        alarmValues.put("ON_WEDNESDAY", booleanToInt(onWednesday));
        alarmValues.put("ON_THURSDAY", booleanToInt(onThursday));
        alarmValues.put("ON_FRIDAY", booleanToInt(onFriday));
        alarmValues.put("ON_SATURDAY", booleanToInt(onSaturday));
        alarmValues.put("ON_SUNDAY", booleanToInt(onSunday));
        alarmValues.put("IS_REPEATED", booleanToInt(isRepeated));
        alarmValues.put("REPEAT_INTERVAL", repeatInterval);
        db.insert(TABLE_NAME, null, alarmValues);
    }

    public int booleanToInt(boolean input) {
        return input ? 1 : 0;
    }
}
