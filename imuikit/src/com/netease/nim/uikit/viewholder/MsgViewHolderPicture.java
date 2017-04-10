package com.netease.nim.uikit.viewholder;


import com.netease.nim.uikit.R;
import com.netease.nim.uikit.activity.WatchMessagePictureActivity;


/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderPicture extends MsgViewHolderImageThumbBase {

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_picture;
    }

    @Override
    protected void onItemClick() {
        WatchMessagePictureActivity.start(context, message);
    }

}
