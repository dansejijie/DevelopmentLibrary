package com.netease.nim.uikit.system;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.netease.nim.uikit.system.RequestCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengqiang on 2016/12/31.
 */

public enum MsgStatusEnum {
    //draft(-1),
    sending(EMMessage.Status.INPROGRESS),
    success(EMMessage.Status.SUCCESS),
    fail(EMMessage.Status.FAIL);
    //read(3),
    //unread(4);

    private EMMessage.Status value;

    private MsgStatusEnum(EMMessage.Status var3) {
        this.value = var3;
    }

    public static MsgStatusEnum statusOfValue(EMMessage.Status var0) {
        MsgStatusEnum[] var1;
        int var2 = (var1 = values()).length;

        for(int var3 = 0; var3 < var2; ++var3) {
            MsgStatusEnum var4;
            if((var4 = var1[var3]).getValue() == var0) {
                return var4;
            }
        }
        return sending;
    }

    public final EMMessage.Status getValue() {
        return this.value;
    }

    /**
     * Created by zhengqiang on 2016/12/31.
     */

    public static class NIMClient {

        private static NIMClient instance;

        private NIMClient() {

        }

        public synchronized static NIMClient getInstance() {
            if (instance == null) {
                instance = new NIMClient();
            }
            return instance;
        }

        public synchronized static NIMClient getService(Class clazz) {
            if (instance == null) {
                instance = new NIMClient();
            }
            return instance;
        }

        public void sendMessage(IMMessage message, boolean b) {
            EMClient.getInstance().chatManager().sendMessage(message.getEMMessage());
        }

        public void deleteChattingHistory(IMMessage message) {
            EMClient.getInstance().chatManager().getConversation(message.getUserName()).removeMessage(message.getUuid());
        }

        public void sendMessageReceipt(String account, IMMessage message) throws HyphenateException {
            EMClient.getInstance().chatManager().ackMessageRead(account, message.getUuid());
        }

        public List<IMMessage> queryMessageListEx(IMMessage message, int pageSize, boolean b) {
            String userName = null;
            if (message.getDirect() == MsgDirectionEnum.In) {
                userName = message.getFrom();
            } else {
                userName = message.getTo();
            }
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);

            List<IMMessage> messages1 = new ArrayList<>();
            if (conversation != null) {
                conversation.markAllMessagesAsRead();
                List<EMMessage> messages = conversation.loadMoreMsgFromDB(message.getUuid(), pageSize);

                if (messages != null && messages.size() > 0) {
                    for (int i = 0; i < messages.size(); i++) {
                        messages1.add(new IMMessage(messages.get(i)));
                    }
                }
            }
            return messages1;
        }

        public void observeReceiveMessage(EMMessageListener incomingMessageObserver, boolean register) {
            if (register) {
                EMClient.getInstance().chatManager().addMessageListener(incomingMessageObserver);
            } else {
                EMClient.getInstance().chatManager().removeMessageListener(incomingMessageObserver);
            }
        }


        public static void queryMessageListByType(MsgTypeEnum type, String account, RequestCallback<List<EMMessage>> callback) {

            try {

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(account);
                List<EMMessage> msgs = new ArrayList<>();
                if (conversation != null) {
                    List<EMMessage> temp = conversation.getAllMessages();
                    if (temp != null && temp.size() > 0) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getType() == type.getValue()) {
                                if (temp.get(i).status() == EMMessage.Status.SUCCESS) {
                                    msgs.add(temp.get(i));
                                }

                            }
                        }
                        callback.onSuccess(msgs);
                    } else {
                        callback.onFailed(0);
                    }
                } else {
                    callback.onFailed(0);
                }
            } catch (Exception e) {
                callback.onException(e);
            }
        }

    }
}
