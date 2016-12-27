package com.example.trickyalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Almaz on 21.12.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Function that do some action after alarm is on.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();
        Intent intent1 = new Intent(context, SignalActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("id", intent.getStringExtra("id"));
        context.startActivity(intent1);
        wakeLock.release();
    }

    /**
     * Function that set a new alarm.
     * @param context
     * @param alarm which should work.
     */
    public void setAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id", alarm.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getID().hashCode(), intent, 0);
        Calendar calendar = alarm.getTime();
        long time = calendar.getTimeInMillis();
        long timeWithBias = time - alarm.getBias() * 1000;
        Random random = new Random(System.currentTimeMillis());
        long randomTime = time;
        if(alarm.getBias() != 0) {
            randomTime = (long) random.nextInt(alarm.getBias() * 1000) + timeWithBias;
        }
        if(alarm.getRepeatInterval() == 0){
            alarmManager.set(AlarmManager.RTC_WAKEUP, randomTime, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, randomTime, alarm.getRepeatInterval() * 1000, pendingIntent);
        }
    }

    /**
     * Function that set a new alarm.
     * @param context
     * @param time in which alarm should work.
     */
    public void setAlarm(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    /**
     * Function that cancel alarm.
     * @param context
     */
    public void cancelAlarm(Context context, Alarm alarm){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getID().hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
