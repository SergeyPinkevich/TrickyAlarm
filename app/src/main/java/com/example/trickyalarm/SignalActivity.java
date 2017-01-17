package com.example.trickyalarm;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trickyalarm.database.AlarmRepo;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

public class SignalActivity extends AppCompatActivity implements View.OnTouchListener {

    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private Ringtone ringtone;
    private ImageView cross;
    private ImageView curveArrow;
    private ImageView arrow1;
    private ImageView arrow2;
    private ImageView arrow3;
    private ImageView arrow4;
    float x;
    View view;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signal);

        view = findViewById(R.id.activity_signal);
        AlarmRepo alarmRepo = new AlarmRepo(this.getApplicationContext());
        alarm = alarmRepo.getAlarmById(getIntent().getStringExtra(AlarmReceiver.ALARM_ID));
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

        arrow1 = (ImageView) findViewById(R.id.arrow1ImageView);
        arrow2 = (ImageView) findViewById(R.id.arrow2ImageView);
        arrow3 = (ImageView) findViewById(R.id.arrow3ImageView);
        arrow4 = (ImageView) findViewById(R.id.arrow4ImageView);

        cross = (ImageView) findViewById(R.id.crossImageView);
        cross.setOnTouchListener(this);

        curveArrow = (ImageView) findViewById(R.id.repeatImageView);
        curveArrow.setOnTouchListener(this);

    }

    // Put off alarm for interval time.
    public void putOffAlarm(){
        AlarmReceiver alarmReceiver = new AlarmReceiver(this.getApplicationContext());
        if (alarm.getRepeatInterval() > 0) {
            long futureAlarm = alarm.getTime().getTimeInMillis() + alarm.getRepeatInterval() * 60000;
            Calendar alarmNext = Calendar.getInstance();
            alarmNext.setTimeInMillis(futureAlarm);
            alarm.setTime(alarmNext);
            alarmReceiver.setAlarm(this.getApplicationContext(), alarm);
            stopRingtone();
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(), R.string.cannot_repeat, Toast.LENGTH_SHORT).show();
        }
    }

    // Turn off alarm.
    public void turnOffAlarm() {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == findViewById(R.id.crossImageView)){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = cross.getX();
                    fadOutAnimation(arrow1);
                    fadOutAnimation(arrow2);
                    fadOutAnimation(arrow3);
                    fadOutAnimation(arrow4);
                    fadOutAnimation(curveArrow);
                    break;
                case MotionEvent.ACTION_MOVE:
                    cross.setX(event.getRawX() - cross.getWidth()/1.5f);
                    break;
                case MotionEvent.ACTION_UP:
                    if (cross.getX() > view.getWidth()/3) {
                        turnOffAlarm();
                    }
                    else {
                        fadeInAnimation(arrow1);
                        fadeInAnimation(arrow2);
                        fadeInAnimation(arrow3);
                        fadeInAnimation(arrow4);
                        fadeInAnimation(curveArrow);
                        cross.setX(x);
                    }
            }
        }

        if (v == findViewById(R.id.repeatImageView)){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = curveArrow.getX();
                    fadOutAnimation(arrow1);
                    fadOutAnimation(arrow2);
                    fadOutAnimation(arrow3);
                    fadOutAnimation(arrow4);
                    fadOutAnimation(cross);
                    break;
                case MotionEvent.ACTION_MOVE:
                    curveArrow.setX(event.getRawX() - curveArrow.getWidth()/1.5f);
                    break;
                case MotionEvent.ACTION_UP:
                    if (curveArrow.getX() < view.getWidth()/2) {
                        putOffAlarm();
                    }
                    else {
                        fadeInAnimation(arrow1);
                        fadeInAnimation(arrow2);
                        fadeInAnimation(arrow3);
                        fadeInAnimation(arrow4);
                        fadeInAnimation(cross);
                        curveArrow.setX(x);
                    }
            }
        }

        return false;
    }

    public void fadeInAnimation(final View view) {
        view.animate()
                .alpha(1.0f)
                .setDuration(100);
    }


    public void fadOutAnimation(final View view) {
        view.animate()
                .alpha(0.0f)
                .setDuration(100);
    }



}
