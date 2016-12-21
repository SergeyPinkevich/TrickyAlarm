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

    /**
     * Single alarm (without scheduler depending on weekdays)
     * @param isEnable
     * @param time
     * @param bias
     * @param isRepeated
     * @param repeatInterval
     */
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

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
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

    public boolean getOnMonday() {
        return onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean getOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean getOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean getOnThursday() {
        return onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean getOnFriday() {
        return onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean getOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    public boolean getOnSunday() {
        return onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
