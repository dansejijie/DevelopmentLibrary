package com.dansejijie.library;

import android.view.View;

/**
 * Created by tygzx on 17/1/11.
 */

public class Action {

    public String title;

    public String description;

    View.OnClickListener onClickListener;

    public Action(String title, View.OnClickListener listener){
        this.title=title;
        this.onClickListener=listener;
    }

    public Action(String title, View.OnClickListener listener,String description){
        this.title=title;
        this.onClickListener=listener;
        this.description=description;
    }

    public void setDescription(String description){
        this.description=description;
    }
}
