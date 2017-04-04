package com.netease.nimlib.sdk.msg.model;

import android.util.Log;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dansejijie on 17/4/4.
 */

public class IMMessage implements Serializable {

    public EMMessage emMessage;

    public IMMessage(){

    }

    public IMMessage(EMMessage emMessage){
        this.emMessage=emMessage;
    }

    public EMMessage getEMMessage(){
        return emMessage;
    }

    public void setEMMessage(EMMessage emMessage){
        this.emMessage=emMessage;
    }

    public String getUuid(){
        return emMessage.getMsgId();
    };


    public boolean isTheSame(IMMessage var1){
        return var1.emMessage.getMsgId()==emMessage.getMsgId();
    };

    public String getSessionId(){
        return emMessage.getMsgId();
    }

    public SessionTypeEnum getSessionType(){
        return SessionTypeEnum.EMMessageSessionTypeEnumConvertToIMMessageSessionTypeEnum(emMessage.getChatType());
    }

    public String getFromNick(){
        return emMessage.getFrom();
    }

    public MsgTypeEnum getMsgType(){

       return MsgTypeEnum.EMMessageMsgTypeEnumConvertToIMMessageMsgTypeEnum(emMessage.getType());
    }

    public MsgStatusEnum getStatus(){
        return MsgStatusEnum.EMMessageMsgStatusEnumConvertToIMMessageMsgStatusEnum(emMessage);
    }

    public void setStatus(MsgStatusEnum var1){

        EMMessage.Status status=MsgStatusEnum.IMMessageMsgStatusEnumConvertToEMMessageMsgStatusEnum(var1);
        if (status==EMMessage.Status.CREATE){
            Log.e("TAG","这里需要修改下");//TODO
        }else {
            emMessage.setStatus(status);
        }

    }

    public void setDirect(MsgDirectionEnum var1){

        emMessage.setDirection(MsgDirectionEnum.IMMessageMsgStatusEnumConvertToEMMessageMsgStatusEnum(var1));
    }

    public MsgDirectionEnum getDirect(){
        return MsgDirectionEnum.EMMessageMsgStatusEnumConvertToIMMessageMsgStatusEnum(emMessage.direct());
    }

    public void setContent(String var1){
        Log.e("TAG","unhandle");
    }

    public String getContent(){
        Log.e("TAG","unhandle");
        return "";
    }

    public long getTime(){
        return emMessage.getMsgTime();
    }

    public void setFromAccount(String var1){
        emMessage.setFrom(var1);
    }

    public String getFromAccount(){
        return emMessage.getFrom();
    }

    public void setAttachment(MsgAttachment var1){
        emMessage.addBody(var1.getEMMessageBody());
    }

    public MsgAttachment getAttachment(){
        return new MsgAttachment(emMessage.getBody());
    }

    public AttachStatusEnum getAttachStatus(){
        return AttachStatusEnum.EMMessageMsgStatusEnumConvertToIMMessageMsgStatusEnum(((EMFileMessageBody)(emMessage.getBody())).downloadStatus());
    }

    public void setAttachStatus(AttachStatusEnum var1){
        Log.e("TAG","not handler");//// TODO: 17/4/4
    }

    public CustomMessageConfig getConfig(){
        Log.e("TGA","unhandler");
        return new CustomMessageConfig();
    }

    public void setConfig(CustomMessageConfig var1){
        Log.e("TGA","unhandler");
    }
//
//    public Map<String, Object> getRemoteExtension(){
//
//    }
//
//    public void setRemoteExtension(Map<String, Object> var1){
//
//    }
//
//    public Map<String, Object> getLocalExtension(){
//
//    }
//
//    public void setLocalExtension(Map<String, Object> var1){
//
//    }
//
    public String getPushContent(){
        Log.e("TAG","unhandler");
        return " ";

    }

    public void setPushContent(String var1){
        Log.e("TAG","unhandler");

    }

    public Map<String, Object> getPushPayload(){
        Log.e("TAG","unhandler");
        return new HashMap<String,Object>();
    }

    public void setPushPayload(Map<String, Object> var1){
        Log.e("TAG","unhandler");

    }
//
//    public MemberPushOption getMemberPushOption(){
//
//    }
//
//    public void setMemberPushOption(MemberPushOption var1){
//
//    }
//
//    public boolean isRemoteRead(){
//
//    }
//
//    public int getFromClientType(){
//
//    }
//
//    public NIMAntiSpamOption getNIMAntiSpamOption(){
//
//    }
//
//    public void setNIMAntiSpamOption(NIMAntiSpamOption var1){
//
//    }
}
