package com.netease.nim.uikit.viewholder;

import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.PathUtil;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.util.media.BitmapDecoder;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
//import com.netease.nim.uikit.common.http.EMImageLoadHelper;
import com.netease.nim.uikit.system.MsgDirectionEnum;
import com.netease.nim.uikit.system.MsgStatusEnum;
import com.netease.nim.uikit.system.MsgTypeEnum;
import com.netease.nim.uikit.system.NimUIKit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public abstract class MsgViewHolderImageThumbBase extends MsgViewHolderBase {

    protected SimpleDraweeView thumbnail;
    protected View progressCover;
    EMImageMessageBody msgAttachment;

    private static DisplayMetrics metrics= NimUIKit.getContext().getResources().getDisplayMetrics();


    @Override
    protected void inflateContentView() {
        thumbnail = findViewById(R.id.message_item_thumb_thumbnail);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar);
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
    }

    @Override
    protected void bindContentView() {

        if (message.getDirect()== MsgDirectionEnum.In){
            setMessageReceiveCallback();
        }else {
            setMessageSendCallback();
        }
    }

    @Override
    protected void setStatus() {

        EMImageMessageBody attachment = (EMImageMessageBody) message.getAttachment();
        MsgStatusEnum status = message.getStatus();
        // alert button
        if (TextUtils.isEmpty(attachment.getLocalUrl())) {
            if (attachment.downloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED || status == MsgStatusEnum.fail) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        if (message.getStatus() == MsgStatusEnum.sending
                || (isReceivedMessage() && attachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING)) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressCover.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        /*if (status == MsgStatusEnum.sending || attachment.downloadStatus()== EMFileMessageBody.EMDownloadStatus.DOWNLOADING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }*/

        GenericDraweeHierarchyBuilder hierarchyBuilder=GenericDraweeHierarchyBuilder.newInstance(NimUIKit.getContext().getResources());
        hierarchyBuilder.setPlaceholderImage(R.drawable.nim_default_img).setFailureImage(R.drawable.nim_default_img_failed).build();
        msgAttachment = (EMImageMessageBody) message.getAttachment();
        thumbnail.setHierarchy(hierarchyBuilder.build());
        thumbnail.setController(null);
        if (message.getDirect() == MsgDirectionEnum.In) {
            if (msgAttachment.thumbnailDownloadStatus()==EMFileMessageBody.EMDownloadStatus.SUCCESSED){
                thumbnail.setController(null);
                String thumbPath = msgAttachment.thumbnailLocalPath();
                loadImage(thumbPath);
            }else if (msgAttachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
                String thumbPath = msgAttachment.getRemoteUrl();
                loadImage(thumbPath);
            }
            /*if (msgAttachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    msgAttachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                thumbnail.setController(null);
                refreshCurrentItem();
                setMessageReceiveCallback();
            } else {


            }*/
            return;
        }else {
            String thumbImageName = msgAttachment.getLocalUrl().substring(msgAttachment.getLocalUrl().lastIndexOf("/") + 1, msgAttachment.getLocalUrl().length());
            String thumbPath = PathUtil.getInstance().getImagePath() + "/" + "th" + thumbImageName;
            loadImage(thumbPath);
        }

    }

    private void loadImage(String thumbPath) {

        Uri uri;

        if (new File(thumbPath).exists()) {
            setImageSize(thumbPath);
            uri=Uri.fromFile(new File(thumbPath));
        } else if (new File(msgAttachment.getLocalUrl()).exists()) {
            setImageSize(msgAttachment.getLocalUrl());
            uri=Uri.fromFile(new File(msgAttachment.getLocalUrl()));
        } else {
//            Map<String,String>authHeaders=new HashMap<>();
//            authHeaders.put("share-secret", ((EMImageMessageBody)message.getAttachment()).getSecret());
//            authHeaders.put("Authorization", "Bearer " + EMClient.getInstance().getAccessToken());
//            authHeaders.put("thumbnail", "true");
//            authHeaders.put("Accept","application/octet-stream");
//            DraweeController controller= EMImageLoadHelper.newDraweeControllerBuilder()
//                    .setCallerContext(authHeaders)
//                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(((EMImageMessageBody)message.getAttachment()).getRemoteUrl()))
//                        .setResizeOptions(new ResizeOptions((int)(metrics.widthPixels*1.5f),(int)(metrics.heightPixels*1.5f)))
//                        .build())
//                    .build();
//            thumbnail.setController(controller);
            return;
        }

        thumbnail.setImageURI(uri);
        /*DraweeController oneControl= Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        refreshCurrentItem();
                    }
                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                    }
                })
                .setUri(uri).build();
        thumbnail.setController(oneControl);*/

    }

    private void refreshStatus() {
        EMImageMessageBody attachment = (EMImageMessageBody) message.getAttachment();
        if (attachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
            alertButton.setVisibility(View.VISIBLE);
        } else {
            alertButton.setVisibility(View.GONE);
        }
        if (message.getStatus() == MsgStatusEnum.sending
                || (isReceivedMessage() && attachment.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING)) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressCover.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        progressCover.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }


    private void setImageSize(String thumbPath) {
        int[] bounds = null;
        if (thumbPath != null) {
            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
        }
        if (bounds == null) {
            if (message.getMsgType() == MsgTypeEnum.image) {
                EMImageMessageBody attachment = (EMImageMessageBody) message.getAttachment();
                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
            }
        }

        if (bounds != null) {
            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
        }
    }

    private int maskBg() {
        return R.drawable.nim_message_item_round_bg;
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
    }

}
