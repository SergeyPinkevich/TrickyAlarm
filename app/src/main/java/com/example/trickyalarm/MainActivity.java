package com.example.trickyalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
