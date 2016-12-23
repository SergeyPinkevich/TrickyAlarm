package com.example.trickyalarm;

import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class Alarm {

    private boolean isEnable;
    private Calendar time;
    private int bias;
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;
    private boolean onSunday;
    private boolean isRepeated;
    private int repeatInterval;

    public Alarm(boolean isEnable, Calendar time, int bias, boolean isRepeated, int repeatInterval) {
        this.isEnable = isEnable;
        this.time = time;
        this.bias = bias;
        this.isRepeated = isRepeated;
        this.repeatInterval = repeatInterval;
        this.onMonday = false;
        this.onTuesday = false;
        this.onWednesday = false;
        this.onThursday = false;
        this.onFriday = false;
        this.onSaturday = false;
        this.onSunday = false;
    }

    public Alarm(boolean isEnable, Calendar time, int bias, boolean onMonday, boolean onTuesday,
                 boolean onWednesday, boolean onThursday, boolean onFriday, boolean onSaturday,
                 boolean onSunday, boolean isRepeated, int repeatInterval) {
        this.isEnable = isEnable;
        this.time = time;
        this.bias = bias;
        this.onMonday = onMonday;
        this.onTuesday = onTuesday;
        this.onWednesday = onWednesday;
        this.onThursday = onThursday;
        this.onFriday = onFriday;
        this.onSaturday = onSaturday;
        this.onSunday = onSunday;
        this.isRepeated = isRepeated;
        this.repeatInterval = repeatInterval;
    }

    public Alarm(AlarmDatabase alarmDatabase) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmDatabase.getTime());
        this.isEnable = intToBoolean(alarmDatabase.getIsEnable());
        this.time = calendar;
        this.bias = alarmDatabase.getBias();
        this.onMonday = intToBoolean(alarmDatabase.getOnMonday());
        this.onTuesday = intToBoolean(alarmDatabase.getOnTuesday());
        this.onWednesday = intToBoolean(alarmDatabase.getOnWednesday());
        this.onThursday = intToBoolean(alarmDatabase.getOnThursday());
        this.onFriday = intToBoolean(alarmDatabase.getOnFriday());
        this.onSaturday = intToBoolean(alarmDatabase.getOnSaturday());
        this.onSunday = intToBoolean(alarmDatabase.getOnSunday());
        this.isRepeated = intToBoolean(alarmDatabase.getIsRepeated());
        this.repeatInterval = alarmDatabase.getRepeatInterval();
    }

    public boolean intToBoolean(int value) {
        return value > 0 ? true : false;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getBias() {
        return bias;
    }

    public void setBias(int bias) {
        this.bias = bias;
    }

    public boolean isOnMonday() {
        return onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean isOnFriday() {
        return onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    public boolean isOnSunday() {
        return onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
}
