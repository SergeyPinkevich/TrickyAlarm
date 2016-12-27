package com.example.trickyalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);
    }

    // Put off alarm for 15 seconds.
    public void putOffAlarm(View view){
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setAlarm(this.getApplicationContext(), (int) (System.currentTimeMillis() + 15 * 1000));
        finish();
    }

    // Turn off alarm.
    public void turnOffAlarm(View view){
        finish();
    }

}
