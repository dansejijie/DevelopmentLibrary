package com.dansejijie.library;

/**
 * Created by tygzx on 17/3/15.
 */

public class Shout {

    onShoutListener listener;

    Shout(onShoutListener listener){
        this.listener=listener;
    }

    public interface onShoutListener{
        public void dogshout();
    }
}
