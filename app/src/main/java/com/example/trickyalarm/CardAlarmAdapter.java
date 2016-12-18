package com.example.trickyalarm;

/**
 * Created by dns on 10.11.2016.
 */


import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

class CardAlarmAdapter extends RecyclerView.Adapter<CardAlarmAdapter.ViewHolder>{
    //Предоставляет ссылку на представления, используемые в RecyclerView

    private String[] dates;
    private boolean[] checkers;
    private String[] days;
    private String[] timeLeft;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder (CardView v){
            super(v);
            cardView = v;
        }
    }

    public CardAlarmAdapter (String[] dates, boolean[] checkers, String[] days, String[] timeLeft){
        this.dates = dates;
        this.checkers = checkers;
        this.days = days;
        this.timeLeft = timeLeft;
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
//        Switch switch1 = (Switch) cardView.findViewById(R.id.switch1);
//        switch1.setChecked(checkers[position]);
//
//        TextView time = (TextView) cardView.findViewById(R.id.time_main);
//        time.setText(dates[position]);
//
//        TextView daysView = (TextView) cardView.findViewById(R.id.days);
//        daysView.setText(days[position]);
//
//        TextView timeLeftView = (TextView) cardView.findViewById(R.id.timeLeft);
//        timeLeftView.setText(timeLeft[position]);

        LinearLayout relativeLayout = (LinearLayout) cardView.findViewById(R.id.layout);


        if (dates[position] == "01:20"){
            relativeLayout.setBackgroundResource(R.drawable.p1);
        }
        if (dates[position] == "9:00"){
            relativeLayout.setBackgroundResource(R.drawable.p2);
        }
        if (dates[position] == "13:20"){
            relativeLayout.setBackgroundResource(R.drawable.p3);
        }

    }

    @Override public int getItemCount()
    {
        return dates.length;
    }
}