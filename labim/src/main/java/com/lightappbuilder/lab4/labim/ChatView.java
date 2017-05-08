package com.lightappbuilder.lab4.labim;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.lightappbuilder.lab4.labim.delete.LABRNTopView;
import com.netease.nim.uikit.extra.easeui.EaseConstant;

/**
 * Created by vice on 2016/7/22.
 */
public class ChatView extends LABRNTopView {
    private static final String TAG = "LABIMChatView";

    private String toUserId = null;
    private String toUserAvatar = null;
    private String toUserNickName = null;
    private String myAvatar = null;
    private String myNickName = null;
    private ChatFragment chatFragment;

    public ChatView(Context context) {
        super(context);
        init();
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFitsSystemWindows(true);

//        View v = LayoutInflater.from(getContext()).inflate(R.layout.test_rn_custom_layout, this, false);
//        v.setFitsSystemWindows(true);
//        Button btnUp = (Button) v.findViewById(R.id.btn_up);
//        EditText etContent = (EditText) v.findViewById(R.id.et_content);
//        final FrameLayout bottomBar = (FrameLayout) v.findViewById(R.id.bottom_bar);
//
//        etContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomBar.setVisibility(View.GONE);
//            }
//        });
//
//
//        btnUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomBar.setVisibility(bottomBar.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
//            }
//        });
//
//        this.addView(v, -1, -1);
    }

    /**
     * 设置信息
     */
    public void setOptions(ReadableMap options) {
        //TODO 字段可为空的判断 对options改变处理
        this.toUserId = options.getString("toImId");
        try {
            this.toUserAvatar = options.getString("toAvatar");
            this.toUserNickName = options.getString("toNickname");
            this.myAvatar = options.getString("myAvatar");
            this.myNickName = options.getString("myNickname");
        } catch (Exception e) {
            e.printStackTrace();
        }
        openChatFragment();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        openChatFragment();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (chatFragment != null) {
            chatFragment.hideKBoard();
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.i(TAG, "onMeasure() called with: " + "widthMeasureSpec = [" + MeasureSpec.toString(widthMeasureSpec) + "], heightMeasureSpec = [" + MeasureSpec.toString(heightMeasureSpec) + "]");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.i(TAG, "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
//        super.onLayout(changed, left, top, right, bottom);
//    }
//
//    @Override
//    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
//        Log.i(TAG, "onApplyWindowInsets() called with: " + "insets = [" + insets + "]");
//        WindowInsets ret = super.onApplyWindowInsets(insets);
//        Log.i(TAG, "onApplyWindowInsets() called with: " + "ret = [" + ret + "]");
//        // XXX 子View 有fitsSystemWindows时的bug
//        //applyWindowInsets = true;
//        return ret;
//    }
//
//    @Override
//    public void forceLayout() {
//        Log.i(TAG, "forceLayout: xxxxxx");
//        super.forceLayout();
//    }
//
//    @Override
//    public void requestLayout() {
//        Log.i(TAG, "requestLayout: xxxxxxxxx");
//        super.requestLayout();
//    }

    private void openChatFragment() {
        if (chatFragment == null && ViewCompat.isAttachedToWindow(this) && !TextUtils.isEmpty(toUserId)) {
            //new出EaseChatFragment或其子类的实例
            chatFragment = ChatFragment.newInstance(new ChatFragment.AvatarClickListener() {
                @Override
                public void onAvatarClick(String userName) {
                    WritableMap event = Arguments.createMap();
                    event.putString("userName", userName);
                    ReactContext reactContext = (ReactContext) getContext();
                    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                            getId(), "onAvatarClick", event);
                }
            });
            //传入参数
            Bundle args = new Bundle();
            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            args.putString(EaseConstant.EXTRA_USER_ID, toUserId);
            args.putString("toUserAvatar", toUserAvatar);
            args.putString("toUserNickName", toUserNickName);
            args.putString("myAvatar", myAvatar);
            args.putString("myNickName", myNickName);

            chatFragment.setArguments(args);

            ReactContext reactContext = (ReactContext) getContext();
            Activity activity = (Activity) reactContext.getBaseContext();
            activity.getFragmentManager().beginTransaction().add(getId(), chatFragment).commit();
        }
    }

    public void destroy() {
        if (chatFragment != null) {
            ReactContext reactContext = (ReactContext) getContext();
            Activity activity = (Activity) reactContext.getBaseContext();
            try {
                activity.getFragmentManager().beginTransaction().remove(chatFragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            chatFragment = null;
        }
    }

}
