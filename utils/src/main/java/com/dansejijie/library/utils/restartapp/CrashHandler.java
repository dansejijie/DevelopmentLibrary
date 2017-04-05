package com.dansejijie.library.utils.restartapp;

/**
 * Created by dansejijie on 17/4/5.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler{


    public CrashHandler() {

    }


    //完美配合bugly异常上传，bugly工作人员回复只要UncaughtExceptionHandler的注册在bugly注册之前即可。
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        ReStartApp.start1();
    }


}
