package com.example.trickyalarm.database;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trickyalarm.Alarm;
import com.example.trickyalarm.R;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class SimpleDatabaseHelper extends SQLiteOpenHelper {

    private static SimpleDatabaseHelper sInstance;
    private Context mContext;

    private static final String DB_NAME = "alarms.db";
    private static final int DB_VERSION = 1;

    /**
     * Singleton pattern
     * @param context
     * @return
     */
    public static synchronized SimpleDatabaseHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new SimpleDatabaseHelper(context);
        return sInstance;
    }

    public SimpleDatabaseHelper(Context context) {
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
            String CREATE_TABLE_ALARMS = "CREATE TABLE " + Alarm.TABLE + " ("
                    + Alarm.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Alarm.KEY_ALARM_ID + " TEXT, "
                    + Alarm.KEY_enable + " INTEGER, "
                    + Alarm.KEY_time + " INTEGER, "
                    + Alarm.KEY_bias + " INTEGER, "
                    + Alarm.KEY_on_monday + " INTEGER, "
                    + Alarm.KEY_on_tuesday + " INTEGER, "
                    + Alarm.KEY_on_wednesday + " INTEGER, "
                    + Alarm.KEY_on_thursday + " INTEGER, "
                    + Alarm.KEY_on_friday + " INTEGER, "
                    + Alarm.KEY_on_saturday + " INTEGER, "
                    + Alarm.KEY_on_sunday + " INTEGER, "
                    + Alarm.KEY_repeated + " INTEGER, "
                    + Alarm.KEY_repeat_interval + " INTEGER, "
                    + Alarm.KEY_volume + " INTEGER, "
                    + Alarm.KEY_vibrated + " INTEGER, "
                    + Alarm.KEY_sound + " TEXT, "
                    + Alarm.KEY_color + " INTEGER, "
                    + Alarm.KEY_notification_time + " INTEGER);";

            String CREATE_TABLE_COLORS = "CREATE TABLE " + ColorRepo.TABLE + " ("
                    +  "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ColorRepo.KEY_color_id + " INTEGER);";

            db.execSQL(CREATE_TABLE_ALARMS);
            db.execSQL(CREATE_TABLE_COLORS);

            addAllColors(db);
        }
    }

    public void addAllColors(SQLiteDatabase db) {
        ColorRepo colorRepo = new ColorRepo(mContext);
        TypedArray locationFlags = mContext.getResources().obtainTypedArray(R.array.randomColors);
        for (int i = 0; i < locationFlags.length(); i++) {
            int resId = locationFlags.getResourceId(i, -1);
            colorRepo.addColor(db, resId);
        }
        locationFlags.recycle();
    }
}
