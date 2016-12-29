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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.trickyalarm.database.AlarmRepo;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

public class SignalActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private Ringtone ringtone;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signal);

        AlarmRepo alarmRepo = new AlarmRepo(this.getApplicationContext());
        alarm = alarmRepo.getAlarmById(getIntent().getStringExtra("id"));
        String hours = String.valueOf(alarm.getTime().get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(alarm.getTime().get(Calendar.MINUTE));
        if(hours.length() == 1)
            hours = '0' + hours;
        if(minutes.length() == 1)
            minutes = '0' + minutes;
        TextView clock = (TextView) findViewById(R.id.TimetextView);
        clock.setText(hours + ":" + minutes);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setVolume(alarm.getVolume());
        try {
            playRingtone();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Put off alarm for interval time.
    public void putOffAlarm(View view){
        AlarmReceiver alarmReceiver = new AlarmReceiver(this.getApplicationContext());
        alarmReceiver.setAlarm(this.getApplicationContext(), alarm.getRepeatInterval());
        stopRingtone();
        finish();
    }

    // Turn off alarm.
    public void turnOffAlarm(View view) {
        stopRingtone();
        finish();
    }

    /**
     * set volume on a scale of 1 to 10
     * @param currVolume chosen level of volume
     */
    public void setVolume (int currVolume) {
        int maxVolume = 10;
        float volume = (float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
        mediaPlayer.setVolume(1-volume, 1-volume);
    }

    /**
     * play chosen ringtone with vibration at most 10 minutes
     * @throws IOException
     */
    public void playRingtone() throws IOException {
        mediaPlayer.setLooping(true);
        mediaPlayer.setDataSource(this, getRingtoneUri(alarm.getSound()));
        mediaPlayer.prepare();
        mediaPlayer.start();
        if(alarm.isVibrated())
            vibrator.vibrate(600000);
    }

    /**
     * stop playing chosen ringtone and vibration
     */
    public void stopRingtone() {
        mediaPlayer.stop();
        if(alarm.isVibrated())
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
            if (ringtoneMgr.getRingtoneUri(currentPosition).toString().equals(path))
                ringtone = ringtoneMgr.getRingtoneUri(currentPosition);
        }
        ringtoneCursor.close();
        return ringtone;
    }

}
