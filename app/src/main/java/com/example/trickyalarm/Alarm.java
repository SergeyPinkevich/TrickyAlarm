package com.example.trickyalarm;

import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class Alarm {

    // Label table name
    public static final String TABLE = "alarms";

    // Labels Table Columns name
    public static final String KEY_ID = "_id";
    public static final String KEY_ALARM_ID = "alarm_id";
    public static final String KEY_enable = "enable";
    public static final String KEY_time = "time";
    public static final String KEY_bias = "bias";
    public static final String KEY_on_monday = "monday";
    public static final String KEY_on_tuesday = "tuesday";
    public static final String KEY_on_wednesday = "wednesday";
    public static final String KEY_on_thursday = "thursday";
    public static final String KEY_on_friday = "friday";
    public static final String KEY_on_saturday = "saturday";
    public static final String KEY_on_sunday = "sunday";
    public static final String KEY_repeated = "repeated";
    public static final String KEY_repeat_interval = "repeat_interval";

    private String ID;
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
        generateId();
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
        generateId();
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

    /**
     * Generate ID which is time of creation in milliseconds
     */
    private void generateId() {
        Calendar calendar = Calendar.getInstance();
        long timeOfCreation = calendar.getTimeInMillis();
        this.ID = String.valueOf(timeOfCreation);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
