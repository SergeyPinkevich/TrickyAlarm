package com.example.trickyalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Сергей Пинкевич on 27.12.2016.
 */

public class ColorRepo {
    private SimpleDatabaseHelper mHelper;
    public static final String TABLE = "colors";
    public static final String KEY_color_id = "color_id";

    public ColorRepo(Context context) {
        mHelper = SimpleDatabaseHelper.getInstance(context);
    }

    public void addColor(SQLiteDatabase db, int resId) {
        ContentValues colorValues = getContentValues(resId);
        db.insert(TABLE, null, colorValues);
//        db.close();
    }

    public void addColor(int resId) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues colorValues = getContentValues(resId);
        database.insert(TABLE, null, colorValues);
        database.close();
    }

    public int deleteColor(int resId) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int result = database.delete(TABLE, KEY_color_id + " = ?", new String[] {String.valueOf(resId)});
        database.close();
        return result;
    }

    public ArrayList<Integer> getColorList() {
        ArrayList<Integer> colors = new ArrayList<>();

        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                colors.add(cursor.getInt(cursor.getColumnIndex(KEY_color_id)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return colors;
    }

    public ContentValues getContentValues(int colorId) {
        ContentValues colorValues = new ContentValues();
        colorValues.put(KEY_color_id, colorId);
        return colorValues;
    }
}
