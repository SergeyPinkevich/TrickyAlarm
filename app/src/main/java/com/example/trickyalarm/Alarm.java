package com.example.trickyalarm;

/**
 * Created by Сергей Пинкевич on 09.11.2016.
 */

public class Alarm {

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
    private boolean[] weekdays;

    /**
     * Single alarm (without scheduler depending on weekdays)
     * @param isEnable
     * @param time
     * @param bias
     * @param isRepeated
     * @param repeatInterval
     */
    public Alarm(int isEnable, long time, int bias, int isRepeated, int repeatInterval) {
        this.isEnable = isEnable;
        this.time = time;
        this.bias = bias;
        this.isEnable = isRepeated;
        this.repeatInterval = repeatInterval;
    }

    /**
     * Alarm with sheduler
     * @param isEnable
     * @param time
     * @param bias
     * @param onMonday
     * @param onTuesday
     * @param onWednesday
     * @param onThursday
     * @param onFriday
     * @param onSaturday
     * @param onSunday
     * @param isRepeated
     * @param repeatInterval
     */
    public Alarm(int isEnable, long time, int bias, int onMonday, int onTuesday,
                 int onWednesday, int onThursday, int onFriday, int onSaturday,
                 int onSunday, int isRepeated, int repeatInterval) {
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
        this.isEnable = isRepeated;
        this.repeatInterval = repeatInterval;
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
}
