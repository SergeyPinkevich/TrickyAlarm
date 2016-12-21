package com.example.trickyalarm;

/**
 * Created by dns on 10.11.2016.
 */


import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class CardAlarmAdapter extends RecyclerView.Adapter<CardAlarmAdapter.ViewHolder>{
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private ArrayList<Alarm> mAlarms;
    private Typeface mFontForText;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder (CardView v){
            super(v);
            cardView = v;
        }
    }

    public CardAlarmAdapter (ArrayList<Alarm> alarms, Typeface typeface){
        this.mAlarms = alarms;
        mFontForText = typeface;
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