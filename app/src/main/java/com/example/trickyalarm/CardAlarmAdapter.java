package com.example.trickyalarm;

/**
 * Created by dns on 10.11.2016.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.trickyalarm.database.AlarmRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class CardAlarmAdapter extends RecyclerView.Adapter<CardAlarmAdapter.ViewHolder> implements View.OnClickListener{
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private static final String ALARM_LIST_POSITION = "position";

    private ArrayList<Alarm> mAlarms;
    private Typeface mFontForText;
    private Context mContext;

    private AlarmRepo repo;

    private boolean[] daysConditions = new boolean[7];

    private Button onMonday;
    private Button onTuesday;
    private Button onWednesday;
    private Button onThursday;
    private Button onFriday;
    private Button onSaturday;
    private Button onSunday;
    private ToggleButton toggleButton;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder (CardView v){
            super(v);
            cardView = v;
        }
    }

    public CardAlarmAdapter (ArrayList<Alarm> alarms, Typeface typeface, Context context){
        this.mAlarms = alarms;
        mFontForText = typeface;
        mContext = context;
        repo = new AlarmRepo(mContext);
    }

    @Override
    public CardAlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cardView = holder.cardView;

        cardView.setCardElevation(0);
        int color = mContext.getResources().getColor(mAlarms.get(position).getColor());
        cardView.setBackgroundColor(color);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAlarmActivity.class);
                intent.putExtra(ALARM_LIST_POSITION, position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        final TextView alarmTime = (TextView) cardView.findViewById(R.id.alarm_time);
        String time = calendarToString(mAlarms.get(position).getTime());
        alarmTime.setText(time);
        alarmTime.setTypeface(mFontForText);

        onMonday = (Button) cardView.findViewById(R.id.monday_letter);
        onTuesday = (Button) cardView.findViewById(R.id.tuesday_letter);
        onWednesday = (Button) cardView.findViewById(R.id.wednesday_letter);
        onThursday = (Button) cardView.findViewById(R.id.thursday_letter);
        onFriday = (Button) cardView.findViewById(R.id.friday_letter);
        onSaturday = (Button) cardView.findViewById(R.id.saturday_letter);
        onSunday = (Button) cardView.findViewById(R.id.sunday_letter);

        setColorText(onMonday, mAlarms.get(position).isOnMonday());
        setColorText(onTuesday, mAlarms.get(position).isOnTuesday());
        setColorText(onWednesday, mAlarms.get(position).isOnWednesday());
        setColorText(onThursday, mAlarms.get(position).isOnThursday());
        setColorText(onFriday, mAlarms.get(position).isOnFriday());
        setColorText(onSaturday, mAlarms.get(position).isOnSaturday());
        setColorText(onSunday, mAlarms.get(position).isOnSunday());

        toggleButton = (ToggleButton) cardView.findViewById(R.id.toggle_button);
        toggleButton.setChecked(mAlarms.get(position).isEnable());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Alarm alarm = mAlarms.get(position);
                alarm.setEnable(b);
                if (b) {
                    Toast.makeText(mContext, mContext.getString(R.string.alarm_on), Toast.LENGTH_SHORT);
                    NotificationPublisher publisher = new NotificationPublisher(mContext);
                    publisher.createNotification(alarm);
                }
                else {
                    Toast.makeText(mContext, mContext.getString(R.string.alarm_off), Toast.LENGTH_SHORT);
                    AlarmReceiver receiver = new AlarmReceiver(mContext);
                    receiver.cancelAlarm(mContext, alarm);
                    cancelNotification(alarm);
                }
                repo.updateAlarm(alarm);
            }
        });

        TextView biasTime = (TextView) cardView.findViewById(R.id.bias_time);
        int bias = mAlarms.get(position).getBias();
        if (bias > 0)
            biasTime.setText(String.valueOf(mAlarms.get(position).getBias()));
        else
            biasTime.setText("");
        biasTime.setTypeface(mFontForText);
    }

    public void cancelNotification(Alarm alarm) {
        NotificationPublisher publisher = new NotificationPublisher(mContext);
        int notificationId = NotificationPublisher.getNotificationId(alarm.getID());
        publisher.cancelNotification(mContext, notificationId, alarm);
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
        }
    }

    public void setTextColor(Button button, int number) {
        daysConditions[number] = !daysConditions[number];
        if (daysConditions[number])
            button.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
        else
            button.setTextColor(ContextCompat.getColor(mContext, R.color.semi_transparent));
    }

    public void setColorText(Button button, boolean condition) {
        if (condition)
            button.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
        else
            button.setTextColor(ContextCompat.getColor(mContext, R.color.semi_transparent));
    }

    public String calendarToString(Calendar time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return dateFormat.format(time.getTime());
    }

    @Override public int getItemCount()
    {
        return mAlarms.size();
    }
}