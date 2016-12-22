package com.example.trickyalarm;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TIME_PATTERN = "HH:mm";

    private Toolbar mActionBarToolbar;
    private TextView mToolbarTitle;
    private Typeface mCustomFont;
    private RelativeLayout timeContainer;

    private TextView lblTime;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;

    private NumberPicker numberPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());




        lblTime = (TextView) findViewById(R.id.lblTime);

        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Thin.ttf");

        timeContainer = (RelativeLayout) findViewById(R.id.timeContainer);


        timeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        customizeToolbar();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        update();
    }

    private void update() {
        lblTime.setText(timeFormat.format(calendar.getTime()));
    }

    public void openTimePicker() {

        TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    public void customizeToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) mActionBarToolbar.findViewById(R.id.toolbar_title);



        setSupportActionBar(mActionBarToolbar);
        mToolbarTitle.setText(mActionBarToolbar.getTitle());

        mToolbarTitle.setTypeface(mCustomFont);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
