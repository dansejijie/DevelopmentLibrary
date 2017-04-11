package com.netease.nimlib.sdk;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansejijie on 17/4/4.
 */

public class NIMClient {

    static NIMClient instanece;

    public NIMClient() {
    }

    public static void init(Context var0, LoginInfo var1, SDKOptions var2) {
        //b.a(var0, var1, var2);
    }

    public static NIMClient getService(Class var0) {

        if (instanece == null) {
            instanece = new NIMClient();
        }
        return instanece;
    }

    public AbortableFuture downloadAttachment(IMMessage imMessage, boolean b) {

        return new AbortableFuture() {
            @Override
            public boolean abort() {
                return false;
            }

            @Override
            public void setCallback(RequestCallback var1) {

            }
        };
    }

    public InvocationFuture queryMessageListByType(final MsgTypeEnum msgTypeEnum, final String account) {

        return new InvocationFuture() {
            @Override
            public void setCallback(RequestCallback var1) {
                try {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(account);
                    List<EMMessage> msgs = new ArrayList<>();
                    if (conversation != null) {
                        List<EMMessage> temp = conversation.getAllMessages();
                        if (temp != null && temp.size() > 0) {
                            for (int i = 0; i < temp.size(); i++) {
                                if (temp.get(i).getType() == MsgTypeEnum.IMMessageMsgTypeEnumConvertToEMMessageMsgTypeEnum(msgTypeEnum)) {
                                    if (temp.get(i).status() == EMMessage.Status.SUCCESS) {
                                        msgs.add(temp.get(i));
                                    }

                                }
                            }
                            var1.onSuccess(msgs);
                        } else {
                            var1.onFailed(0);
                        }
                    } else {
                        var1.onFailed(0);
                    }
                } catch (Exception e) {
                    var1.onException(e);
                }
            }
        };
    }

    public void observeMsgStatus(Observer<IMMessage> imMessageObserver, boolean b) {

    }

    public void sendCustomNotification(CustomNotification config) {

    }

    public AbortableFuture transVoiceToText(String url, String path, long duration) {

        return new AbortableFuture() {
            @Override
            public boolean abort() {
                return false;
            }

            @Override
            public void setCallback(RequestCallback var1) {

            }
        };

    }

    public void observeCustomNotification(Observer<CustomNotification> customNotification, boolean b) {
    }


    public void sendMessage(IMMessage imMessage, boolean b) {

        EMClient.getInstance().chatManager().sendMessage(imMessage.getEMMessage());
    }

    public void setChattingAccount(String sessionId, SessionTypeEnum sessionTypeEnum) {

    }

    public void deleteChattingHistory(IMMessage imMessage) {
        //// TODO: 17/4/10
        EMClient.getInstance().chatManager().getConversation(imMessage.getEMMessage().getUserName()).removeMessage(imMessage.getUuid());
    }

    public void sendMessageReceipt(String account, IMMessage imMessage) {

    }

    public void updateIMMessageStatus(IMMessage imMessage) {
        EMClient.getInstance().chatManager().updateMessage(imMessage.getEMMessage());
    }

    private InvocationFuture queryMessageListExInvocationFuture;
    private RequestCallback queryRequestCallback;

    public InvocationFuture queryMessageListEx(final IMMessage anchor, QueryDirectionEnum direction, final int count, boolean b) {

        return new InvocationFuture() {
            @Override
            public void setCallback(RequestCallback var1) {


                String userName = anchor.getUserName();
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);

                if (conversation == null) {
                    var1.onFailed(0);
                } else {

                    conversation.markAllMessagesAsRead();
                    List<EMMessage> messages = conversation.loadMoreMsgFromDB(anchor.getUuid(), count);

                    if (messages != null && messages.size() > 0) {
                        List<IMMessage> messages1 = new ArrayList<>();
                        for (int i = 0; i < messages.size(); i++) {
                            messages1.add(new IMMessage(messages.get(i)));
                        }
                        var1.onSuccess(messages1);
                    } else {
                        var1.onFailed(0);
                    }
                }
            }
        };
    }

    public InvocationFuture pullMessageHistory(IMMessage anchor, int count, boolean b) {

        return new InvocationFuture() {
            @Override
            public void setCallback(RequestCallback var1) {
                var1.onFailed(0);
            }
        };
    }

    public InvocationFuture revokeMessage(IMMessage imMessage) {
        return new InvocationFuture() {
            @Override
            public void setCallback(RequestCallback var1) {
                var1.onFailed(0);
            }
        };
    }


//    private EMMessageListener msgListener = new EMMessageListener() {
//
//        @Override
//        public void onMessageReceived(List<EMMessage> messages) {
//            //收到消息
//        }
//
//        @Override
//        public void onCmdMessageReceived(List<EMMessage> messages) {
//            //收到透传消息
//        }
//
//        @Override
//        public void onMessageRead(List<EMMessage> messages) {
//            //收到已读回执
//        }
//
//        @Override
//        public void onMessageDelivered(List<EMMessage> message) {
//            //收到已送达回执
//        }
//
//        @Override
//        public void onMessageChanged(EMMessage message, Object change) {
//            //消息状态变动
//        }
//    };
//
//    public void registerObserver(boolean register){
//        if (register){
//            EMClient.getInstance().chatManager().addMessageListener(msgListener);
//
//        }else {
//            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
//        }
//    }


    public static void getService() {

        //return b.a(var0);
    }


//    public static StatusCode getStatus() {
//        return d.e();
//    }

    public static void toggleNotification(boolean var0) {

    }

    public static void updateStatusBarNotificationConfig(StatusBarNotificationConfig var0) {

    }

    public static void updateStrings(NimStrings var0) {
//        b.a(var0);
    }

    public static String getSdkStorageDirPath() {
        return " ";
    }

    class a implements AbortableFuture<String> {
        @Override
        public boolean abort() {
            return false;
        }

        @Override
        public void setCallback(RequestCallback var1) {

        }
    }
}
