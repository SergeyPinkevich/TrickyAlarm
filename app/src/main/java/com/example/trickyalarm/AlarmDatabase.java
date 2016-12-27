package com.example.trickyalarm;

/**
 * Created by Сергей Пинкевич on 23.12.2016.
 */

public class AlarmDatabase {

    private String id;
    private int isEnable;
    private long time;
    private int bias;
    private int onMonday;
    private int onTuesday;
    private int onWednesday;
    private int onThursday;
    private int onFriday;
    private int onSaturday;
    private int onSunday;
    private int isRepeated;
    private int repeatInterval;
    private int volume;
    private int isVibrated;
    private String sound;
    private int color;

    public AlarmDatabase(Alarm alarm) {
        this.id = alarm.getID();
        this.isEnable = booleanToInt(alarm.isEnable());
        this.time = alarm.getTime().getTime().getTime();
        this.bias = alarm.getBias();
        this.onMonday = booleanToInt(alarm.isOnMonday());
        this.onTuesday = booleanToInt(alarm.isOnTuesday());
        this.onWednesday = booleanToInt(alarm.isOnWednesday());
        this.onThursday = booleanToInt(alarm.isOnThursday());
        this.onFriday = booleanToInt(alarm.isOnFriday());
        this.onSaturday = booleanToInt(alarm.isOnSaturday());
        this.onSunday = booleanToInt(alarm.isOnSunday());
        this.isRepeated = booleanToInt(alarm.isRepeated());
        this.repeatInterval = alarm.getRepeatInterval();
        this.volume = alarm.getVolume();
        this.isVibrated = booleanToInt(alarm.isVibrated());
        this.sound = alarm.getSound();
        this.color = alarm.getColor();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBias() {
        return bias;
    }

    public void setBias(int bias) {
        this.bias = bias;
    }

    public int getOnMonday() {
        return onMonday;
    }

    public void setOnMonday(int onMonday) {
        this.onMonday = onMonday;
    }

    public int getOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(int onTuesday) {
        this.onTuesday = onTuesday;
    }

    public int getOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(int onWednesday) {
        this.onWednesday = onWednesday;
    }

    public int getOnThursday() {
        return onThursday;
    }

    public void setOnThursday(int onThursday) {
        this.onThursday = onThursday;
    }

    public int getOnFriday() {
        return onFriday;
    }

    public void setOnFriday(int onFriday) {
        this.onFriday = onFriday;
    }

    public int getOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(int onSaturday) {
        this.onSaturday = onSaturday;
    }

    public int getOnSunday() {
        return onSunday;
    }

    public void setOnSunday(int onSunday) {
        this.onSunday = onSunday;
    }

    public int getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(int isRepeated) {
        this.isRepeated = isRepeated;
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

    public int getIsVibrated() {
        return isVibrated;
    }

    public void setIsVibrated(int isVibrated) {
        this.isVibrated = isVibrated;
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
}
