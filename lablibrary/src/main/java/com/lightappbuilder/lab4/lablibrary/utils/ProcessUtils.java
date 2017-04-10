package com.lightappbuilder.lab4.lablibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class ProcessUtils {

    private ProcessUtils() {

    }

    /**
     * 获取当前进程名
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if(appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**
     * 判断当前进程是否为默认进程(跟包名相同)
     */
    public static boolean isDefaultProcess(Context context) {
        String packageName = context.getPackageName();
        String curProcessName = getCurProcessName(context);
        Log.i("ProcessUtils", "isDefaultProcess packageName=" + packageName + "  |  curProcessName=" + curProcessName);
        return packageName.equalsIgnoreCase(curProcessName);
    }

    /**
     * 判断当前进程是否为子进程(android:process=":xxx")
     */
    public static boolean isChildProcess(Context context, String childName) {
        String curProcessName = getCurProcessName(context);
        return curProcessName.endsWith(":" + childName);
    }

    /**
     * Log进程id与进程名
     */
    public static void logProcessInfo(Context context) {
        int pid = android.os.Process.myPid();
        String packageName = context.getPackageName();
        StringBuilder sb = new StringBuilder();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        sb.append("进程列表=====================================\n");
        for(ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if(appProcess.pid == pid) {
                sb.append("当前进程>>>> ");
            } else if(appProcess.processName.startsWith(packageName + ":")) {
                sb.append("当前包>>>>> ");
            }
            sb.append("Process pid=").append(appProcess.pid).append(" | processName=").append(appProcess.processName).append('\n');
        }
        sb.append("进程列表结束====================================");
        Log.i("logProcessInfo", sb.toString());
    }
}
