package com.example.trickyalarm;

/**
 * Created by dns on 10.11.2016.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

class CardAlarmAdapter extends RecyclerView.Adapter<CardAlarmAdapter.ViewHolder>{
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private ArrayList<Alarm> mAlarms;
    private Typeface mFontForText;
    private Context mContext;
    private SimpleDatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

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
    }

    @Override
    public CardAlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cardView = holder.cardView;

        cardView.setCardElevation(0);
        cardView.setBackgroundColor(getRandomColor());

        TextView alarmTime = (TextView) cardView.findViewById(R.id.alarm_time);
        String time = calendarToString(mAlarms.get(position).getTime());
        alarmTime.setText(time);
        alarmTime.setTypeface(mFontForText);

        Button onMonday = (Button) cardView.findViewById(R.id.monday_letter);
        Button onTuesday = (Button) cardView.findViewById(R.id.tuesday_letter);
        Button onWednesday = (Button) cardView.findViewById(R.id.wednesday_letter);
        Button onThursday = (Button) cardView.findViewById(R.id.thursday_letter);
        Button onFriday = (Button) cardView.findViewById(R.id.friday_letter);
        Button onSaturday = (Button) cardView.findViewById(R.id.saturday_letter);
        Button onSunday = (Button) cardView.findViewById(R.id.sunday_letter);

        setColorText(onMonday, mAlarms.get(position).isOnMonday());
        setColorText(onTuesday, mAlarms.get(position).isOnTuesday());
        setColorText(onWednesday, mAlarms.get(position).isOnWednesday());
        setColorText(onThursday, mAlarms.get(position).isOnThursday());
        setColorText(onFriday, mAlarms.get(position).isOnFriday());
        setColorText(onSaturday, mAlarms.get(position).isOnSaturday());
        setColorText(onSunday, mAlarms.get(position).isOnSunday());

        ToggleButton toggleButton = (ToggleButton) cardView.findViewById(R.id.toggle_button);
        toggleButton.setChecked(mAlarms.get(position).isEnable());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Alarm alarm = mAlarms.get(position);
                alarm.setEnable(b);
                updateDatabase(alarm, position);
            }
        });

        TextView biasTime = (TextView) cardView.findViewById(R.id.bias_time);
        biasTime.setText(String.valueOf(mAlarms.get(position).getBias()));
        biasTime.setTypeface(mFontForText);
    }

    public void updateDatabase(Alarm alarm, int position) {
        mHelper = new SimpleDatabaseHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
        mHelper.updateAlarm(mDatabase, alarm, position);
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

    public int getRandomColor() {
        int colors[] = mContext.getResources().getIntArray(R.array.randomColors);
        return colors[new Random().nextInt(colors.length)];
    }

    @Override public int getItemCount()
    {
        return mAlarms.size();
    }
}