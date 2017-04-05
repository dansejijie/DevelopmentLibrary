package com.dansejijie.library.utils.restartapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Process;

/**
 * Created by dansejijie on 17/4/5.
 */

public class ReStartApp {

    private static Application context;

    public static void init(Application application){
        context=application;
    }

    public static void start1(){

        Intent i = context.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(context.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);

    }

    //当默认启动的activity的launchMode 设置为不是以”standard” 方式启动时，这个时候重启app不能清除activity中的内存数据。
    public static void start2(){
        Intent intent = context.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(context.getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        System.exit(0);
        //android.os.Process.killProcess(Process.myPid());
    }

    public static void normalKillApp(){
        android.os.Process.killProcess(Process.myPid());
    }

}
