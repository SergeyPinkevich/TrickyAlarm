package com.example.trickyalarm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
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
import com.example.trickyalarm.database.AlarmRepo;
import com.example.trickyalarm.database.ColorRepo;

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
    private ColorRepo mColorRepo;
    private Alarm alarm;

    private Toolbar mActionBarToolbar;
    //text views:
    private TextView mToolbarTitle;
    private Typeface mCustomFont;
    private TextView lblBias;
    private TextView lblRepeat;
    private TextView lblWeekly;
    private TextView lblInterval;
    private TextView lblVolume;
    private TextView lblVibration;
    private TextView lblSound;
    private TextView soundSelector;
    private TextView lblTime;

    private AlertDialog ringtoneialog;
    private AlertDialog.Builder builder;
    private boolean[] daysConditions = new boolean[7];
    private String[][] ringtones;
    int whichRingtone;
    //buttons:
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


    private Calendar calendar;
    private SimpleDateFormat timeFormat;


    private int backgroundColor;

    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initializeFields();
        setBackground();
        formRingtoneDialog();
        setTypeFace();
        animateRepeat();
        setOnClick();
        setFromAlarm();
        volume.setMax(10);
        bias.setMax(60);
        interval.setMax(60);
        customizeToolbar();
        update();
    }

    /**
     * initialize all required fields
     */
    public void initializeFields()  {
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        repo = new AlarmRepo(this);
        alarm = repo.getAlarmsList().get(getIntent().getExtras().getInt(ALARM_LIST_POSITION));
        calendar = alarm.getTime();
        lblTime = (TextView) findViewById(R.id.lblTime);
        lblBias = (TextView) findViewById(R.id.lblBias);
        lblRepeat = (TextView) findViewById(R.id.lblRepeat);
        weekdaysLayout = (LinearLayout) findViewById(R.id.weekdays_layout);
        lblWeekly = (TextView) findViewById(R.id.lblWeekly);
        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");
        lblInterval = (TextView) findViewById(R.id.lblInterval);
        lblVibration = (TextView) findViewById(R.id.lblVibration);
        lblVolume = (TextView)  findViewById(R.id.lblVolume);
        lblSound = (TextView) findViewById(R.id.lblSound);
        soundSelector = (TextView) findViewById(R.id.soundSelector);
        ringtones = getRingtones();
        ringtone = getRingtone(0);
        onMonday = (Button) findViewById(R.id.monday_letter);
        onTuesday = (Button) findViewById(R.id.tuesday_letter);
        onWednesday = (Button) findViewById(R.id.wednesday_letter);
        onThursday = (Button) findViewById(R.id.thursday_letter);
        onFriday = (Button) findViewById(R.id.friday_letter);
        onSaturday = (Button) findViewById(R.id.saturday_letter);
        onSunday = (Button) findViewById(R.id.sunday_letter);
        confirm = (Button) findViewById(R.id.add_alarm_confirm);
        repeat = (ToggleButton) findViewById(R.id.toggle_button);
        vibrate = (ToggleButton) findViewById(R.id.toggle_button_vibration);
        repeat = (ToggleButton) findViewById(R.id.toggle_button);
        bias = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarBias);
        volume = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarVolume);
        interval = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarInterval);
        builder = new AlertDialog.Builder(EditAlarmActivity.this);
    }

    /**
     * set fonts of all labels to Exo2-Light
     */
    public void setTypeFace() {
        lblTime.setTypeface(mCustomFont);
        lblBias.setTypeface(mCustomFont);
        lblRepeat.setTypeface(mCustomFont);
        lblWeekly.setTypeface(mCustomFont);
        lblInterval.setTypeface(mCustomFont);
        lblVolume.setTypeface(mCustomFont);
        lblVibration.setTypeface(mCustomFont);
        lblSound.setTypeface(mCustomFont);
        soundSelector.setTypeface(mCustomFont);
    }

    /**
     * make repeat field animated
     */
    public void animateRepeat() {
        lblRepeat.setAlpha(0.0f);
        weekdaysLayout.setAlpha(0.0f);

        lblRepeat.setVisibility(View.GONE);
        weekdaysLayout.setVisibility(View.GONE);


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

    }

    /**
     *  set the color of background to a random one
     */
    public void setBackground() {
        containerLayout = (RelativeLayout) findViewById(R.id.activity_add_alarm);
        backgroundColor = alarm.getColor();
        int color = getResources().getColor(backgroundColor);
        containerLayout.setBackgroundColor(color);
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

    /**
     * implement organization of the ringtone dialog
     */
    public void formRingtoneDialog() {
        builder.setTitle(R.string.title_sound_selector);
        builder.setCancelable(false);
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ringtone.stop();
                dialog.cancel();
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ringtone.stop();
            }
        });

        builder.setSingleChoiceItems(ringtones[0], -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), "Selected: "+ringtones[0][which], Toast.LENGTH_SHORT);
                toast.show();
                whichRingtone = which;
                ringtone.stop();
                ringtone = getRingtone(which);
                ringtone.play();
                soundSelector.setText(ringtones[0][which]);
            }
        });
        ringtoneialog = builder.create();
    }

    /**
     * set onClickListeners to most of the buttons and labels where required
     */
    public void setOnClick() {
        lblTime.setOnClickListener(this);
        soundSelector.setOnClickListener(this);
        vibrate.setOnClickListener(this);
        onMonday.setOnClickListener(this);
        onTuesday.setOnClickListener(this);
        onWednesday.setOnClickListener(this);
        onThursday.setOnClickListener(this);
        onFriday.setOnClickListener(this);
        onSaturday.setOnClickListener(this);
        onSunday.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    /**
     * set states of fields according to alarm
     */
    public void setFromAlarm() {
        soundSelector.setText(getRingtoneTitle(alarm.getSound()));
        repeat.setChecked(alarm.isRepeated());
        bias.setProgress(alarm.getBias());
        chooseButton(onMonday, alarm.isOnMonday(), 0);
        chooseButton(onTuesday, alarm.isOnTuesday(), 1);
        chooseButton(onWednesday, alarm.isOnWednesday(), 2);
        chooseButton(onThursday, alarm.isOnThursday(), 3);
        chooseButton(onFriday, alarm.isOnFriday(), 4);
        chooseButton(onSaturday, alarm.isOnSaturday(), 5);
        chooseButton(onSunday, alarm.isOnSunday(), 6);
        interval.setProgress(alarm.getRepeatInterval());
        volume.setProgress(alarm.getVolume());
        vibrate.setChecked(alarm.isVibrated());
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
                chooseButton(onMonday, 0);
                break;
            case R.id.tuesday_letter:
                chooseButton(onTuesday, 1);
                break;
            case R.id.wednesday_letter:
                chooseButton(onWednesday, 2);
                break;
            case R.id.thursday_letter:
                chooseButton(onThursday, 3);
                break;
            case R.id.friday_letter:
                chooseButton(onFriday, 4);
                break;
            case R.id.saturday_letter:
                chooseButton(onSaturday, 5);
                break;
            case R.id.sunday_letter:
                chooseButton(onSunday, 6);
                break;
            case R.id.soundSelector:
                ringtoneialog.show();
                break;
            case R.id.lblTime:
                openTimePicker();
                break;
            case R.id.toggle_button_vibration:
                if(vibrate.isChecked()){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);
                }
                break;
            case R.id.add_alarm_confirm:
                updateAlarm();
                Intent intent = new Intent(EditAlarmActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     *set color of button according to its state
     * @param button
     * @param number - number of button
     */
    public void chooseButton(Button button, int number) {
        daysConditions[number] = !daysConditions[number];
        if (daysConditions[number])
            button.setTextColor(ContextCompat.getColor(this, R.color.white_color));
        else
            button.setTextColor(ContextCompat.getColor(this, R.color.semi_transparent));
    }

    /**
     * set color of button according to its state
     * @param button - number of button
     * @param turned - is button turned or not
     * @param number
     */
    public void chooseButton(Button button, boolean turned, int number) {
        if (turned) {
            button.setTextColor(ContextCompat.getColor(this, R.color.white_color));
            daysConditions[number] = true;
        }
        else
            button.setTextColor(ContextCompat.getColor(this, R.color.semi_transparent));
    }

    /**
     * update alarm to data base with its new parameters selected by user
     */
    public void updateAlarm() {
        final String ID = new AlarmRepo(this).getAlarmsList().get(getIntent().getExtras().getInt(ALARM_LIST_POSITION)).getID();
        Alarm alarm;
        if (repeat.isChecked())
            alarm = new Alarm(ID, true, calendar, bias.getProgress(), daysConditions[0], daysConditions[1], daysConditions[2],
                    daysConditions[3], daysConditions[4], daysConditions[5], daysConditions[6], true, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), ringtones[1][whichRingtone], backgroundColor);
        else
            alarm = new Alarm(ID, true, calendar, bias.getProgress(), false, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), ringtones[1][whichRingtone], backgroundColor);


        AlarmReceiver alarmReceiver = new AlarmReceiver(this.getApplicationContext());
        alarmReceiver.cancelAlarm(this.getApplicationContext(), alarm);
        repo.updateAlarm(alarm);
        alarmReceiver.setAlarm(this.getApplicationContext(), alarm);

    }


    /**
     * @return array of addresses of ringtones
     */
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
        return ringtones;
    }

    /**
     * @return array with titles of ringtones in column 0 and their
     * addresses in column 1
     */
    public String[][] getRingtones() {
        int i = 0;
        Uri[] ringtonesUri = getRingtonesUri();
        String[][] ringtones = new String[2][ringtonesUri.length];
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = ringtoneMgr.getCursor();

        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            ringtones[0][i] = notificationTitle;
            ringtones[1][i] = ringtonesUri[i].toString();
            i++;
        }

        return ringtones;
    }

    /**
     * @param item number of ringtone
     * @return the ringtone at position item
     */
    public Ringtone getRingtone(int item) {
        return RingtoneManager.getRingtone(this, getRingtonesUri()[item]);
    }

    /**
     * find ringtone's title according to its address
     * @param path - address of ringtone
     * @return ringtone's title
     */
    public String getRingtoneTitle(String path) {
        String title = "";
        for (int i = 0; i < ringtones[0].length; i++) {
            if (ringtones[1][i].equals(path))
                title = ringtones[0][i];
        }
        return title;
    }
}
