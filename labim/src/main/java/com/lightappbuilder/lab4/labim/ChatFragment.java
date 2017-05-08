package com.lightappbuilder.lab4.labim;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.PathUtil;
import com.netease.nim.uikit.extra.delete.L;
import com.netease.nim.uikit.extra.easeui.EaseConstant;
import com.netease.nim.uikit.extra.easeui.controller.EaseUI;
import com.netease.nim.uikit.extra.session.fragment.MessageFragment;
import com.netease.nim.uikit.extra.session.viewholder.EaseImageUtils;
import com.netease.nim.uikit.extra.uinfo.model.EaseUser;
import com.netease.nim.uikit.extra.uinfo.model.RobotUser;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by vice on 2016/7/22.
 */
public class ChatFragment extends MessageFragment  {
    private static final String TAG = "ChatFragment";

    public interface AvatarClickListener {
        void onAvatarClick(String userName);
    }

    private boolean isRobot;
    private int savedKeyboardHeight;
    String toUser;
    String me;

    private AvatarClickListener avatarClickListener;

    public static ChatFragment newInstance(AvatarClickListener avatarClickListener) {
        ChatFragment frag = new ChatFragment();
        frag.avatarClickListener = avatarClickListener;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setChatFragmentListener(this);//设置聊天点击监听

        if (sessionType == SessionTypeEnum.P2P) {
            Map<String, RobotUser> robotMap = LABIMHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(sessionId)) {
                isRobot = true;
            }
        }
    }

//    @Override
//    protected void setUpView() {
//        super.setUpView();
//        hideTitleBar();
//        setChatFragmentListener(this);//设置聊天点击监听
//
//        if (sessionType == SessionTypeEnum.P2P) {
//            Map<String, RobotUser> robotMap = LABIMHelper.getInstance().getRobotList();
//            if (robotMap != null && robotMap.containsKey(sessionId)) {
//                isRobot = true;
//            }
//        }
//
//
//        //进来根据存储设置高度
////        savedKeyboardHeight = SharedPreferencesUtils.getKeyboardHeight(getActivity());
////        ViewGroup.LayoutParams layoutParams = inputMenu.blankView.getLayoutParams();
////        layoutParams.height = savedKeyboardHeight;
////        inputMenu.blankView.setLayoutParams(layoutParams);
////        inputMenu.blankView.requestLayout();
////        Log.i(TAG, "setUpView: getkeyboardHeight " + savedKeyboardHeight);
////
////        Rect rect = new Rect();
////        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
////        final int topStatusHeight = rect.top;
////        final View root = getView();
////        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////            @Override
////            public void onGlobalLayout() {
////                int statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(getActivity());
////                Rect rect = new Rect();
////                root.getWindowVisibleDisplayFrame(rect);
//////                int displayHeight = (rect.bottom - rect.top);
////                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom - topStatusHeight;
////                int keyboardHeight = rootInvisibleHeight - statusBarHeight;//键盘高度
////                //和存储的高度不一样的时候保存高度
////                if (keyboardHeight != savedKeyboardHeight && keyboardHeight > 100) {
////                    SharedPreferencesUtils.setKeyboardHeight(getActivity(), keyboardHeight);
////                    savedKeyboardHeight = keyboardHeight;
////                    System.out.println("keyboardHeight" + keyboardHeight + "savedKeyboardHeight" + savedKeyboardHeight);
////                    ViewGroup.LayoutParams layoutParams = inputMenu.blankView.getLayoutParams();
////                    layoutParams.height = keyboardHeight;
////                    inputMenu.blankView.setLayoutParams(layoutParams);
////                }
////                if (rootInvisibleHeight <= 100) {
////                    System.out.println("键盘消失");
//////                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)inputMenu. getLayoutParams();
//////                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//////                    params.setMargins(0,350,0,0);
//////                    System.out.println(params.topMargin+"--"+params.bottomMargin);
//////                    inputMenu.setLayoutParams(params);
////                } else {
////                    System.out.println("键盘显示");
//////                    ViewGroup.LayoutParams layoutParams = inputMenu.chatExtendMenuContainer.getLayoutParams();
//////                    layoutParams.height=rootInvisibleHeight-statusBarHeight;
//////                    inputMenu.chatExtendMenuContainer.setLayoutParams(layoutParams);
//////                    inputMenu.chatExtendMenuContainer.setVisibility(View.VISIBLE);
////                }
////            }
////        });
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取历史消息
//        if (!TextUtils.isEmpty(IMConfig.chatRecordUrl)) {
//            final Request request = new Request.Builder().url(IMConfig.chatRecordUrl + "&chat_uid=" + toUser).tag(this).build();
//            OkHttpClientProvider.getOkHttpClient().newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.e(TAG, "onFailure: 获取聊天记录失败", e);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String result = response.body().string();
//                    try {
//                        onLoadRecords(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //OkHttpCallUtil.cancelTag(OkHttpClientProvider.getOkHttpClient(), this);
    }

    /**
     * 设置用户信息
     */
    private void setupUserInfo() {
        Bundle bundle = getArguments();

        toUser = bundle.getString(EaseConstant.EXTRA_USER_ID);
        me = EMClient.getInstance().getCurrentUser();

        LABIMHelper.getInstance().getUserProfileManager().setCurrentUserNick(bundle.getString("myNickName"));
        LABIMHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(bundle.getString("myAvatar"));
        LABIMHelper.getInstance().setCurrentUserName(me);

        EaseUser eaToUser = new EaseUser(toUser);
        eaToUser.setAvatar(bundle.getString("toUserAvatar"));
        eaToUser.setNick(bundle.getString("toUserNickName"));
        DemoModel model = new DemoModel(getActivity());
        model.saveContact(eaToUser);
        LABIMHelper.getInstance().getContactList();
        LABIMHelper.getInstance().getContactList().put(toUser, eaToUser);
    }

    private void onLoadRecords(String json) throws Exception {
        L.i(TAG, "onLoadRecords json:", json);

        JSONObject jsonObject = new JSONObject(json);

        if (!"ok".equals(jsonObject.getString("CODE"))) {
            return;
        }

        JSONObject data = jsonObject.getJSONObject("DATA");
        //int page_size = data.optInt("page_size");
        JSONArray list = data.getJSONArray("list");

        List<EMMessage> messages = new ArrayList<>();

        for (int i = 0; i < list.length(); ++i) {
            JSONObject messageJson = list.getJSONObject(i);
            EMMessage emMessage = createMessage(messageJson);
            if (emMessage == null) {
                continue;
            }
            messages.add(emMessage);
        }

        if (!messages.isEmpty()) {
            importMessages(messages);
        }
    }

    private EMMessage createMessage(JSONObject messageJson) throws JSONException {
        long timestamp = messageJson.getLong("timestamp");
        String msg_id = messageJson.getString("msg_id");
        String from = messageJson.getString("from");
        String to = messageJson.getString("to");

        boolean isSend;
        if (from.equals(toUser)) {
            isSend = false;
        } else if (from.equals(me)) {
            isSend = true;
        } else {
            return null;
        }

        JSONObject payload = messageJson.getJSONObject("payload");
        JSONObject body = payload.getJSONArray("bodies").getJSONObject(0);

        String type = body.getString("type");
        EMMessage emMessage = null;
        switch (body.getString("type")) {
            case "txt": {
                if (isSend) {
                    emMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
                } else {
                    emMessage = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                }

                String msg = body.getString("msg");

                EMTextMessageBody emMessageBody = new EMTextMessageBody(msg);
                emMessage.addBody(emMessageBody);
                break;
            }
            case "img": {
                if (isSend) {
                    emMessage = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
                } else {
                    emMessage = EMMessage.createReceiveMessage(EMMessage.Type.IMAGE);
                }

                String url = body.getString("url");
                String secret = body.getString("secret");
                //long file_length = body.getLong("file_length");
                String filename = body.getString("filename");
                String localUrl = EaseImageUtils.getImagePath(url);

                EMImageMessageBody emMessageBody = new EMImageMessageBody(new File(localUrl));
                emMessageBody.setRemoteUrl(url);
                emMessageBody.setThumbnailSecret(secret);
                emMessageBody.setThumbnailUrl(url);
                emMessageBody.setSecret(secret);
                emMessageBody.setFileName(filename);
                emMessageBody.setLocalUrl(localUrl);
                emMessage.addBody(emMessageBody);
                break;
            }
            case "audio": {
                if (isSend) {
                    emMessage = EMMessage.createSendMessage(EMMessage.Type.VOICE);
                } else {
                    emMessage = EMMessage.createReceiveMessage(EMMessage.Type.VOICE);
                }

                String url = body.getString("url");
                int length = body.getInt("length");
                String secret = body.getString("secret");
                //long file_length = body.getLong("file_length");
                String filename = body.getString("filename");
                //如果这条消息本地已存在,是不会更新本地消息的,所以不用担心localUrl 与本地不同导致重复下载
                String localUrl = PathUtil.getInstance().getVoicePath() + "/" + url.substring(url.lastIndexOf("/") + 1, url.length());

                EMVoiceMessageBody emMessageBody = new EMVoiceMessageBody(new File(localUrl), length);
                emMessageBody.setLocalUrl(localUrl);
                emMessageBody.setFileName(filename);
                emMessageBody.setSecret(secret);
                emMessageBody.setRemoteUrl(url);
                emMessage.addBody(emMessageBody);

                // 对音频数据如果根据本地是否存在设置状态
                if (new File(localUrl).exists()) {
                    emMessage.setStatus(EMMessage.Status.SUCCESS);
                } else {
                    emMessage.setStatus(EMMessage.Status.CREATE);
                    emMessage.setAttribute("lab_sync_audio", true); //标记是未加载的 同步下来的音频消息
                }
                break;
            }
        }
        if (emMessage != null) {
            emMessage.setTo(to);
            emMessage.setFrom(from);
            emMessage.setMsgTime(timestamp);
            emMessage.setMsgId(msg_id);
            emMessage.setChatType(EMMessage.ChatType.Chat);
            if (!"audio".equals(body.getString("type"))) {
                emMessage.setStatus(EMMessage.Status.SUCCESS);
            }
            emMessage.setDirection(isSend ? EMMessage.Direct.SEND : EMMessage.Direct.RECEIVE);
            emMessage.setAcked(true);
        }
        return emMessage;
    }

    private void importMessages(List<EMMessage> messages) {
        EMClient.getInstance().chatManager().importMessages(messages);


//        int allMsgCount = conversation.getAllMsgCount();
//        if (allMsgCount == 0) {
//            //本地没有消息的时候加载10条
//            conversation.loadMoreMsgFromDB("", 10);
//            Activity activity = getActivity();
//            if (activity != null) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (messageList != null) {
//                            messageList.refresh();
//                        }
//                    }
//                });
//            }
//            return;
//        }
//
//        List<EMMessage> currentAllMessages = conversation.getAllMessages();
//        EMMessage firstMessage = currentAllMessages.isEmpty() ? null : currentAllMessages.get(0);
//        int loadLength = -1;
//        if (firstMessage != null) {
//            String firstMessageMsgId = firstMessage.getMsgId();
//            for (int i = 0; i < messages.size(); i++) {
//                if (firstMessageMsgId.equals(messages.get(i).getMsgId())) {
//                    loadLength = i + 1;
//                    break;
//                }
//            }
//        }
//        if (loadLength == -1) {
//            //没有匹配的msgId ,加载全部
//            conversation.loadMoreMsgFromDB("", messages.size());
//        } else {
//            conversation.loadMoreMsgFromDB("", loadLength);
//        }
//
//        Activity activity = getActivity();
//        if (activity != null) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (messageList == null) {
//                        return;
//                    }
//                    try {
//                        int firstVisiblePosition = messageList.getListView().getFirstVisiblePosition();
//                        EMMessage emMessage = (EMMessage) messageList.getListView().getAdapter().getItem(firstVisiblePosition);
//                        List<EMMessage> allMessages = conversation.getAllMessages();
//                        int newPosition = firstVisiblePosition;
//                        for (int i = 0; i < allMessages.size(); ++i) {
//                            if (emMessage.getMsgId().equals(allMessages.get(i).getMsgId())) {
//                                newPosition = i;
//                                break;
//                            }
//                        }
//                        if (newPosition == firstVisiblePosition) {
//                            messageList.refresh();
//                        } else {
//                            messageList.refreshSeekTo(newPosition);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // XXX 简单处理 打开聊天界面就清空通知栏
        EaseUI.getInstance().getNotifier().reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void hideKBoard() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        EaseChatPrimaryMenu chatPrimaryMenu = (EaseChatPrimaryMenu) inputMenu.chatPrimaryMenu;
//        imm.hideSoftInputFromWindow(chatPrimaryMenu.editText.getWindowToken(), 0); //强制隐藏键盘
    }

}

