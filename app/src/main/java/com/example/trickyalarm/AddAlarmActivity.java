package com.example.trickyalarm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
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
import java.util.Random;


public class AddAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private static final String TIME_PATTERN = "HH:mm";
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final String ALARM_ID = "alarm_id";

    private AlarmRepo repo;
    private ColorRepo mColorRepo;

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
    private TextView lblNotification;
    private TextView lblExplainNotification;

    private AlertDialog ringtoneDialog;
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
    private DiscreteSeekBar notification;

    private RelativeLayout containerLayout;
    private LinearLayout weekdaysLayout;


    private Calendar calendar;
    private SimpleDateFormat timeFormat;


    private int backgroundColor;
    private int randomPosition;

    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        initializeFields();
        setBackground();
        setTypeFace();
        setOnClick();
        formRingtoneDialog();
        animateRepeat();
        volume.setMax(10);
        volume.setProgress(5);
        bias.setMax(60);
        interval.setMax(60);
        notification.setMax(24);
        customizeToolbar();
        update();
    }

    /**
     * initialize all required fields
     */
    public void initializeFields()  {
        mColorRepo = new ColorRepo(this);
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        lblTime = (TextView) findViewById(R.id.lblTime);
        mCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Exo2-Light.ttf");
        lblBias = (TextView) findViewById(R.id.lblBias);
        lblRepeat = (TextView) findViewById(R.id.lblRepeat);
        lblWeekly = (TextView) findViewById(R.id.lblWeekly);
        lblInterval = (TextView) findViewById(R.id.lblInterval);
        lblSound = (TextView) findViewById(R.id.lblSound);
        lblVolume = (TextView)  findViewById(R.id.lblVolume);
        lblVibration = (TextView) findViewById(R.id.lblVibration);
        soundSelector = (TextView) findViewById(R.id.soundSelector);
        ringtones = getRingtones();
        builder = new AlertDialog.Builder(AddAlarmActivity.this);
        onMonday = (Button) findViewById(R.id.monday_letter);
        onTuesday = (Button) findViewById(R.id.tuesday_letter);
        onWednesday = (Button) findViewById(R.id.wednesday_letter);
        onThursday = (Button) findViewById(R.id.thursday_letter);
        onFriday = (Button) findViewById(R.id.friday_letter);
        onSaturday = (Button) findViewById(R.id.saturday_letter);
        onSunday = (Button) findViewById(R.id.sunday_letter);
        weekdaysLayout = (LinearLayout) findViewById(R.id.weekdays_layout);
        vibrate = (ToggleButton) findViewById(R.id.toggle_button_vibration);
        repeat = (ToggleButton) findViewById(R.id.toggle_button);
        bias = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarBias);
        interval = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarInterval);
        volume = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarVolume);
        confirm = (Button) findViewById(R.id.add_alarm_confirm);
        lblNotification = (TextView) findViewById(R.id.lblNotification) ;
        lblExplainNotification= (TextView) findViewById(R.id.explainNotification);
        notification = (DiscreteSeekBar) findViewById(R.id.discreteSeekBarNontifcation);
        ringtone = getRingtone(0);
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
        lblExplainNotification.setTypeface(mCustomFont);
        lblNotification.setTypeface(mCustomFont);
    }

    /**
     *  set the color of background to a random one
     */
    public void setBackground() {
        containerLayout = (RelativeLayout) findViewById(R.id.activity_add_alarm);
        randomPosition = new Random().nextInt(MainActivity.colorList.size());
        backgroundColor = MainActivity.colorList.get(randomPosition);
        repo = new AlarmRepo(this);
        int color = getResources().getColor(backgroundColor);
        containerLayout.setBackgroundColor(color);
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
                Toast toast = Toast.makeText(getApplicationContext(), "Selected: " + ringtones[0][which], Toast.LENGTH_SHORT);
                toast.show();
                whichRingtone = which;
                ringtone.stop();
                ringtone = getRingtone(which);
                ringtone.play();
                soundSelector.setText(ringtones[0][which]);
            }
        });
        ringtoneDialog = builder.create();
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
        calendar.set(Calendar.SECOND, 0);
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
                ringtoneDialog.show();
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
                addAlarm();
                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * set color of button according to its state
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
     * add alarm to data base with parameters selected by user
     */
    public void addAlarm() {
        Alarm alarm;

        if (repeat.isChecked())
            alarm = new Alarm(generateId(), true, calendar, bias.getProgress(), daysConditions[0], daysConditions[1], daysConditions[2],
                    daysConditions[3], daysConditions[4], daysConditions[5], daysConditions[6], true, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), ringtones[1][whichRingtone], backgroundColor, notification.getProgress());
        else
            alarm = new Alarm(generateId(), true, calendar, bias.getProgress(), false, interval.getProgress(), volume.getProgress(), vibrate.isChecked(), ringtones[1][whichRingtone], backgroundColor, notification.getProgress());

        // Add alarm to Database
        repo.addAlarm(alarm);

        removeColorFromPossibleColorsList();

        createNotification(alarm);

        AlarmReceiver alarmReceiver = new AlarmReceiver(this.getApplicationContext());
        alarmReceiver.setAlarm(this.getApplicationContext(), alarm);
    }

    private void removeColorFromPossibleColorsList() {
        MainActivity.colorList.remove(randomPosition);
        mColorRepo.deleteColor(backgroundColor);
    }

    /**
     * Create text for notification
     * @param alarm
     */
    private void createNotification(Alarm alarm) {
        // Create String from string.xml file
        String notificationText = getString(R.string.go_bed) + " " +
                String.valueOf(notification.getProgress()) + " " + getString(R.string.hours);

        // Create and setup notification
        Notification noti = getNotification(notificationText);
        if (notification.getProgress() > 0)
            scheduleNotification(noti, alarm, notification.getProgress());
    }

    /**
     *
     * @param notification notification, which was created
     * @param alarm Alarm which is added to alarm list
     * @param timeForNotification number of hours where notification should be shown. If 0 it isn't been shown
     */
    private void scheduleNotification(Notification notification, Alarm alarm, int timeForNotification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        int id = NotificationPublisher.getNotificationId(alarm.getID());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getID().hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alarm.getTime().getTimeInMillis() - (3600000 * timeForNotification));

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    /**
     * Return the notification
     * @param content text which is shown for user
     * @return
     */
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Tricky Alarm");
        builder.setContentText(content);
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        return builder.build();
    }

    /**
     * Generate ID which is time of creation in milliseconds
     */
    private String generateId() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
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

}
