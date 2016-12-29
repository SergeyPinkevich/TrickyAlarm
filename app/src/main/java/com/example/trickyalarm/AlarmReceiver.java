package com.example.trickyalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.trickyalarm.database.AlarmRepo;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by Almaz on 21.12.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    final public static String id = "id";
    boolean first = true;
    Context context1;

    /**
     * AlarmReceiver constructor.l
     * @param applicationContext
     */
    public AlarmReceiver(Context applicationContext) {
        context1 = applicationContext;
    }

    /**
     * Empty constructor.
     */
    public AlarmReceiver() {
    }

    /**
     * Function that do some action after alarm is on.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "");
        wakeLock.acquire();
        Intent intent1 = new Intent(context, SignalActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra(id, intent.getStringExtra(id));
        AlarmRepo alarmRepo = new AlarmRepo(context);
        if (!intent.getStringExtra(id).isEmpty() && alarmRepo.getAlarmById(intent.getStringExtra(id)).isRepeated()) {
            first = false;
            setAlarm(context, alarmRepo.getAlarmById(intent.getStringExtra(id)));
        }
        context.startActivity(intent1);
        wakeLock.release();
    }

    /**
     * Method that change calendar of alarm to next alarm.
     * @param alarm
     */
    public void nextAlarmTime(Alarm alarm){
        Calendar calendar = alarm.getTime();
        boolean[] array = {alarm.isOnSunday(), alarm.isOnMonday(), alarm.isOnTuesday(), alarm.isOnWednesday(), alarm.isOnThursday(),
                alarm.isOnFriday(), alarm.isOnSaturday()};
        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int end = i;

        if(!first) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if(i == 6){
                i = 0; end = 0;
            } else{
                i++; end++;
            }
        }

        do{
            if(array[i])
                break;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if(i == 6)
                i = -1;
            i++;
        } while(i != end);

        alarm.setTime(calendar);
    }

    /**
     * Function that set a new alarm.
     * @param context
     * @param alarm which should work.
     */
    public void setAlarm(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(id, alarm.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getID().hashCode(), intent, 0);

        if(alarm.isRepeated()){
            nextAlarmTime(alarm);
        }

        Calendar calendar = alarm.getTime();
        AlarmRepo alarmRepo = new AlarmRepo(context1);
        alarmRepo.updateAlarm(alarm);

        long time = calendar.getTimeInMillis();
        long timeWithBias = time - alarm.getBias() * 60000;
        Random random = new Random(System.currentTimeMillis());
        long randomTime = time;

        if(alarm.getBias() != 0)
            randomTime = (long) random.nextInt(alarm.getBias() * 60000) + timeWithBias;

        alarmManager.set(AlarmManager.RTC_WAKEUP, randomTime, pendingIntent);
    }

    /**
     * Function that set a new alarm.
     * @param context
     * @param time in which alarm should work.
     */
    public void setAlarm(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -1, intent, 0);
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
