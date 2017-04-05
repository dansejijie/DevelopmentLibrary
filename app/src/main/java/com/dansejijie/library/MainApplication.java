package com.dansejijie.library;

import android.app.Application;

import com.dansejijie.library.utils.restartapp.CrashHandler;

/**
 * Created by tygzx on 17/2/27.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }
}
