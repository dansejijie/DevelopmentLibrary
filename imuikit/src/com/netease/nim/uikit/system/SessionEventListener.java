package com.netease.nim.uikit.system;

import android.content.Context;

/**
 * Created by tygzx on 17/1/7.
 */

public interface SessionEventListener {

    // 头像点击事件处理，一般用于打开用户资料页面
    void onAvatarClicked(Context context, IMMessage message);

    // 头像长按事件处理，一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
    void onAvatarLongClicked(Context context, IMMessage message);
}