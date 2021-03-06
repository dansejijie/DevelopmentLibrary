package com.lightappbuilder.lab4.lablibrary;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.facebook.react.ReactApplication;
import com.facebook.react.modules.network.OkHttpClientProvider;
import com.lightappbuilder.lab4.lablibrary.utils.AppContextUtils;
import com.lightappbuilder.lab4.lablibrary.utils.L;
import com.lightappbuilder.lab4.lablibrary.utils.ProcessUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by yinhf on 16/7/13.
 */
public abstract class LABApplication extends Application implements ReactApplication {

    @Override
    public void onCreate() {
        MobclickAgent.setCatchUncaughtExceptions(false);
        super.onCreate();

        //ProcessUtils.LogCurProcessInfo(this);
        AppContextUtils.init(this);
        Config.DEBUG = isDebug();
        L.setAllLevelLoggable(Config.DEBUG);
        if(ProcessUtils.isDefaultProcess(this)) {
            mainProcessOnCreate();
        }
    }

    //app 主进程onCreate
    protected void mainProcessOnCreate() {
        if(Build.VERSION.SDK_INT >= 19 && Config.DEBUG) {
            //启用chrome调试
            WebView.setWebContentsDebuggingEnabled(true);
        }
        initOkHttp();
    }

    protected boolean isDebug() {
        return false;
    }

    //是否禁止发送异常报告
    protected boolean disableReportCrash() {
        return Config.DEBUG;
    }

    /**
     * 初始化okhttp 默认只在mainProcessOnCreate中调用
     * 如果其它进程需要则可手动调用
     */
    protected void initOkHttp() {
        OkHttpClientProvider.replaceOkHttpClient(createOkHttpClient());
    }

    protected OkHttpClient createOkHttpClient() {
        File cacheFile = new File(getCacheDir(), "lab-okhttp-cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb
        return OkHttpClientProvider.getOkHttpClient().newBuilder()
                .cache(cache)
                .addInterceptor(new LABNetworkInterceptor())
                .build();
    }
}
