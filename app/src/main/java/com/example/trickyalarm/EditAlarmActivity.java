package com.example.trickyalarm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by nicholas on 25/12/2016.
 */

public class EditAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private static final String TIME_PATTERN = "HH:mm";
    private static final String ALARM_LIST_POSITION = "position";

    private AlarmRepo repo;

    private Alarm alarm;

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;
    private TextView lblTextTime;
    private TextView lblBias;
    private TextView lblRepeat;
    private TextView lblWeekly;
    private TextView lblInterval;
    private TextView lblVolume;
    private TextView lblVibration;
    private TextView lblSound;
    private TextView soundSelector;

    private AlertDialog soundList;

    private boolean[] daysConditions = new boolean[7];
    private String[] sounds;

    private Button onMonday;
    private Button onTuesday;
    private Button onWednesday;
    private Button onThursday;
    private Button onFriday;
    private Button onSaturday;
    private Button onSunday;
    private Button confirm;

    private ToggleButton repeat;
    private ToggleButton vibrate;
    private DiscreteSeekBar bias;
    private DiscreteSeekBar interval;
    private DiscreteSeekBar volume;

    private RelativeLayout containerLayout;
    private LinearLayout weekdaysLayout;

    private TextView lblTime;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;

    private int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        repo = new AlarmRepo(this);
        alarm = repo.getAlarmsList().get(getIntent().getExtras().getInt(ALARM_LIST_POSITION));

        containerLayout = (RelativeLayout) findViewById(R.id.activity_add_alarm);
        backgroundColor = alarm.getColor();
        containerLayout.setBackgroundColor(backgroundColor);

        calendar = alarm.getTime();
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        lblTime = (TextView) findViewById(R.id.lblTime);

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");

        lblTextTime = (TextView) findViewById(R.id.lblTextTime);
        lblTextTime.setTypeface(mCustomFont);

        lblTime.setTypeface(mCustomFont);

        lblTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        lblBias = (TextView) findViewById(R.id.lblBias);
        lblBias.setTypeface(mCustomFont);

        lblRepeat = (TextView) findViewById(R.id.lblRepeat);
        lblRepeat.setTypeface(mCustomFont);

        weekdaysLayout = (LinearLayout) findViewById(R.id.weekdays_layout);

        lblWeekly = (TextView) findViewById(R.id.lblWeekly);
        lblWeekly.setTypeface(mCustomFont);

        lblInterval = (TextView) findViewById(R.id.lblInterval);
        lblInterval.setTypeface(mCustomFont);

        lblVolume = (TextView)  findViewById(R.id.lblVolume);
        lblVolume.setTypeface(mCustomFont);

        lblVibration = (TextView) findViewById(R.id.lblVibration);
        lblVibration.setTypeface(mCustomFont);

        lblSound = (TextView) findViewById(R.id.lblSound);
        lblSound.setTypeface(mCustomFont);

        soundSelector = (TextView) findViewById(R.id.soundSelector);
        soundSelector.setTypeface(mCustomFont);
        soundSelector.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditAlarmActivity.this);
        builder.setTitle(R.string.title_sound_selector);
        builder.setIcon(R.drawable.ic_action_add_alarm);
        sounds = getRingtonesTitles();

        builder.setItems(sounds, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), "Selected: "+sounds[which], Toast.LENGTH_SHORT);
                toast.show();
                getRingtone(which).play();
                soundSelector.setText(sounds[which]);
            }
        });

        builder.setCancelable(false);
        soundList = builder.create();

        onMonday = (Button) findViewById(R.id.monday_letter);
        onTuesday = (Button) findViewById(R.id.tuesday_letter);
        onWednesday = (Button) findViewById(R.id.wednesday_letter);
        onThursday = (Button) findViewById(R.id.thursday_letter);
        onFriday = (Button) findViewById(R.id.friday_letter);
        onSaturday = (Button) findViewById(R.id.saturday_letter);
        onSunday = (Button) findViewById(R.id.sunday_letter);

        repeat = (ToggleButton) findViewById(R.id.toggle_button);
        repeat.setChecked(alarm.isRepeated());
        vibrate = (ToggleButton) findViewById(R.id.toggle_button_vibration);
        vibrate.setChecked(alarm.isVibrated());

        lblRepeat.setAlpha(0.0f);
        weekdaysLayout.setAlpha(0.0f);

        lblRepeat.setVisibility(View.GONE);
        weekdaysLayout.setVisibility(View.GONE);

        repeat = (ToggleButton) findViewById(R.id.toggle_button);
        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fadeInAnimation(lblRepeat);
                    fadeInAnimation(weekdaysLayout);
                } else {
                    fadOutAnimation(lblRepeat);
                    fadOutAnimation(weekdaysLayout);
                }
            }
        });

        bias = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarBias);
        bias.setProgress(alarm.getBias());

        interval = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarInterval);
        interval.setProgress(alarm.getRepeatInterval());

        volume = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarVolume);
        volume.setProgress(alarm.getVolume());

        confirm = (Button) findViewById(R.id.add_alarm_confirm);

        onMonday.setOnClickListener(this);
        onTuesday.setOnClickListener(this);
        onWednesday.setOnClickListener(this);
        onThursday.setOnClickListener(this);
        onFriday.setOnClickListener(this);
        onSaturday.setOnClickListener(this);
        onSunday.setOnClickListener(this);
        confirm.setOnClickListener(this);

        customizeToolbar();

        update();
    }

    public void fadeInAnimation(final View view) {
        view.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void fadOutAnimation(final View view) {
        view.animate()
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void update() {
        lblTime.setText(timeFormat.format(calendar.getTime()));
    }


    public void openTimePicker() {
        TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void customizeToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(mActionBarToolbar);
        mToolbarTitle.setText("Edit alarm");

        mToolbarTitle.setTypeface(mCustomFont);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.monday_letter:
                setTextColor(onMonday, 0);
                break;
            case R.id.tuesday_letter:
                setTextColor(onTuesday, 1);
                break;
            case R.id.wednesday_letter:
                setTextColor(onWednesday, 2);
                break;
            case R.id.thursday_letter:
                setTextColor(onThursday, 3);
                break;
            case R.id.friday_letter:
                setTextColor(onFriday, 4);
                break;
            case R.id.saturday_letter:
                setTextColor(onSaturday, 5);
                break;
            case R.id.sunday_letter:
                setTextColor(onSunday, 6);
                break;
            case R.id.soundSelector:
                soundList.show();
                break;
            case R.id.add_alarm_confirm:
                updateAlarm();
                Intent intent = new Intent(EditAlarmActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setTextColor(Button button, int number) {
        daysConditions[number] = !daysConditions[number];
        if (daysConditions[number])
            button.setTextColor(ContextCompat.getColor(this, R.color.white_color));
        else
            button.setTextColor(ContextCompat.getColor(this, R.color.semi_transparent));
    }

    public void updateAlarm() {
        final String ID = new AlarmRepo(this).getAlarmsList().get(getIntent().getExtras().getInt(ALARM_LIST_POSITION)).getID();
        Alarm alarm;
        if (repeat.isChecked())
            alarm = new Alarm(ID, true, calendar, bias.getProgress(), daysConditions[0], daysConditions[1], daysConditions[2],
                    daysConditions[3], daysConditions[4], daysConditions[5], daysConditions[6], true, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), getSoundAddress(1), backgroundColor);
        else
            alarm = new Alarm(ID, true, calendar, bias.getProgress(), false, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), getSoundAddress(1), backgroundColor);
        repo.updateAlarm(alarm);
    }


    /**
     * return address of a selected sound
     */
    private String getSoundAddress(int item) {
        return getRingtonesUri()[item].getPath();
    }

    public Uri[] getRingtonesUri() {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor ringtoneCursor = ringtoneMgr.getCursor();
        int ringtoneCount = ringtoneCursor.getCount();
        if (ringtoneCount == 0 && !ringtoneCursor.moveToFirst()) {
            return null;
        }
        Uri[] ringtones = new Uri[ringtoneCount];
        while(!ringtoneCursor.isAfterLast() && ringtoneCursor.moveToNext()) {
            int currentPosition = ringtoneCursor.getPosition();
            ringtones[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
        }
        ringtoneCursor.close();
        Ringtone currentRingtone = RingtoneManager.getRingtone(this, ringtones[1]);
        currentRingtone.play();
        currentRingtone.getTitle(this);
        return ringtones;
    }

    public String[] getRingtonesTitles() {
        int i = 0;
        Uri[] ringtones = getRingtonesUri();
        String[] titles = new String[ringtones.length];
        for (Uri ringtone :ringtones) {
            titles[i] = RingtoneManager.getRingtone(this, ringtone).getTitle(this);
            i += 1;
        }
        return titles;
    }
    public Ringtone getRingtone(int item) {
        return RingtoneManager.getRingtone(this, getRingtonesUri()[item]);
    }
}
