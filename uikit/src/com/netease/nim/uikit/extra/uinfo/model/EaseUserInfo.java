package com.netease.nim.uikit.extra.uinfo.model;

import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.Map;

/**
 * Created by dansejijie on 17/4/16.
 */

public class EaseUserInfo implements NimUserInfo {


    private String account;

    private String avatar;

    public EaseUserInfo(String account,String avatar){
        this.account=account;
        this.avatar=avatar;
    }

    @Override
    public String getSignature() {
        return null;
    }

    @Override
    public GenderEnum getGenderEnum() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensionMap() {
        return null;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
