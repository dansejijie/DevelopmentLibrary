package com.netease.nim.uikit.viewholder;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.hyphenate.EMCallBack;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.module.list.MsgAdapter;
import com.netease.nim.uikit.system.IMMessage;
import com.netease.nim.uikit.system.MsgDirectionEnum;
import com.netease.nim.uikit.system.MsgStatusEnum;
import com.netease.nim.uikit.system.NimUIKit;
import com.netease.nim.uikit.system.SessionTypeEnum;
import com.netease.nim.uikit.widget.DisplayUtils;
import com.netease.nim.uikit.widget.HeadImageView;


/**
 * 会话窗口消息列表项的ViewHolder基类，负责每个消息项的外层框架，包括头像，昵称，发送/接收进度条，重发按钮等。<br>
 * 具体的消息展示项可继承该基类，然后完成具体消息内容展示即可。
 */
public abstract class MsgViewHolderBase extends TViewHolder {

    private static final String TAG=MsgViewHolderBase.class.getSimpleName();

    private static final int DEFAULT_AVATAR_SIZE = DisplayUtils.dp2px(60); //TODO

    protected IMMessage message;

    protected View alertButton;
    protected TextView timeTextView;
    protected ProgressBar progressBar;
    protected TextView nameTextView;
    protected FrameLayout contentContainer;
    protected LinearLayout nameContainer;
    protected TextView readReceiptTextView;

    private HeadImageView avatarLeft;
    private HeadImageView avatarRight;

    public ImageView nameIconView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    // contentContainerView的默认长按事件。如果子类需要不同的处理，可覆盖onItemLongClick方法
    // 但如果某些子控件会拦截触摸消息，导致contentContainer收不到长按事件，子控件也可在inflate时重新设置
    protected View.OnLongClickListener longClickListener;


    /// -- 以下接口可由子类覆盖或实现
    // 返回具体消息类型内容展示区域的layout res id
    abstract protected int getContentResId();

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    // 内容区域长按事件响应处理。该接口的优先级比adapter中有长按事件的处理监听高，当该接口返回为true时，adapter的长按事件监听不会被调用到。
    protected boolean onItemLongClick() {
        return false;
    }

    // 当是接收到的消息时，内容区域背景的drawable id
    protected int leftBackground() {
        return R.drawable.ease_chatfrom_bg;
    }

    // 当是发送出去的消息时，内容区域背景的drawable id
    protected int rightBackground() {
        return R.drawable.ease_chatto_bg;
    }

    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return false;
    }

    // 是否显示头像，默认为显示
    protected boolean isShowHeadImage() {
        return true;
    }

    // 是否显示气泡背景，默认为显示
    protected boolean isShowBubble() {
        return true;
    }

    /// -- 以下接口可由子类调用
    // 获取MsgAdapter对象
    protected final MsgAdapter getAdapter() {
        return (MsgAdapter) adapter;
    }


    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }

    // 根据layout id查找对应的控件
    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }

    // 判断消息方向，是否是接收到的消息
    protected boolean isReceivedMessage() {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    /// -- 以下是基类实现代码
    @Override
    protected final int getResId() {
        return R.layout.nim_message_item;
    }

    @Override
    protected final void inflate() {
        timeTextView = findViewById(R.id.message_item_time);
        avatarLeft = findViewById(R.id.message_item_portrait_left);
        avatarRight = findViewById(R.id.message_item_portrait_right);
        alertButton = findViewById(R.id.message_item_alert);
        progressBar = findViewById(R.id.message_item_progress);
        nameTextView = findViewById(R.id.message_item_nickname);
        contentContainer = findViewById(R.id.message_item_content);
        nameIconView = findViewById(R.id.message_item_name_icon);
        nameContainer = findViewById(R.id.message_item_name_layout);
        readReceiptTextView = findView(R.id.textViewAlreadyRead);
        View.inflate(view.getContext(), getContentResId(), contentContainer);
        inflateContentView();
    }

    /**
     * set callback for sending message
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    Log.i(TAG,"setMessageSendCallback:onSuccess");
                    refreshCurrentItem();
                }
                @Override
                public void onProgress(final int progress, String status) {
                    Log.i(TAG,"setMessageSendCallback:onProgress");
                    refreshCurrentItem();
                }

                @Override
                public void onError(int code, String error) {
                    Log.i(TAG,"setMessageSendCallback:onError");
                    refreshCurrentItem();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * set callback for receiving message
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    Log.i(TAG,"setMessageReceiveCallback:onSuccess");
                    refreshCurrentItem();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    Log.i(TAG,"setMessageReceiveCallback:onProgress");
                    refreshCurrentItem();
                }

                @Override
                public void onError(int code, String error) {
                    Log.i(TAG,"setMessageReceiveCallback:onError");
                    refreshCurrentItem();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }

    @Override
    protected final void refresh(Object item) {
        message =(IMMessage) item;
        setHeadImageView();
        setNameTextView();
        setTimeTextView();
        setStatus();
        setOnClickListener();
        setLongClickListener();
        setContent();
        setReadReceipt();
        bindContentView();
    }

    public void refreshCurrentItem() {
        if (message != null) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refresh(message);
                }
            });

        }
    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (getAdapter().needShowTime(message)) {
            timeTextView.setVisibility(View.VISIBLE);
        } else {
            timeTextView.setVisibility(View.GONE);
            return;
        }

        String text = TimeUtil.getTimeShowString(message.getTime(), false);
        timeTextView.setText(text);
    }

    /**
     * 设置消息发送状态
     */
    protected void setStatus() {

        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.VISIBLE);
                break;
            case sending:
                progressBar.setVisibility(View.VISIBLE);
                alertButton.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
                break;
        }
    }

    private void setHeadImageView() {

        // 头像默认设置
        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(NimUIKit.getContext().getResources())
                .setPlaceholderImage(R.drawable.ease_default_avatar); //TODO
        avatarLeft.setHierarchy(hierarchyBuilder.build());
        avatarRight.setHierarchy(hierarchyBuilder.build());


        HeadImageView show = isReceivedMessage() ? avatarLeft : avatarRight;
        HeadImageView hide = isReceivedMessage() ? avatarRight : avatarLeft;
        hide.setVisibility(View.GONE);
        if (!isShowHeadImage()) {
            show.setVisibility(View.GONE);
            return;
        }
        if (isMiddleItem()) {
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            show.loadBuddyAvatar(message.getFromAccount());

        }
    }

    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getAdapter().getEventListener() != null) {
            alertButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onFailedBtnClick(message);
                }
            });
            avatarLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onAvatarClick(message.getFrom());
                }
            });
            avatarRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapter().getEventListener().onAvatarClick(message.getFrom());
                }
            });
        }

        // 内容区域点击事件响应， 相当于点击了整项
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });

    }

    /**
     * item长按事件监听
     */
    private void setLongClickListener() {
        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 优先派发给自己处理，
                if (!onItemLongClick()) {
                    if (getAdapter().getEventListener() != null) {
                        getAdapter().getEventListener().onViewHolderLongClick(contentContainer, view, message);
                        return true;
                    }
                }
                return false;
            }
        };
        // 消息长按事件响应处理
        contentContainer.setOnLongClickListener(longClickListener);

        // 头像长按事件响应处理
        /*if (NimUIKit.getSessionListener() != null) {
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NimUIKit.getSessionListener().onAvatarLongClicked(context, message);
                    return true;
                }
            };
            avatarLeft.setOnLongClickListener(longClickListener);
            avatarRight.setOnLongClickListener(longClickListener);
        }*/
    }

    public void setNameTextView() {
        if (message.getSessionType() == SessionTypeEnum.Team && isReceivedMessage() && !isMiddleItem()) {
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText("团队");
        } else {
            nameTextView.setVisibility(View.GONE);
        }
    }

    private void setContent() {
        if (!isShowBubble() && !isMiddleItem()) {
            return;
        }

        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.message_item_body);

        int index = isReceivedMessage() ? 0 : 3;
        if (bodyContainer.getChildAt(index) != contentContainer) {
            bodyContainer.removeView(contentContainer);
            bodyContainer.addView(contentContainer, index);
        }

        if (isMiddleItem()) {
            setGravity(bodyContainer, Gravity.CENTER);
        } else {
            if (isReceivedMessage()) {
                setGravity(bodyContainer, Gravity.LEFT);
                contentContainer.setBackgroundResource(leftBackground());
            } else {
                setGravity(bodyContainer, Gravity.RIGHT);
                contentContainer.setBackgroundResource(rightBackground());
            }
        }
    }

    private void setReadReceipt() {
        if (!TextUtils.isEmpty(getAdapter().getUuid()) && message.getUuid().equals(getAdapter().getUuid())) {
            readReceiptTextView.setVisibility(View.VISIBLE);
        } else {
            readReceiptTextView.setVisibility(View.GONE);
        }
        readReceiptTextView.setVisibility(View.GONE);
    }
}
