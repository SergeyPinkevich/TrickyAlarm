package com.example.trickyalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 24.12.2016.
 */

public class AlarmRepo {
    private SimpleDatabaseHelper mHelper;

    public AlarmRepo(Context context) {
        mHelper = SimpleDatabaseHelper.getInstance(context);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues alarmValues = getContentValues(alarm);
        database.insert(Alarm.TABLE, null, alarmValues);
        database.close();
    }

    public void updateAlarm(Alarm alarm) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues alarmValues = getContentValues(alarm);
        database.update(Alarm.TABLE, alarmValues, Alarm.KEY_ALARM_ID + " = ?", new String[] {alarm.getID()});
        database.close();
    }

    public int deleteAlarm(Alarm alarm) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int result = database.delete(Alarm.TABLE, Alarm.KEY_ALARM_ID + " = ?", new String[] {alarm.getID()});
        database.close();
        return result;
    }

    public ArrayList<Alarm> getAlarmsList() {
        ArrayList<Alarm> alarms = new ArrayList<>();

        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = database.query(Alarm.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Calendar calendar = Calendar.getInstance();
                long milliseconds = cursor.getLong(cursor.getColumnIndex(Alarm.KEY_time));
                calendar.setTimeInMillis(milliseconds);
                Alarm alarm = new Alarm(cursor.getString(cursor.getColumnIndex(Alarm.KEY_ID)),
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_enable)) > 0, calendar,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_bias)),
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_monday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_tuesday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_wednesday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_thursday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_friday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_saturday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_on_sunday)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_repeated)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_repeat_interval)),
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_volume)),
                        cursor.getInt(cursor.getColumnIndex(Alarm.KEY_vibrated)) > 0,
                        cursor.getString(cursor.getColumnIndex(Alarm.KEY_sound)));
                alarms.add(alarm);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return alarms;
    }

    public ContentValues getContentValues(Alarm alarm) {
        // Convert Alarm to AlarmDatabase
        AlarmDatabase alarmDatabase = new AlarmDatabase(alarm);

        // Insert AlarmDatabase into DB
        ContentValues alarmValues = new ContentValues();
        alarmValues.put(Alarm.KEY_ALARM_ID, alarmDatabase.getId());
        alarmValues.put(Alarm.KEY_enable, alarmDatabase.getIsEnable());
        alarmValues.put(Alarm.KEY_time, alarmDatabase.getTime());
        alarmValues.put(Alarm.KEY_bias, alarmDatabase.getBias());
        alarmValues.put(Alarm.KEY_on_monday, alarmDatabase.getOnMonday());
        alarmValues.put(Alarm.KEY_on_tuesday, alarmDatabase.getOnTuesday());
        alarmValues.put(Alarm.KEY_on_wednesday, alarmDatabase.getOnWednesday());
        alarmValues.put(Alarm.KEY_on_thursday, alarmDatabase.getOnThursday());
        alarmValues.put(Alarm.KEY_on_friday, alarmDatabase.getOnFriday());
        alarmValues.put(Alarm.KEY_on_saturday, alarmDatabase.getOnSaturday());
        alarmValues.put(Alarm.KEY_on_sunday, alarmDatabase.getOnSunday());
        alarmValues.put(Alarm.KEY_repeated, alarmDatabase.getIsRepeated());
        alarmValues.put(Alarm.KEY_repeat_interval, alarmDatabase.getRepeatInterval());

        return alarmValues;
    }
}
