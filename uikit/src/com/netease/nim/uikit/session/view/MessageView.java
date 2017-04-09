package com.netease.nim.uikit.session.view;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.netease.nim.uikit.CustomPushContentProvider;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.view.TView;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.actions.ImageAction;
import com.netease.nim.uikit.session.actions.LocationAction;
import com.netease.nim.uikit.session.actions.VideoAction;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nim.uikit.session.module.input.InputPanel;
import com.netease.nim.uikit.session.module.list.MessageListPanelEx;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dansejijie on 17/4/4.
 */

public class MessageView extends TView implements ModuleProxy {


    public MessageView(Context context) {
        super(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.nim_message_fragment, null, false);

    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater=LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.nim_message_fragment, null, false);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.nim_message_fragment, null, false);
    }

    private View rootView;

    private SessionCustomization customization;

    protected static final String TAG = "MessageActivity";

    // 聊天对象
    protected String sessionId; // p2p对方Account或者群id

    protected SessionTypeEnum sessionType;

    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;

    private Bundle mArguments;

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        parseIntent();
//    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
//        return rootView;
//    }

    /**
     * ***************************** life cycle *******************************
     */


    public void onPause() {

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                SessionTypeEnum.None);
        if (messageListPanel!=null){
            messageListPanel.onPause();
        }
        if (inputPanel!=null){
            inputPanel.onPause();
        }


    }


    public void onResume() {


        if (messageListPanel!=null){
            messageListPanel.onResume();
        }
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 默认使用听筒播放
    }


    public void onDestroy() {

        if (messageListPanel!=null){
            messageListPanel.onPause();
        }
        if (inputPanel!=null){
            inputPanel.onPause();
        }
        registerObservers(false);
    }

    public boolean onBackPressed() {
        if (inputPanel.collapse(true)) {
            return true;
        }

        if (messageListPanel.onBackPressed()) {
            return true;
        }
        return false;
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        sessionId = getArguments().getString(Extras.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) getArguments().getSerializable(Extras.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) getArguments().getSerializable(Extras.EXTRA_ANCHOR);

        customization = (SessionCustomization) getArguments().getSerializable(Extras.EXTRA_CUSTOMIZATION);
        Container container = new Container(getActivity(), sessionId, sessionType, this);

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList());
            inputPanel.setCustomization(customization);
        } else {
            inputPanel.reload(container, customization);
        }

        registerObservers(true);

        if (customization != null) {
            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
        }
    }

    /**
     * ************************* 消息收发 **********************************
     */
    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return true;
    }

    /**
     * ****************** 观察者 **********************
     */

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            if (messages == null || messages.isEmpty()) {
                return;
            }

            List<IMMessage>imMessages=new ArrayList<>();
            for (EMMessage emMessage:messages){
                imMessages.add(new IMMessage(emMessage));
            }
            messageListPanel.onIncomingMessage(imMessages);
            sendMsgReceipt(); // 发送已读回执
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            receiveReceipt();

        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    private void registerObservers(boolean register) {
//        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
//        service.observeReceiveMessage(incomingMessageObserver, register);
//        service.observeMessageReceipt(messageReceiptObserver, register);

        if (register){
            EMClient.getInstance().chatManager().addMessageListener(msgListener);
        }else {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            messageListPanel.onIncomingMessage(messages);
            sendMsgReceipt(); // 发送已读回执
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            receiveReceipt();
        }
    };


    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
        if (!isAllowSendMessage(message)) {
            return false;
        }
//        appendPushConfig(message);
        // send message to server and save to db
        NIMClient.getService(MsgService.class).sendMessage(message, false);

        messageListPanel.onMsgSend(message);

        return true;
    }

//    private void appendPushConfig(IMMessage message) {
//        CustomPushContentProvider customConfig = NimUIKit.getCustomPushContentProvider();
//        if (customConfig != null) {
//            String content = customConfig.getPushContent(message);
//            Map<String, Object> payload = customConfig.getPushPayload(message);
//            message.setPushContent(content);
//            message.setPushPayload(payload);
//        }
//    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new ImageAction());
        actions.add(new VideoAction());
        actions.add(new LocationAction());

        if (customization != null && customization.actions != null) {
            actions.addAll(customization.actions);
        }
        return actions;
    }

    /**
     * 发送已读回执
     */
    private void sendMsgReceipt() {
        messageListPanel.sendReceipt();
    }

    /**
     * 收到已读回执
     */
    public void receiveReceipt() {
        messageListPanel.receiveReceipt();
    }

    public Bundle getArguments(){
        return mArguments;
    }

    public void setArguments(Bundle bundle){
        mArguments=bundle;
        parseIntent();
    }

    public View getView(){
        return rootView;
    }
}
