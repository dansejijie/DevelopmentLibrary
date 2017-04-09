package com.netease.nim.uikit.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.netease.nim.uikit.R;
//import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nim.uikit.session.view.MessageView;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends Activity{

    private boolean isResume = false;

    private MessageView messageView;

    private String sessionId;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        registerObservers(true);



        messageView=new MessageView(this);



        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(messageView.getView(),lp);


        EMClient.getInstance().login("dansejijie","xhzxzq15",new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                Bundle bundle=new Bundle();
                bundle.putString(Extras.EXTRA_ACCOUNT,"dansejijie2");
                bundle.putSerializable(Extras.EXTRA_TYPE,SessionTypeEnum.P2P);
                messageView.setArguments(bundle);

                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        messageView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        messageView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        messageView.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        messageView.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        messageView.onBackPressed();
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
//        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

//    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
//        @Override
//        public void onAddedOrUpdatedFriends(List<String> accounts) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onDeletedFriends(List<String> accounts) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onAddUserToBlackList(List<String> account) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//
//        @Override
//        public void onRemoveUserFromBlackList(List<String> account) {
//            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
//        }
//    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

//    @Override
//    protected MessageFragment fragment() {
//        Bundle arguments = getIntent().getExtras();
//        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
//        MessageFragment fragment = new MessageFragment();
//        fragment.setArguments(arguments);
//        fragment.setContainerId(R.id.message_fragment_container);
//        return fragment;
//    }
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.nim_message_activity;
//    }
//
//    @Override
//    protected void initToolBar() {
//        ToolBarOptions options = new ToolBarOptions();
//        setToolBar(R.id.toolbar, options);
//    }
}
