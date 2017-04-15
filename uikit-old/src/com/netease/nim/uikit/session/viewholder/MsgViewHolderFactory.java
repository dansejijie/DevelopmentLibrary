//package com.netease.nim.uikit.session.viewholder;
//
//import com.hyphenate.chat.EMImageMessageBody;
//import com.hyphenate.chat.EMLocationMessageBody;
//import com.hyphenate.chat.EMMessageBody;
//import com.hyphenate.chat.EMVideoMessageBody;
//import com.hyphenate.chat.EMVoiceMessageBody;
//import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
//import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
//import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
//import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
//import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
//import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
//import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
//import com.netease.nimlib.sdk.msg.model.IMMessage;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * 消息项展示ViewHolder工厂类。
// */
//public class MsgViewHolderFactory {
//
//
//    private static HashMap<Class<? extends EMMessageBody>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();
//
//    private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;
//
//    static {
//        // built in
//        register(EMImageMessageBody.class, MsgViewHolderPicture.class);
////        Log.i("个人测试:","MsgViewHolderFactory:缺少注册Audio和Video.class");
//        register(EMVoiceMessageBody.class, MsgViewHolderAudio.class);
//        //register(EMVideoMessageBody.class, MsgViewHolderVideo.class);
//        //register(EMLocationMessageBody.class, MsgViewHolderLocation.class);
//        //register(NotificationAttachment.class, MsgViewHolderNotification.class);
//    }
//
//    public static void register(Class<? extends EMMessageBody> attach, Class<? extends MsgViewHolderBase> viewHolder) {
//        viewHolders.put(attach, viewHolder);
//    }
//
//    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
//        tipMsgViewHolder = viewHolder;
//    }
//
//    public static Class<? extends MsgViewHolderBase> getViewHolderByType(IMMessage message) {
//
//        if (message.getMsgType() == MsgTypeEnum.text) {
//            return MsgViewHolderText.class;
//        } /*else if (message.getMsgType() == MsgTypeEnum.tip) {
//            return tipMsgViewHolder == null ? MsgViewHolderUnknown.class : tipMsgViewHolder;
//        } */else {
//            Class<? extends MsgViewHolderBase> viewHolder = null;
//            if (message.getAttachment() != null) {
//                Class<? extends EMMessageBody> clazz = message.getAttachment().getEMMessageBody().getClass();
//                while (viewHolder == null && clazz != null) {
//                    viewHolder = viewHolders.get(clazz);
//                    if (viewHolder == null) {
//                        clazz = getSuperClass(clazz);
//                    }
//                }
//            }
//            return viewHolder == null ? MsgViewHolderText.class : viewHolder;
//        }
//    }
//
//    public static int getViewTypeCount() {
//        // plus text and unknown
//        return viewHolders.size() + 2;
//    }
//
//    public static Class<? extends EMMessageBody> getSuperClass(Class<? extends EMMessageBody> derived) {
//        Class sup = derived.getSuperclass();
//        if (sup != null && EMMessageBody.class.isAssignableFrom(sup)) {
//            return sup;
//        } else {
//            for (Class itf : derived.getInterfaces()) {
//                if (EMMessageBody.class.isAssignableFrom(itf)) {
//                    return itf;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static List<Class<? extends MsgViewHolderBase>> getAllViewHolders() {
//        List<Class<? extends MsgViewHolderBase>> list = new ArrayList<>();
//        list.addAll(viewHolders.values());
//        if (tipMsgViewHolder != null) {
//            list.add(tipMsgViewHolder);
//        }
//        list.add(MsgViewHolderUnknown.class);
//        list.add(MsgViewHolderText.class);
//
//        return list;
//    }
//}
package com.netease.nim.uikit.session.viewholder;

import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息项展示ViewHolder工厂类。
 */
public class MsgViewHolderFactory {

    private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;

    static {
        // built in
        register(ImageAttachment.class, MsgViewHolderPicture.class);
        register(AudioAttachment.class, MsgViewHolderAudio.class);
        register(VideoAttachment.class, MsgViewHolderVideo.class);
        register(LocationAttachment.class, MsgViewHolderLocation.class);
        register(NotificationAttachment.class, MsgViewHolderNotification.class);
    }

    public static void register(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        viewHolders.put(attach, viewHolder);
    }

    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        tipMsgViewHolder = viewHolder;
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(IMMessage message) {
        if (message.getMsgType() == MsgTypeEnum.text) {
            return MsgViewHolderText.class;
        } else if (message.getMsgType() == MsgTypeEnum.tip) {
            return tipMsgViewHolder == null ? MsgViewHolderUnknown.class : tipMsgViewHolder;
        } else {
            Class<? extends MsgViewHolderBase> viewHolder = null;
            if (message.getAttachment() != null) {
                Class<? extends MsgAttachment> clazz = message.getAttachment().getClass();
                while (viewHolder == null && clazz != null) {
                    viewHolder = viewHolders.get(clazz);
                    if (viewHolder == null) {
                        clazz = getSuperClass(clazz);
                    }
                }
            }
            return viewHolder == null ? MsgViewHolderUnknown.class : viewHolder;
        }
    }

    private static Class<? extends MsgAttachment> getSuperClass(Class<? extends MsgAttachment> derived) {
        Class sup = derived.getSuperclass();
        if (sup != null && MsgAttachment.class.isAssignableFrom(sup)) {
            return sup;
        } else {
            for (Class itf : derived.getInterfaces()) {
                if (MsgAttachment.class.isAssignableFrom(itf)) {
                    return itf;
                }
            }
        }
        return null;
    }

    public static List<Class<? extends MsgViewHolderBase>> getAllViewHolders() {
        List<Class<? extends MsgViewHolderBase>> list = new ArrayList<>();
        list.addAll(viewHolders.values());
        if (tipMsgViewHolder != null) {
            list.add(tipMsgViewHolder);
        }
        list.add(MsgViewHolderUnknown.class);
        list.add(MsgViewHolderText.class);

        return list;
    }
}
