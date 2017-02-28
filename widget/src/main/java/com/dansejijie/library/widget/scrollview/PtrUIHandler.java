package com.dansejijie.library.widget.scrollview;


import android.view.ViewGroup;

/**
 * Created by tygzx on 17/1/22.
 */

public interface PtrUIHandler {

    /**
     * When the content ViewGroup has reached top and refresh has been completed, ViewGroup will be reset.
     *
     * @param frame
     */
    public void onUIReset(ViewGroup frame);

    /**
     * prepare for loading
     *
     * @param frame
     */
    public void onUIRefreshPrepare(ViewGroup frame);

    public void onUIReleaseToRefresh(ViewGroup frame);

    /**
     * perform refreshing UI
     */
    public void onUIRefreshBegin(ViewGroup frame);

    /**
     * perform UI after refresh
     */
    public void onUIRefreshComplete(ViewGroup frame);

    public void onUIPositionChange(ViewGroup frame, boolean isUnderTouch, byte status, PtrIndicator indicator);

    public void onUIRefreshFooterBegin(ViewGroup frame);
}