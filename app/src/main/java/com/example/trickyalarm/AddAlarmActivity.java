package com.example.trickyalarm;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.R.attr.onClick;

public class AddAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private static final String TIME_PATTERN = "HH:mm";

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;
    private TextView lblTextTime;
    private TextView lblBias;
    private TextView lblRepeat;
    private TextView lblWeekly;

    private Button onMonday;
    private Button onTuesday;
    private Button onWednesday;
    private Button onThursday;
    private Button onFriday;
    private Button onSaturday;
    private Button onSunday;
    private Button confirm;

    private ToggleButton repeatable;
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

        onMonday = (Button) findViewById(R.id.monday_letter);
        onTuesday = (Button) findViewById(R.id.tuesday_letter);
        onWednesday = (Button) findViewById(R.id.wednesday_letter);
        onThursday = (Button) findViewById(R.id.thursday_letter);
        onFriday = (Button) findViewById(R.id.friday_letter);
        onSaturday = (Button) findViewById(R.id.saturday_letter);
        onSunday = (Button) findViewById(R.id.sunday_letter);

        confirm = (Button) findViewById(R.id.add_alarm_confirm);


        onMonday.setOnClickListener(this);
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
                setColorText(onMonday);
                break;
            case R.id.add_alarm_confirm:
                onSupportNavigateUp();
                break;
        }
    }

    public void setColorText(Button button) {
        if (button.isEnabled())
            button.setTextColor(Color.WHITE);

        else if (!button.isEnabled())
            button.setTextColor(Color.BLACK);
    }

    public void confirmAddAlarm() {

    }
}
