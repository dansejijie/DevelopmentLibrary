package com.netease.nim.uikit.common.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.log.LogUtil;


/**
 * Created by dansejijie on 17/4/4.
 */

public abstract class TView extends FrameLayout {


    public TView(Context context) {
        super(context);
    }

    public TView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final Handler handler = new Handler();

    private int containerId;

    private boolean destroyed;

    private boolean added=false;

    protected final boolean isDestroyed() {
        return destroyed;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

//    public void onActivityCreated(Bundle savedInstanceState) {
//
//        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onActivityCreated()");
//
//        destroyed = false;
//    }
//
//    public void onDestroy() {
//
//        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onDestroy()");
//
//        destroyed = true;
//    }

    protected final Handler getHandlerCompat() {
        return handler;
    }

    protected final void postRunnable(final Runnable runnable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        });
    }

    protected final void postDelayedCompat(final Runnable runnable, long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        }, delay);
    }

    protected void showKeyboard(boolean isShow) {
        Activity activity = (Activity)getContext();
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }

    protected void hideKeyboard(View view) {
        Activity activity = (Activity)getContext();
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        imm.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

//    protected void setToolBar(int toolbarId, int titleId, int logoId) {
//        if (getActivity() != null && getActivity() instanceof UI) {
//            ((UI)getActivity()).setToolBar(toolbarId, titleId, logoId);
//        }
//    }
//    protected void setTitle(int titleId) {
//        if (getActivity() != null && getActivity() instanceof UI) {
//            getActivity().setTitle(titleId);
//        }
//    }


    public Activity getActivity(){
        return (Activity)getContext();
    }


    public boolean isAdded(){
        return added;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        added=true;
        destroyed=false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        added=false;
        destroyed=true;
    }
}
