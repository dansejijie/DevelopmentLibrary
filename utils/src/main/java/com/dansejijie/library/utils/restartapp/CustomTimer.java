package com.dansejijie.library.utils.restartapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by dansejijie on 17/4/5.
 */

public class CustomTimer {

    public static void start(Context context){

        Intent intent=new Intent(AlarmReceiver.INTENT_ALARM_LOG);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar=Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,8);

        calendar.set(Calendar.MINUTE,30);

        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,,,pendingIntent);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),1000*60*60*24,pendingIntent);
    }
}
