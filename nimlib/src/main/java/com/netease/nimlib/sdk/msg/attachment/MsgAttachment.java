package com.netease.nimlib.sdk.msg.attachment;
import com.hyphenate.chat.EMMessageBody;

import java.io.Serializable;

/**
 * Created by dansejijie on 17/4/4.
 */

public class MsgAttachment{
    EMMessageBody emMessageBody;

    public MsgAttachment(EMMessageBody emMessageBody){
        this.emMessageBody=emMessageBody;
    }

    public void setEMMessageBody(EMMessageBody emMessageBody){
        this.emMessageBody=emMessageBody;
    }

    public EMMessageBody getEMMessageBody(){
        return emMessageBody;
    }
}

