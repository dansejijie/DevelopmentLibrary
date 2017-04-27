package com.dansejijie.library;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;


/**
 * Created by tygzx on 17/2/27.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getApplicationContext());
    }
}
