package com.hyphenate.chatuidemo;

/**
 * Created by dansejijie on 17/5/2.
 */

public class IMOptions {

    public IMOptions(){

    }

    private EaseNotificationInfoProvider provider;

    public void setProvider(EaseNotificationInfoProvider provider) {
        this.provider = provider;
    }

    public EaseNotificationInfoProvider getProvider() {

        return provider;
    }
}
