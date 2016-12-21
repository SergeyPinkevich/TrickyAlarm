package com.example.trickyalarm;

/**
 * Created by dns on 10.11.2016.
 */


import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class CardAlarmAdapter extends RecyclerView.Adapter<CardAlarmAdapter.ViewHolder>{
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private ArrayList<Alarm> mAlarms;
    private Typeface mFontForText;
    private Context mContext;

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
    public CardAlarmAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position){

        CardView cardView = holder.cardView;

        cardView.setCardElevation(0);

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

        setColorText(onMonday, mAlarms.get(position).getOnMonday());
        setColorText(onTuesday, mAlarms.get(position).getOnTuesday());
        setColorText(onWednesday, mAlarms.get(position).getOnWednesday());
        setColorText(onThursday, mAlarms.get(position).getOnThursday());
        setColorText(onFriday, mAlarms.get(position).getOnFriday());
        setColorText(onSaturday, mAlarms.get(position).getOnSaturday());
        setColorText(onSunday, mAlarms.get(position).getOnSunday());

        ToggleButton toggleButton = (ToggleButton) cardView.findViewById(R.id.toggle_button);
        toggleButton.setChecked(mAlarms.get(position).isEnable());

        TextView biasTime = (TextView) cardView.findViewById(R.id.bias_time);
        biasTime.setText(String.valueOf(mAlarms.get(position).getBias()));
        biasTime.setTypeface(mFontForText);
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