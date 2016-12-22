package com.example.trickyalarm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;

    private ArrayList<Alarm> alarms;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setAlarm(this.getApplicationContext(), (int) (System.currentTimeMillis() + 5 * 1000));

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");

        customizeToolbar();

        readFromDatabase();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.alarm_list);

        CardAlarmAdapter adapter = new CardAlarmAdapter(alarms, mCustomFont, this);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void readFromDatabase() {
        alarms = new ArrayList<>();
        try {
            SimpleDatabaseHelper databaseHelper = new SimpleDatabaseHelper(this);
            mDatabase = databaseHelper.getReadableDatabase();

            mCursor = mDatabase.query(SimpleDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
            if (mCursor.moveToFirst()) {
                while (mCursor.isAfterLast() == false) {
                    long milliseconds = mCursor.getLong(2);
                    Alarm temp = new Alarm(intToBoolean(mCursor.getInt(1)), getCalendarFromMilliseconds(milliseconds),
                            mCursor.getInt(3), intToBoolean(mCursor.getInt(3)),
                            intToBoolean(mCursor.getInt(4)), intToBoolean(mCursor.getInt(5)),
                            intToBoolean(mCursor.getInt(6)), intToBoolean(mCursor.getInt(7)),
                            intToBoolean(mCursor.getInt(8)), intToBoolean(mCursor.getInt(9)),
                            intToBoolean(mCursor.getInt(10)), mCursor.getInt(11));
                    alarms.add(temp);
                    mCursor.moveToNext();
                }
            }
            close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean intToBoolean(int number) {
        return number > 0 ? true : false;
    }

    public Calendar getCalendarFromMilliseconds(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }

    public void close() {
        mCursor.close();
        mDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_alarm) {
            Intent intent = new Intent(this, AddAlarmActivity.class);
            startActivity(intent);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public void customizeToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(mActionBarToolbar);
        mToolbarTitle.setText(mActionBarToolbar.getTitle());

        mToolbarTitle.setTypeface(mCustomFont);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}