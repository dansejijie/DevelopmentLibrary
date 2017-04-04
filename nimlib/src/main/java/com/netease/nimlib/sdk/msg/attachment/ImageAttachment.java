package com.netease.nimlib.sdk.msg.attachment;

import android.util.Log;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;

/**
 * Created by dansejijie on 17/4/4.
 */

public class ImageAttachment extends FileAttachment{
    private int width;
    private int height;
    private static final String KEY_WIDTH = "w";
    private static final String KEY_HEIGHT = "h";

    public ImageAttachment(EMMessageBody body){
        super(body);
    }

    public int getWidth() {
        Log.e("TAG","unhandler");
        return 100;
    }

    public void setWidth(int var1) {
        Log.e("TAG","unhandler");
        this.width = var1;
    }

    public int getHeight() {
        Log.e("TAG","unhandler");
        return 100;
    }

    public void setHeight(int var1) {
        Log.e("TAG","unhandler");
        this.height = var1;
    }

    public boolean isHdImage() {
        Log.e("TAG","unhandler");
        return false;
    }
}
