package com.example.trickyalarm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;

    private ArrayList<Alarm> alarms;
    private SimpleDatabaseHelper databaseHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;

    private RecyclerView mRecyclerView;
    private CardAlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AlarmReceiver alarmReceiver = new AlarmReceiver();
        //alarmReceiver.setAlarm(this.getApplicationContext(), (int) (System.currentTimeMillis() + 10 * 1000));

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");

        customizeToolbar();

        databaseHelper = SimpleDatabaseHelper.getInstance(this);
        mDatabase = databaseHelper.getWritableDatabase();

        readFromDatabase();

        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);

        mAdapter = new CardAlarmAdapter(alarms, mCustomFont, this);

        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            mAdapter.notifyDataSetChanged();

        swipeSetup();
    }

    /**
     * Method for deleting the card by swipe
     * It uses third-party library and delete record from ArrayList<Alarm> and from Database
     */
    public void swipeSetup() {
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    alarms.remove(position);
                                    databaseHelper.deleteAlarm(mDatabase, alarms.get(position), position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions){
                                    alarms.remove(position);
                                    databaseHelper.deleteAlarm(mDatabase, alarms.get(position), position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    /**
     * Method for reading all records in Database
     */
    public void readFromDatabase() {
        alarms = new ArrayList<>();
        try {
            mCursor = mDatabase.query(SimpleDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
            if (mCursor.moveToFirst()) {
                while (mCursor.isAfterLast() == false) {
                    Calendar calendar = Calendar.getInstance();
                    long milliseconds = mCursor.getLong(2);
                    calendar.setTimeInMillis(milliseconds);
                    Alarm alarm = new Alarm(
                            mCursor.getInt(1) > 0, calendar,
                            mCursor.getInt(3), mCursor.getInt(4) > 0,
                            mCursor.getInt(5) > 0, mCursor.getInt(6) > 0,
                            mCursor.getInt(7) > 0, mCursor.getInt(8) > 0,
                            mCursor.getInt(9) > 0, mCursor.getInt(10) > 0,
                            mCursor.getInt(11) > 0, mCursor.getInt(12));
                    alarms.add(alarm);
                    mCursor.moveToNext();
                }
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overriding existing method to customize menu (add plus icon on toolbar)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Adding intent to plus icon. It starts AddActivity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_alarm) {
            Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
            startActivity(intent);
            close();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * Customizing toolbar
     */
    public void customizeToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(mActionBarToolbar);
        mToolbarTitle.setText(mActionBarToolbar.getTitle());

        mToolbarTitle.setTypeface(mCustomFont);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Overriding existing activity lifecycle method to close cursor and database
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    /**
     * Close cursor and database
     */
    public void close() {
        mCursor.close();
        mDatabase.close();
    }
}