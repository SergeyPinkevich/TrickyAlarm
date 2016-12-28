package com.example.trickyalarm;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.trickyalarm.database.AlarmRepo;

import java.io.IOException;
import java.net.URI;

public class SignalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        AlarmRepo alarmRepo = new AlarmRepo(this.getApplicationContext());
        Alarm alarm = alarmRepo.getAlarmById(getIntent().getStringExtra("id"));

    }

    // Put off alarm for 10 minute.
    public void putOffAlarm(View view){
        AlarmReceiver alarmReceiver = new AlarmReceiver(this.getApplicationContext());
        alarmReceiver.setAlarm(this.getApplicationContext(), (long) (System.currentTimeMillis() + 10 * 60000));
        finish();
    }

    // Turn off alarm.
    public void turnOffAlarm(View view){
        finish();
    }

}
