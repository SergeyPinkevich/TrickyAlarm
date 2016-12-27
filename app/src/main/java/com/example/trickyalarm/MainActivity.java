package com.example.trickyalarm;

import android.content.Intent;
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

import com.example.trickyalarm.database.AlarmRepo;
import com.example.trickyalarm.database.ColorRepo;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;

    private ArrayList<Alarm> alarms;

    private AlarmRepo repo;
    private ColorRepo mColorRepo;

    private RecyclerView mRecyclerView;
    private CardAlarmAdapter mAdapter;

    public static ArrayList<Integer> colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");

        customizeToolbar();
        mColorRepo = new ColorRepo(this);
        colorList = mColorRepo.getColorList();

        repo = new AlarmRepo(this);
        alarms = repo.getAlarmsList();

        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);

        mAdapter = new CardAlarmAdapter(alarms, mCustomFont, getApplicationContext());

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
                                    mColorRepo.addColor(alarms.get(position).getColor());
                                    int deleted = repo.deleteAlarm(alarms.get(position));
                                    alarms.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                    showDeleteMessage(deleted);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions){
                                    mColorRepo.addColor(alarms.get(position).getColor());
                                    int deleted = repo.deleteAlarm(alarms.get(position));
                                    alarms.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                    showDeleteMessage(deleted);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    /**
     * Show toast when alarm is being deleted
     * User gets appropriate toast in both cases: if alarm was deleted and was not.
     * @param value
     */
    public void showDeleteMessage(int value) {
        if (value > 0)
            Toast.makeText(getApplicationContext(), R.string.successful_deletion, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), R.string.unsuccessful_deletion, Toast.LENGTH_SHORT).show();
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
}