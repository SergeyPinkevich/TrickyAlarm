package com.example.trickyalarm;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

public class SignalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        // Vibrate when alarm works.
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        while(true){
            vibrator.vibrate(1000);
        }
    }
}
