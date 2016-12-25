package com.example.trickyalarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

    private AlarmRepo repo;

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
    private String[] sounds = {"1", "2", "3"};

    private Button onMonday;
    private Button onTuesday;
    private Button onWednesday;
    private Button onThursday;
    private Button onFriday;
    private Button onSaturday;
    private Button onSunday;
    private Button confirm;

    private ToggleButton repeat;
    private DiscreteSeekBar bias;
    private DiscreteSeekBar interval;

    private View.OnClickListener weekButtons;

    private TextView lblTime;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_alarm);

        repo = new AlarmRepo(this);

        calendar = Calendar.getInstance();
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

        builder.setItems(sounds, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), "Selected: "+sounds[which], Toast.LENGTH_SHORT);
                toast.show();
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

        bias = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarBias);

        interval = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarInterval);

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
        mToolbarTitle.setText(R.string.new_alarm);

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
                addAlarm();
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

    public void addAlarm() {
        Alarm alarm;

        if (repeat.isChecked())
            alarm = new Alarm(generateId(), true, calendar, bias.getProgress(), daysConditions[0], daysConditions[1], daysConditions[2],
                    daysConditions[3], daysConditions[4], daysConditions[5], daysConditions[6], true, interval.getProgress());
        else
            alarm = new Alarm(generateId(), true, calendar, bias.getProgress(), false, interval.getProgress());

        repo.updateAlarm(alarm);
    }

    /**
     * Generate ID which is time of creation in milliseconds
     */
    private String generateId() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * Generate ID which is time of creation in milliseconds
     */
    private String generateId() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }
}
