package com.netease.nim.uikit.extra.delete;

import android.content.Context;

/**
 * Created by tygzx on 17/5/8.
 */

public class AppContextUtils {

    private static Context sAppContext;

    public static Context get() {
        if(sAppContext == null) {
            throw new NullPointerException("the context is null, please init AppContextUtils in Application first.");
        }
        return sAppContext;
    }

    public static void init(Context context) {
        sAppContext = context;
    }
}
