package com.example.trickyalarm;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        while(true){
            v.vibrate(500);
        }
    }

    // Put off alarm for 15 seconds.
    public void putOffAlarm(View view){
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setAlarm(this.getApplicationContext(), (long) (System.currentTimeMillis() + 15 * 1000));
        finish();
    }

    // Turn off alarm.
    public void turnOffAlarm(View view){
        finish();
    }

}
