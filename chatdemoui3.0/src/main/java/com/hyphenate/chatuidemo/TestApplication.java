package com.hyphenate.chatuidemo;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by dansejijie on 17/4/11.
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options=new EMOptions();

        EMClient.getInstance().init(this,options);
    }
}
