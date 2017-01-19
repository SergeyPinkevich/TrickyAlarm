package com.example.trickyalarm;

import java.util.Calendar;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class Alarm implements Comparable<Alarm>{

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
    public static final String KEY_volume = "volume";
    public static final String KEY_vibrated = "vibrated";
    public static final String KEY_sound = "sound";
    public static final String KEY_color = "color";
    public static final String KEY_notification_time = "notification_time";

    private String ID; // ID (time of creation in milliseconds)
    private boolean isEnable; // ON or OFF
    private Calendar time; // the response time in Calendar format
    private int bias; // bias at which alarm will signal
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;
    private boolean onSunday;
    private boolean isRepeated; // repeated or it will be the single alarm without repetition
    private int repeatInterval;
    private int volume;
    private boolean isVibrated;
    private String sound; // path to the sound file on the device
    private int color;
    private int notificationTime;

    public Alarm(String ID, boolean isEnable, Calendar time, int bias, boolean isRepeated, int repeatInterval,
                 int volume, boolean isVibrated, String sound, int color, int notificationTime) {
        this.ID = ID;
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
        this.volume = volume;
        this.isVibrated = isVibrated;
        this.sound = sound;
        this.color = color;
        this.notificationTime = notificationTime;
    }

    public Alarm(String ID, boolean isEnable, Calendar time, int bias, boolean onMonday, boolean onTuesday,
                 boolean onWednesday, boolean onThursday, boolean onFriday, boolean onSaturday,
                 boolean onSunday, boolean isRepeated, int repeatInterval, int volume, boolean isVibrated,
                 String sound, int color, int notificationTime) {
        this.ID = ID;
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
        this.volume = volume;
        this.isVibrated = isVibrated;
        this.sound = sound;
        this.color = color;
        this.notificationTime = notificationTime;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isVibrated() {
        return isVibrated;
    }

    public void setVibrated(boolean vibrated) {
        isVibrated = vibrated;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public int compareTo(Alarm o) {
        if (this.time.getTimeInMillis() < (o.getTime().getTimeInMillis()))
            return -1;
        else if (this.time.getTimeInMillis() < (o.getTime().getTimeInMillis()))
            return 1;
        else return 0;
    }
}
