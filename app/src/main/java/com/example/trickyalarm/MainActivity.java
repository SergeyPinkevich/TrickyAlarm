package com.example.trickyalarm;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("Tricky Alarm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Thin.ttf");
        mToolbarTitle.setTypeface(mCustomFont);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        String[] alarms = {"9:00","9:00","9:00","01:20","13:20","9:00","01:20","13:20","9:00","13:20"};
        boolean[] checkers = {true, false,true, false,true, false,true, false,true, false};
        String[] days = {"Mon","Weekday","Tue, Sat","Weekend","Wed, Fri","Mon","Weekday","Tue, Sat","Weekend","Wed, Fri"};
        String[] timeLeft = {"(20 Min)","(14 Min)","(39 Min)","(5 Min)","(20 min)","(20 Min)","(14 Min)","(39 Min)","(5 Min)","(20 min)"};

        CardAlarmAdapter adapter = new CardAlarmAdapter(alarms, checkers, days, timeLeft);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
}
