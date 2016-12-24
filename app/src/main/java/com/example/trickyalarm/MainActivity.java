package com.example.trickyalarm;

import android.app.Activity;
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

//        AlarmReceiver alarmReceiver = new AlarmReceiver();
//        alarmReceiver.setAlarm(this.getApplicationContext(), (int) (System.currentTimeMillis() + 60 * 1000));

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

    public void readFromDatabase() {
        alarms = new ArrayList<>();
        try {
            mCursor = mDatabase.query(SimpleDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
            if (mCursor.moveToFirst()) {
                while (mCursor.isAfterLast() == false) {
                    long milliseconds = mCursor.getLong(2);
                    AlarmDatabase alarmDatabase = new AlarmDatabase(
                            mCursor.getInt(1), milliseconds,
                            mCursor.getInt(3), mCursor.getInt(4),
                            mCursor.getInt(5), mCursor.getInt(6),
                            mCursor.getInt(7), mCursor.getInt(8),
                            mCursor.getInt(9), mCursor.getInt(10),
                            mCursor.getInt(11), mCursor.getInt(12));
                    Alarm temp = new Alarm(alarmDatabase);
                    alarms.add(temp);
                    mCursor.moveToNext();
                }
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    public void customizeToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(mActionBarToolbar);
        mToolbarTitle.setText(mActionBarToolbar.getTitle());

        mToolbarTitle.setTypeface(mCustomFont);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    public void close() {
        mCursor.close();
        mDatabase.close();
    }
}