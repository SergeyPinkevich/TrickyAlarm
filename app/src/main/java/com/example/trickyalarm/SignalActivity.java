package com.example.trickyalarm;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
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

    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private Ringtone ringtone;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        AlarmRepo alarmRepo = new AlarmRepo(this.getApplicationContext());
        alarm = alarmRepo.getAlarmById(getIntent().getStringExtra("id"));
        mediaPlayer = new MediaPlayer();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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

    /**
     * set volume on a scale of 1 to 100
     * @param currVolume chosen level of volume
     */
    public void setVolume (int currVolume) {
        int maxVolume = 10;
        float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
        mediaPlayer.setVolume(1-log1, 1-log1);
    }

    /**
     * play chosen ringtone with vibration at most 10 minutes
     * @throws IOException
     */
    public void playRingtone() throws IOException {
        mediaPlayer.setLooping(true);
        mediaPlayer.setDataSource(this, getRingtoneUri(alarm.getSound()));
        mediaPlayer.start();
        vibrator.vibrate(600000);
    }

    /**
     * stop playing chosen ringtone and vibration
     */
    public void stopRingtone() {
        mediaPlayer.stop();
        vibrator.cancel();
    }

    /**
     * @return the uri of ringtone at address path
     */
    public Uri getRingtoneUri(String path) {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor ringtoneCursor = ringtoneMgr.getCursor();
        Uri ringtone = ringtoneMgr.getRingtoneUri(0);
        while(!ringtoneCursor.isAfterLast() && ringtoneCursor.moveToNext()) {
            int currentPosition = ringtoneCursor.getPosition();
            if (ringtoneMgr.getRingtoneUri(currentPosition).equals(path))
                ringtone = ringtoneMgr.getRingtoneUri(currentPosition);
        }
        ringtoneCursor.close();
        return ringtone;
    }

}
