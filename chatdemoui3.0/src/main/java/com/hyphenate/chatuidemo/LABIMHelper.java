//package com.hyphenate.chatuidemo;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.hyphenate.EMCallBack;
//import com.hyphenate.EMConnectionListener;
//import com.hyphenate.EMContactListener;
//import com.hyphenate.EMError;
//import com.hyphenate.EMGroupChangeListener;
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.EMValueCallBack;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMGroup;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.chat.EMMessage.ChatType;
//import com.hyphenate.chat.EMMessage.Status;
//import com.hyphenate.chat.EMMessage.Type;
//import com.hyphenate.chat.EMOptions;
//import com.hyphenate.easeui.controller.EaseUI;
//import com.hyphenate.easeui.controller.EaseUI.EaseSettingsProvider;
//import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
//import com.hyphenate.easeui.model.EaseAtMessageHelper;
//import com.hyphenate.easeui.model.EaseNotifier;
//import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
//import com.hyphenate.easeui.utils.EaseCommonUtils;
//import com.hyphenate.exceptions.HyphenateException;
//import com.hyphenate.util.EMLog;
//import com.lightappbuilder.lab4.lablibrary.rnmodules.notification.LABNotificationModule;
//import com.lightappbuilder.lab4.lablibrary.utils.IntentUtils;
//import com.netease.nim.uikit.NimUIKit;
//import com.netease.nim.uikit.cache.DataCacheManager;
//import com.netease.nim.uikit.contact.core.query.PinYin;
//import com.netease.nim.uikit.extra.session.activity.EMImageLoadHelper;
//import com.netease.nim.uikit.extra.uinfo.model.EaseUser;
//import com.netease.nim.uikit.extra.uinfo.model.RobotUser;
//import com.netease.nim.uikit.session.SessionEventListener;
//import com.netease.nim.uikit.session.constant.Extras;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.msg.model.IMMessage;
//import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class LABIMHelper {
//    /**
//     * data sync listener
//     */
//    static public interface DataSyncListener {
//        /**
//         * sync complete
//         *
//         * @param success true：data sync successful，false: failed to sync data
//         */
//        public void onSyncComplete(boolean success);
//    }
//
//    protected static final String TAG = "LABIMHelper";
//
//    private EaseUI easeUI;
//
//    /**
//     * EMEventListener
//     */
//    protected EMMessageListener messageListener = null;
//
//    private Map<String, EaseUser> contactList;
//
//    private Map<String, RobotUser> robotList;
//
//    private UserProfileManager userProManager;
//
//    private static LABIMHelper instance = null;
//
//    private DemoModel demoModel = null;
//
//    /**
//     * sync groups status listener
//     */
//    private List<DataSyncListener> syncGroupsListeners;
//    /**
//     * sync contacts status listener
//     */
//    private List<DataSyncListener> syncContactsListeners;
//    /**
//     * sync blacklist status listener
//     */
//    private List<DataSyncListener> syncBlackListListeners;
//
//    private boolean isSyncingGroupsWithServer = false;
//    private boolean isSyncingContactsWithServer = false;
//    private boolean isSyncingBlackListWithServer = false;
//    private boolean isGroupsSyncedWithServer = false;
//    private boolean isContactsSyncedWithServer = false;
//    private boolean isBlackListSyncedWithServer = false;
//
//    public boolean isVoiceCalling;
//    public boolean isVideoCalling;
//
//    private String username;
//
//    private Context appContext;
//
////    private CallReceiver callReceiver;
//
//    private EMConnectionListener connectionListener;
//
//    private InviteMessgeDao inviteMessgeDao;
//    private UserDao userDao;
//
//    private LocalBroadcastManager broadcastManager;
//
//    private boolean isGroupAndContactListenerRegisted;
//
//    private LABIMHelper() {
//    }
//
//    public synchronized static LABIMHelper getInstance() {
//        if (instance == null) {
//            instance = new LABIMHelper();
//        }
//        return instance;
//    }
//
//    /**
//     * init helper
//     *
//     * @param context application context
//     */
//    public void init(Context context) {
//
//
//        EMImageLoadHelper.init(context);
//        Fresco.initialize(context);
//
//        //环信
//        EMOptions options=initChatOptions();
//        EMClient.getInstance().init(context, options);
//
//        EMClient.getInstance().setDebugMode(true);
//        //end
//
//        // init pinyin
//        PinYin.init(context);
//        PinYin.validate();
//
//        // 初始化UIKit模块
//        initUIKit(context);
//
//        //这里不该有，记的放在成功登陆的时候
//        NimUIKit.setAccount("dansejijie");
//        DataCacheManager.buildDataCache();
//        //end
//
//        // 注册通知消息过滤器
//        registerIMMessageFilter();
//
//        // 初始化消息提醒
//        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
//
//        // 注册网络通话来电
//        enableAVChat();
//
//        // 注册白板会话
//        enableRTS();
//
//        // 注册语言变化监听
//        registerLocaleReceiver(true);
//
//
//
//
//        /**********************/
//        demoModel = new DemoModel(context);
//        //use default options if options is null
//
//        if (EaseUI.getInstance().init(context, options)) {
//            appContext = context;
//
//            //debug mode, you'd better set it to false, if you want release your App officially.
//            EMClient.getInstance().setDebugMode(true);
//            //get easeui instance
//            easeUI = EaseUI.getInstance();
//            //to set user's profile and avatar
//            setEaseUIProviders();
//            //initialize preference manager
//            PreferenceManager.init(context);
//            //initialize profile manager
//            getUserProfileManager().init(context);
//
//            //EMClient.getInstance().callManager().getVideoCallHelper().setAdaptiveVideoFlag(getModel().isAdaptiveVideoEncode());//TODO
//
//
//            setGlobalListeners();
//            broadcastManager = LocalBroadcastManager.getInstance(appContext);
//            initDbDao();
//        }
//    }
//
//
//    private void initUIKit(Context context) {
//        // 初始化，使用 uikit 默认的用户信息提供者
//        NimUIKit.init(context);
//
//        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
////        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
////
////        // 会话窗口的定制初始化。
////        SessionHelper.init();
//
//        SessionEventListener listener = new SessionEventListener() {
//            @Override
//            public void onAvatarClicked(Context context, IMMessage message) {
//                // 一般用于打开用户资料页面
//                Toast.makeText(context,message.getFrom(),Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onAvatarLongClicked(Context context, IMMessage message) {
//                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
//            }
//        };
//
//        NimUIKit.setSessionListener(listener);
//
//
//        // 通讯录列表定制初始化
////        ContactHelper.init();
//
//        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
//        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());
//    }
//
//    public void registerIMMessageFilter(){
//
//    }
//
//    public void enableAVChat(){
//
//    }
//
//    public void enableRTS(){
//
//    }
//
//    public void registerLocaleReceiver(boolean b){
//
//    }
//
//
//    private EMOptions initChatOptions() {
//        EMOptions options = new EMOptions();
//        // set if accept the invitation automatically
//        options.setAcceptInvitationAlways(false);
//        // set if you need read ack
//        options.setRequireAck(true);
//        // set if you need delivery ack
//        options.setRequireDeliveryAck(false);
//
////        //you need apply & set your own id if you want to use google cloud messaging.
////        options.setGCMNumber("324169311137");
////        //you need apply & set your own id if you want to use Mi push notification
////        options.setMipushConfig("2882303761517426801", "5381742660801");
////        //you need apply & set your own id if you want to use Huawei push notification
////        options.setHuaweiPushAppId("10492024");
//
////        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
////        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
////        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());
//
//        return options;
//    }
//
//    protected void setEaseUIProviders() {
//        // set profile provider if you want easeUI to handle avatar and nickname
//        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {
//
//            @Override
//            public EaseUser getUser(String username) {
//                return getUserInfo(username);
//            }
//        });
//        //set options
//        easeUI.setSettingsProvider(new EaseSettingsProvider() {
//
//            @Override
//            public boolean isSpeakerOpened() {
//                return demoModel.getSettingMsgSpeaker();
//            }
//
//            @Override
//            public boolean isMsgVibrateAllowed(EMMessage message) {
//                return demoModel.getSettingMsgVibrate();
//            }
//
//            @Override
//            public boolean isMsgSoundAllowed(EMMessage message) {
//                return demoModel.getSettingMsgSound();
//            }
//
//            @Override
//            public boolean isMsgNotifyAllowed(EMMessage message) {
//                if (message == null) {
//                    return demoModel.getSettingMsgNotification();
//                }
//                if (!demoModel.getSettingMsgNotification()) {
//                    return false;
//                } else {
//                    String chatUsename = null;
//                    List<String> notNotifyIds = null;
//                    // get user or group id which was blocked to show message notifications
//                    if (message.getChatType() == ChatType.Chat) {
//                        chatUsename = message.getFrom();
//                        notNotifyIds = demoModel.getDisabledIds();
//                    } else {
//                        chatUsename = message.getTo();
//                        notNotifyIds = demoModel.getDisabledGroups();
//                    }
//
//                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        });
////        //set emoji icon provider
////        easeUI.setEmojiconInfoProvider(new EaseEmojiconInfoProvider() {
////
////            @Override
////            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
////                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
////                for(EaseEmojicon emojicon : data.getEmojiconList()){
////                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
////                        return emojicon;
////                    }
////                }
////                return null;
////            }
////
////            @Override
////            public Map<String, Object> getTextEmojiconMapping() {
////                return null;
////            }
////        });
//
//        //set notification options, will use default if you don't set it
//        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {
//
//            @Override
//            public String getTitle(EMMessage message) {
//                return null;
//            }
//
//            @Override
//            public int getSmallIcon(EMMessage message) {
//                return 0;
//            }
//
//            @Override
//            public String getDisplayedText(EMMessage message) {
//                // be used on notification bar, different text according the message type.
//                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
//                if (message.getType() == Type.TXT) {
//                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                }
//                EaseUser user = getUserInfo(message.getFrom());
//                if (user != null) {
//                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
//                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
//                    }
//                    return user.getNick() + ": " + ticker;
//                } else {
//                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
//                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
//                    }
//                    return message.getFrom() + ": " + ticker;
//                }
//            }
//
//            @Override
//            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
//                // here you can customize the text.
//                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
//                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
//                if (message.getType() == Type.TXT) {
//                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                }else if (message.getType()==Type.VOICE){
//                    ticker="[语音]";
//                }else if (message.getType()== Type.IMAGE){
//                    ticker="[图片]";
//                }
//                if (messageNum==1){
//                    return ticker;
//                }
//                return null;
//            }
//
//            @Override
//            public Intent getLaunchIntent(EMMessage message) {
////            	// you can set what activity you want display when user click the notification
////                Intent intent = new Intent(appContext, ChatActivity.class);
////                // open calling activity if there is call
////                if(isVideoCalling){
////                    intent = new Intent(appContext, VideoCallActivity.class);
////                }else if(isVoiceCalling){
////                    intent = new Intent(appContext, VoiceCallActivity.class);
////                }else{
////                    ChatType chatType = message.getChatType();
////                    if (chatType == ChatType.Chat) { // single chat message
////                        intent.putExtra("userId", message.getFrom());
////                        intent.putExtra("chatType", Extras.CHATTYPE_SINGLE);
////                    } else { // group chat message
////                        // message.getTo() is the group id
////                        intent.putExtra("userId", message.getTo());
////                        if(chatType == ChatType.GroupChat){
////                            intent.putExtra("chatType", Extras.CHATTYPE_GROUP);
////                        }else{
////                            intent.putExtra("chatType", Extras.CHATTYPE_CHATROOM);
////                        }
////
////                    }
////                }
//
//                Intent intent = IntentUtils.getLaunchIntent(appContext);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Bundle bundle = new Bundle();
//                bundle.putString("lab_notification_type", "IM"); //标记通知类型为IM
//                bundle.putString("targetImId", message.getChatType() == ChatType.Chat ? message.getFrom() : message.getTo());
//                intent.putExtra(LABNotificationModule.NOTIFICATION_INTENT_KEY, bundle);
//                return intent;
//            }
//        });
//    }
//
//    /**
//     * set global listener
//     */
//    protected void setGlobalListeners() {
//        syncGroupsListeners = new ArrayList<DataSyncListener>();
//        syncContactsListeners = new ArrayList<DataSyncListener>();
//        syncBlackListListeners = new ArrayList<DataSyncListener>();
//
//        isGroupsSyncedWithServer = demoModel.isGroupsSynced();
//        isContactsSyncedWithServer = demoModel.isContactSynced();
//        isBlackListSyncedWithServer = demoModel.isBacklistSynced();
//
//        // create the global connection listener
//        connectionListener = new EMConnectionListener() {
//            @Override
//            public void onDisconnected(int error) {
//                Log.i(TAG, "onDisconnected: error:" + error);
//                if (error == EMError.USER_REMOVED) {
////                    onCurrentAccountRemoved();
//                    LABIMModule.onDisconnected("USER_REMOVED");
//                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
////                    onConnectionConflict();
//                    LABIMModule.onDisconnected("USER_LOGIN_ANOTHER_DEVICE");
//                } else {
//                    //LABIMModule.onDisconnected("UNKNOWN_ERROR");
//                }
//            }
//
//            @Override
//            public void onConnected() {
//                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
//                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
//                    EMLog.d(TAG, "group and contact already synced with servre");
//                } else {
//                    if (!isGroupsSyncedWithServer) {
//                        asyncFetchGroupsFromServer(null);
//                    }
//
//                    if (!isContactsSyncedWithServer) {
//                        asyncFetchContactsFromServer(null);
//                    }
//
//                    if (!isBlackListSyncedWithServer) {
//                        asyncFetchBlackListFromServer(null);
//                    }
//                }
//            }
//        };
//
////        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
////        if(callReceiver == null){
////            callReceiver = new CallReceiver();
////        }
//
//        //register incoming call receiver
////        appContext.registerReceiver(callReceiver, callFilter);
//        //register connection listener
//        EMClient.getInstance().addConnectionListener(connectionListener);
//        //register group and contact event listener
//        registerGroupAndContactListener();
//        //register message event listener
//        registerMessageListener();
//
//    }
//
//    private void initDbDao() {
//        inviteMessgeDao = new InviteMessgeDao(appContext);
//        userDao = new UserDao(appContext);
//    }
//
//    /**
//     * register group and contact listener, you need register when login
//     */
//    public void registerGroupAndContactListener() {
//        if (!isGroupAndContactListenerRegisted) {
//            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
//            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
//            isGroupAndContactListenerRegisted = true;
//        }
//
//    }
//
//    class MyGroupChangeListener implements EMGroupChangeListener{
//        @Override
//        public void onInvitationReceived(String s, String s1, String s2, String s3) {
//
//        }
//
//        @Override
//        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {
//
//        }
//
//        @Override
//        public void onRequestToJoinAccepted(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {
//
//        }
//
//        @Override
//        public void onInvitationAccepted(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onInvitationDeclined(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onUserRemoved(String s, String s1) {
//
//        }
//
//        @Override
//        public void onGroupDestroyed(String s, String s1) {
//
//        }
//
//        @Override
//        public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {
//
//        }
//    }
//
//
//
//
//
//    /***
//     * 好友变化listener
//     */
//    public class MyContactListener implements EMContactListener {
//
//        @Override
//        public void onContactAdded(String username) {
//            // save contact
//            Map<String, EaseUser> localUsers = getContactList();
//            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
//            EaseUser user = new EaseUser(username);
//
//            if (!localUsers.containsKey(username)) {
//                userDao.saveContact(user);
//            }
//            toAddUsers.put(username, user);
//            localUsers.putAll(toAddUsers);
//
//            broadcastManager.sendBroadcast(new Intent(Extras.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactDeleted(String username) {
//            Map<String, EaseUser> localUsers = LABIMHelper.getInstance().getContactList();
//            localUsers.remove(username);
//            userDao.deleteContact(username);
//            inviteMessgeDao.deleteMessage(username);
//
//            broadcastManager.sendBroadcast(new Intent(Extras.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactInvited(String username, String reason) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//                    inviteMessgeDao.deleteMessage(username);
//                }
//            }
//            // save invitation as message
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            msg.setReason(reason);
//            Log.d(TAG, username + "apply to be your friend,reason: " + reason);
//            // set invitation status
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(Extras.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onFriendRequestAccepted(String s) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(s)) {
//                    return;
//                }
//            }
//            // save invitation as message
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(s);
//            msg.setTime(System.currentTimeMillis());
//            Log.d(TAG, s + "accept your request");
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(Extras.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onFriendRequestDeclined(String s) {
//            Log.d(username, username + " refused to your request");
//        }
//
//    }
//
//    /**
//     * save and notify invitation message
//     *
//     * @param msg
//     */
//    private void notifyNewInviteMessage(InviteMessage msg) {
//        if (inviteMessgeDao == null) {
//            inviteMessgeDao = new InviteMessgeDao(appContext);
//        }
//        inviteMessgeDao.saveMessage(msg);
//        //increase the unread message count
//        inviteMessgeDao.saveUnreadMessageCount(1);
//        // notify there is new message
//        getNotifier().vibrateAndPlayTone(null);
//    }
//
////    /**
////     * user has logged into another device
////     */
////    protected void onConnectionConflict() {
////        Intent intent = new Intent(appContext, MainActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intent.putExtra(Extras.ACCOUNT_CONFLICT, true);
////        appContext.startActivity(intent);
////    }
////
////    /**
////     * account is removed
////     */
////    protected void onCurrentAccountRemoved() {
////        Intent intent = new Intent(appContext, MainActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intent.putExtra(Extras.ACCOUNT_REMOVED, true);
////        appContext.startActivity(intent);
////    }
//
//    private EaseUser getUserInfo(String username) {
//        // To get instance of EaseUser, here we get it from the user list in memory
//        // You'd better cache it if you get it from your server
//        EaseUser user = null;
//
//        if (username.equals(EMClient.getInstance().getCurrentUser()))
//            return getUserProfileManager().getCurrentUserInfo();
//        user = getContactList().get(username);
//        if (user == null && getRobotList() != null) {
//            user = getRobotList().get(username);
//        }
//
//        // if user is not in your contacts, set inital letter for him/her
//        if (user == null) {
//            user = new EaseUser(username);
//            EaseCommonUtils.setUserInitialLetter(user);
//        }
//        return user;
//    }
//
//    /**
//     * Global listener
//     * If this event already handled by an activity, you don't need handle it again
//     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
//     */
//    protected void registerMessageListener() {
//        messageListener = new EMMessageListener() {
//            private BroadcastReceiver broadCastReceiver = null;
//
//            @Override
//            public void onMessageReceived(List<EMMessage> var1) {
//                for (EMMessage message : var1) {
//                    //EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
//                    // in background, do not refresh UI, notify it in notification bar
//                    if (!easeUI.isCurrentChatUserMessage(message)) {
//                        //发送不属于当前聊天对象的通知
//                        getNotifier().onNewMsg(message);
//                    } else {
//                        //当前聊天用户则播放提示音
//                        getNotifier().vibrateAndPlayTone(message);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> var1) {
//
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> var1) {
//            }
//
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> var1) {
//
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage var1, Object var2) {
//
//            }
//        };
//
//        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//    }
//
//
//    /**
//     * if ever logged in
//     *
//     * @return
//     */
//    public boolean isLoggedIn() {
//        return EMClient.getInstance().isLoggedInBefore();
//    }
//
//    /**
//     * logout
//     *
//     * @param unbindDeviceToken whether you need unbind your device token
//     * @param callback          callback
//     */
//    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
//        endCall();
//        Log.d(TAG, "logout: " + unbindDeviceToken);
//        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "logout: onSuccess");
//                reset();
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//                if (callback != null) {
//                    callback.onProgress(progress, status);
//                }
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                Log.d(TAG, "logout: onSuccess");
//                reset();
//                if (callback != null) {
//                    callback.onError(code, error);
//                }
//            }
//        });
//    }
//
//    /**
//     * get instance of EaseNotifier
//     *
//     * @return
//     */
//    public EaseNotifier getNotifier() {
//        return easeUI.getNotifier();
//    }
//
//    public DemoModel getModel() {
//        return (DemoModel) demoModel;
//    }
//
//    /**
//     * update contact list
//     *
//     * @param
//     */
//    public void setContactList(Map<String, EaseUser> aContactList) {
//        if (aContactList == null) {
//            if (contactList != null) {
//                contactList.clear();
//            }
//            return;
//        }
//
//        contactList = aContactList;
//    }
//
//    /**
//     * save single contact
//     */
//    public void saveContact(EaseUser user) {
//        contactList.put(user.getUsername(), user);
//        demoModel.saveContact(user);
//    }
//
//    /**
//     * get contact list
//     *
//     * @return
//     */
//    public Map<String, EaseUser> getContactList() {
//        if (isLoggedIn() && contactList == null) {
//            contactList = demoModel.getContactList();
//        }
//
//        // return a empty non-null object to avoid app crash
//        if (contactList == null) {
//            return new Hashtable<String, EaseUser>();
//        }
//
//        return contactList;
//
//        NimUserInfo
//    }
//
//    /**
//     * set current username
//     *
//     * @param username
//     */
//    public void setCurrentUserName(String username) {
//        this.username = username;
//        demoModel.setCurrentUserName(username);
//    }
//
//    /**
//     * get current user's id
//     */
//    public String getCurrentUsernName() {
//        if (username == null) {
//            username = demoModel.getCurrentUsernName();
//        }
//        return username;
//    }
//
//    public void setRobotList(Map<String, RobotUser> robotList) {
//        this.robotList = robotList;
//    }
//
//    public Map<String, RobotUser> getRobotList() {
//        if (isLoggedIn() && robotList == null) {
//            robotList = demoModel.getRobotList();
//        }
//        return robotList;
//    }
//
//    /**
//     * update user list to cache and database
//     *
//     * @param
//     */
//    public void updateContactList(List<EaseUser> contactInfoList) {
//        for (EaseUser u : contactInfoList) {
//            contactList.put(u.getUsername(), u);
//        }
//        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
//        mList.addAll(contactList.values());
//        demoModel.saveContactList(mList);
//    }
//
//    public UserProfileManager getUserProfileManager() {
//        if (userProManager == null) {
//            userProManager = new UserProfileManager();
//        }
//        return userProManager;
//    }
//
//    void endCall() {
//        try {
//            //EMClient.getInstance().callManager().endCall();//TODO
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addSyncGroupListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (!syncGroupsListeners.contains(listener)) {
//            syncGroupsListeners.add(listener);
//        }
//    }
//
//    public void removeSyncGroupListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (syncGroupsListeners.contains(listener)) {
//            syncGroupsListeners.remove(listener);
//        }
//    }
//
//    public void addSyncContactListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (!syncContactsListeners.contains(listener)) {
//            syncContactsListeners.add(listener);
//        }
//    }
//
//    public void removeSyncContactListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (syncContactsListeners.contains(listener)) {
//            syncContactsListeners.remove(listener);
//        }
//    }
//
//    public void addSyncBlackListListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (!syncBlackListListeners.contains(listener)) {
//            syncBlackListListeners.add(listener);
//        }
//    }
//
//    public void removeSyncBlackListListener(DataSyncListener listener) {
//        if (listener == null) {
//            return;
//        }
//        if (syncBlackListListeners.contains(listener)) {
//            syncBlackListListeners.remove(listener);
//        }
//    }
//
//    /**
//     * Get group list from server
//     * This method will save the sync state
//     *
//     * @throws HyphenateException
//     */
//    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
//        if (isSyncingGroupsWithServer) {
//            return;
//        }
//
//        isSyncingGroupsWithServer = true;
//
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
//
//                    // in case that logout already before server returns, we should return immediately
//                    if (!isLoggedIn()) {
//                        isGroupsSyncedWithServer = false;
//                        isSyncingGroupsWithServer = false;
//                        noitifyGroupSyncListeners(false);
//                        return;
//                    }
//
//                    demoModel.setGroupsSynced(true);
//
//                    isGroupsSyncedWithServer = true;
//                    isSyncingGroupsWithServer = false;
//
//                    //notify sync group list success
//                    noitifyGroupSyncListeners(true);
//
//                    if (callback != null) {
//                        callback.onSuccess();
//                    }
//                } catch (HyphenateException e) {
//                    demoModel.setGroupsSynced(false);
//                    isGroupsSyncedWithServer = false;
//                    isSyncingGroupsWithServer = false;
//                    noitifyGroupSyncListeners(false);
//                    if (callback != null) {
//                        callback.onError(e.getErrorCode(), e.toString());
//                    }
//                }
//
//            }
//        }.start();
//    }
//
//    public void noitifyGroupSyncListeners(boolean success) {
//        for (DataSyncListener listener : syncGroupsListeners) {
//            listener.onSyncComplete(success);
//        }
//    }
//
//    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
//        if (isSyncingContactsWithServer) {
//            return;
//        }
//
//        isSyncingContactsWithServer = true;
//
//        new Thread() {
//            @Override
//            public void run() {
//                List<String> usernames = null;
//                try {
//                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//                    // in case that logout already before server returns, we should return immediately
//                    if (!isLoggedIn()) {
//                        isContactsSyncedWithServer = false;
//                        isSyncingContactsWithServer = false;
//                        notifyContactsSyncListener(false);
//                        return;
//                    }
//
//                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
//                    for (String username : usernames) {
//                        EaseUser user = new EaseUser(username);
//                        EaseCommonUtils.setUserInitialLetter(user);
//                        userlist.put(username, user);
//                    }
//                    // save the contact list to cache
//                    getContactList().clear();
//                    getContactList().putAll(userlist);
//                    // save the contact list to database
//                    UserDao dao = new UserDao(appContext);
//                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
//                    dao.saveContactList(users);
//
//                    demoModel.setContactSynced(true);
//                    EMLog.d(TAG, "set contact syn status to true");
//
//                    isContactsSyncedWithServer = true;
//                    isSyncingContactsWithServer = false;
//
//                    //notify sync success
//                    notifyContactsSyncListener(true);
//
//                    getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<EaseUser>>() {
//
//                        @Override
//                        public void onSuccess(List<EaseUser> uList) {
//                            updateContactList(uList);
//                            getUserProfileManager().notifyContactInfosSyncListener(true);
//                        }
//
//                        @Override
//                        public void onError(int error, String errorMsg) {
//                        }
//                    });
//                    if (callback != null) {
//                        callback.onSuccess(usernames);
//                    }
//                } catch (HyphenateException e) {
//                    demoModel.setContactSynced(false);
//                    isContactsSyncedWithServer = false;
//                    isSyncingContactsWithServer = false;
//                    notifyContactsSyncListener(false);
//                    e.printStackTrace();
//                    if (callback != null) {
//                        callback.onError(e.getErrorCode(), e.toString());
//                    }
//                }
//
//            }
//        }.start();
//    }
//
//    public void notifyContactsSyncListener(boolean success) {
//        for (DataSyncListener listener : syncContactsListeners) {
//            listener.onSyncComplete(success);
//        }
//    }
//
//    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {
//
//        if (isSyncingBlackListWithServer) {
//            return;
//        }
//
//        isSyncingBlackListWithServer = true;
//
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();
//
//                    // in case that logout already before server returns, we should return immediately
//                    if (!isLoggedIn()) {
//                        isBlackListSyncedWithServer = false;
//                        isSyncingBlackListWithServer = false;
//                        notifyBlackListSyncListener(false);
//                        return;
//                    }
//
//                    demoModel.setBlacklistSynced(true);
//
//                    isBlackListSyncedWithServer = true;
//                    isSyncingBlackListWithServer = false;
//
//                    notifyBlackListSyncListener(true);
//                    if (callback != null) {
//                        callback.onSuccess(usernames);
//                    }
//                } catch (HyphenateException e) {
//                    demoModel.setBlacklistSynced(false);
//
//                    isBlackListSyncedWithServer = false;
//                    isSyncingBlackListWithServer = true;
//                    e.printStackTrace();
//
//                    if (callback != null) {
//                        callback.onError(e.getErrorCode(), e.toString());
//                    }
//                }
//
//            }
//        }.start();
//    }
//
//    public void notifyBlackListSyncListener(boolean success) {
//        for (DataSyncListener listener : syncBlackListListeners) {
//            listener.onSyncComplete(success);
//        }
//    }
//
//    public boolean isSyncingGroupsWithServer() {
//        return isSyncingGroupsWithServer;
//    }
//
//    public boolean isSyncingContactsWithServer() {
//        return isSyncingContactsWithServer;
//    }
//
//    public boolean isSyncingBlackListWithServer() {
//        return isSyncingBlackListWithServer;
//    }
//
//    public boolean isGroupsSyncedWithServer() {
//        return isGroupsSyncedWithServer;
//    }
//
//    public boolean isContactsSyncedWithServer() {
//        return isContactsSyncedWithServer;
//    }
//
//    public boolean isBlackListSyncedWithServer() {
//        return isBlackListSyncedWithServer;
//    }
//
//    synchronized void reset() {
//        isSyncingGroupsWithServer = false;
//        isSyncingContactsWithServer = false;
//        isSyncingBlackListWithServer = false;
//
//        demoModel.setGroupsSynced(false);
//        demoModel.setContactSynced(false);
//        demoModel.setBlacklistSynced(false);
//
//        isGroupsSyncedWithServer = false;
//        isContactsSyncedWithServer = false;
//        isBlackListSyncedWithServer = false;
//
//        isGroupAndContactListenerRegisted = false;
//
//        setContactList(null);
//        setRobotList(null);
////        getUserProfileManager().reset();
//        DemoDBManager.getInstance().closeDB();
//    }
//
//}
