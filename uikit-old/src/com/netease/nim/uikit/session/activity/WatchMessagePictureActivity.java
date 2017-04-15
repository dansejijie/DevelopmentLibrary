
package com.netease.nim.uikit.session.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.session.view.PhotoBrowserView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 查看聊天消息原图
 * Created by huangjun on 2015/3/6.
 */
public class WatchMessagePictureActivity extends Activity {

    private static final String INTENT_EXTRA_IMAGE_ACCOUNT = "INTENT_EXTRA_IMAGE_ACCOUNT";
    private static final String INTENT_EXTRA_IMAGE_MESSAGEID = "INTENT_EXTRA_IMAGE_MESSAGEID";
    private static final String TAG = WatchMessagePictureActivity.class.getSimpleName();
    private List<EMMessage> imageMsgList = new ArrayList<>();
    private int firstDisplayImageIndex = 0;
    private String messionId = null;
    private String firstMessageId = null;
    private PhotoBrowserView photoBrowserView;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_IMAGE_ACCOUNT, message.getFromAccount());
        intent.putExtra(INTENT_EXTRA_IMAGE_MESSAGEID, message.getSessionId());
        intent.setClass(context, WatchMessagePictureActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_watch_picture_activity);
        photoBrowserView = (PhotoBrowserView) findViewById(R.id.photoBrowserView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        messionId = getIntent().getExtras().getString(INTENT_EXTRA_IMAGE_ACCOUNT);
        firstMessageId = getIntent().getExtras().getString(INTENT_EXTRA_IMAGE_MESSAGEID);
        if (TextUtils.isEmpty(messionId) || TextUtils.isEmpty(firstMessageId)) {
            finish();
        }
        queryImageMessages();

    }
    private void queryImageMessages() {

        NIMClient.getService(WatchMessagePictureActivity.class).queryMessageListByType(MsgTypeEnum.image, messionId).setCallback(new RequestCallbackWrapper<List<EMMessage>>() {
            @Override
            public void onResult(int var1, List<EMMessage> var2, Throwable var3) {
                if (var1== ResponseCode.RES_SUCCESS){
                    imageMsgList.addAll(var2);
                    Collections.reverse(imageMsgList);
                    setDisplayIndex();
                    photoBrowserView.setOptions(imageMsgList, firstDisplayImageIndex);
                }else {
                    Toast.makeText(WatchMessagePictureActivity.this,"当前无图片",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
    // 设置第一个选中的图片index
    private void setDisplayIndex() {
        for (int i = 0; i < imageMsgList.size(); i++) {
           if (imageMsgList.get(i).getMsgId().equals(firstMessageId)){
                firstDisplayImageIndex = i;
                break;
            }
        }
    }

}

