package com.dansejijie.library.utils.restartapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dansejijie on 17/4/5.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static String INTENT_ALARM_LOG="intent_alarm_log";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action==INTENT_ALARM_LOG){
            //// TODO: 17/4/5  
        }
    }
}
