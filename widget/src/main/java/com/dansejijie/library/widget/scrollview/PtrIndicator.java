package com.dansejijie.library.widget.scrollview;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by tygzx on 17/1/23.
 */

public class PtrIndicator {

    public final static int POS_START = 0;
    protected int mOffsetToRefresh = 0;
    private PointF mPtLastMove = new PointF();
    private float mOffsetX;
    private float mOffsetY;
    private int mCurrentPos = 0;
    private int mLastPos = 0;
    private int mHeaderHeight;
    private int mPressedPos = 0;

    private float mResistance = 1.7f;
    private boolean mIsUnderTouch = false;
    private int mOffsetToKeepHeaderWhileLoading = -1;
    // record the refresh complete position
    private int mRefreshCompleteY = 0;

    private static final String TAG=PtrIndicator.class.getSimpleName();

    //额外添加，用来判断滑动状态是否是自由滑动
    private boolean isFling=false;
    //第一次点击的位置
    private PointF mFirstPressDown=new PointF();

    //上一次处理过的移动的距离，遵循根号的递增
    private PointF mProcessLastDelta=new PointF();

    //当前处理过的移动的距离，遵循根号的递增
    private PointF mProcessCurrentDelta=new PointF();
    //控件大小
    private int viewWidth=0;

    private int viewHeight=0;

    private int heightFactor=0;//控件高度-头部高度

    private int lastScrollY=0;

    private int currentScrollY=0;

    private int rangeScrollY;

    private float offsetToRefreshFactor=0.65f;


    public void setScrollY(int scrollY){
        lastScrollY=currentScrollY;
        currentScrollY=scrollY;
    }

    public void setRangeScrollY(int range){
        rangeScrollY=range;
    }
    public boolean isFling(){
        return isFling;
    }
    //只在三个地方调用，一个是在onTouchEvent里的up事件的fling里设置fling，一个是onTouchEvent的down里的mScroll.abortAnimation();一个是在coumpteScrollOffset里的已完成动画
    public void setFling(boolean b){
        isFling=b;
    }

    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

    public float getResistance() {
        return mResistance;
    }

    public void setResistance(float resistance) {
        mResistance = resistance;
    }

    public void onUIRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    public boolean goDownCrossFinishPosition() {
        return mCurrentPos >= mRefreshCompleteY;
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY / mResistance);
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        offsetToRefreshFactor = ratio;
        mOffsetToRefresh = (int) (mHeaderHeight * ratio);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return offsetToRefreshFactor;
    }

    public int getOffsetToRefresh() {
        return mOffsetToRefresh;
    }

    public void setOffsetToRefresh(int offset) {
        offsetToRefreshFactor = mHeaderHeight * 1f / offset;
        mOffsetToRefresh = offset;
    }

    public void onPressDown(float x, float y) {
        mIsUnderTouch = true;
        mPressedPos = mCurrentPos;
        mPtLastMove.set(x, y);

    }

    /**
     *
     * @param x
     * @param y
     * @param overSide 判断是否越过上下边界
     */
    public final void onMove(float x, float y,boolean overSide) {
        float offsetX = x - mPtLastMove.x;
        float offsetY = (y - mPtLastMove.y);
        processOnMove(x, y, offsetX, offsetY);
        mPtLastMove.set(x, y);

        processOnDelta(x,y,overSide);
    }

    public void onRelease() {
        mIsUnderTouch = false;

        firstReachSide=true;
        firstNotReachSide=true;
    }

    private boolean firstReachSide=true;
    private boolean firstNotReachSide=true;
    private float moveDistance=0;
    private void processOnDelta(float x, float y,boolean overSide) {

        if (overSide){
            if (firstReachSide) {
                mFirstPressDown.set(x, y);
                mProcessCurrentDelta.set(0, 0);
                firstReachSide=false;
            }
            float deltaY=y-mFirstPressDown.y;
            mProcessLastDelta.set(mProcessCurrentDelta.x,mProcessCurrentDelta.y);
            mProcessCurrentDelta.set(0,  (float) ((deltaY/Math.abs(deltaY))* Math.sqrt(Math.abs(deltaY)*heightFactor)));
            firstNotReachSide=true;
        }else {
            if (firstNotReachSide){
                mFirstPressDown.set(x, y);
                mProcessCurrentDelta.set(0, 0);
                firstNotReachSide=false;
            }
            float deltaY=y-mFirstPressDown.y;
            mProcessLastDelta.set(mProcessCurrentDelta.x,mProcessCurrentDelta.y);
            mProcessCurrentDelta.set(0, deltaY);
            moveDistance=deltaY;
            firstReachSide=true;
        }
    }

    //上一次和当前此处理后距离的相差
    public float getDeltaY(){
        return mProcessCurrentDelta.y-mProcessLastDelta.y;
    }

    public void setViewWidthAndHeight(int width,int height){
        this.viewWidth=width;
        this.viewHeight=height;
    }

    protected void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public int getLastPosY() {
        return mLastPos;
    }

    public int getCurrentPosY() {
        return mCurrentPos;
    }

    public float getOffsetToRefreshFactor(){
        return offsetToRefreshFactor;
    }

    /**
     * Update current position before update the UI
     */
    public final void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
        mCurrentPos = current;
        onUpdatePos(current, mLastPos);
    }

    protected void onUpdatePos(int current, int last) {

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        updateHeight();

        heightFactor= (int) (mHeaderHeight*mHeaderHeight*1.0f/(viewHeight-mHeaderHeight)*1.5);
    }

    protected void updateHeight() {
        mOffsetToRefresh = (int) (offsetToRefreshFactor * mHeaderHeight);
        mOffsetToKeepHeaderWhileLoading=mOffsetToRefresh;
    }

    public void convertFrom(PtrIndicator ptrSlider) {
        mCurrentPos = ptrSlider.mCurrentPos;
        mLastPos = ptrSlider.mLastPos;
        mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return mCurrentPos > POS_START;
    }

    public boolean hasJustLeftStartPosition() {
        return mLastPos == POS_START && hasLeftStartPosition();
    }

    public boolean hasJustBackToStartPosition() {
        return mLastPos != POS_START && isInStartPosition();
    }

    public boolean isOverOffsetToRefresh() {
        return mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean crossRefreshLineFromTopToBottom() {
        //return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
        Log.i(TAG,"lastScrollY:"+lastScrollY+" currentScrollY:"+currentScrollY+" mHeaderHeight*0.8:"+mHeaderHeight*offsetToRefreshFactor);
        return currentScrollY<-mHeaderHeight*offsetToRefreshFactor;
    }

    public boolean overRefreshLineFromBottomToTop(){
        return currentScrollY>-mHeaderHeight*offsetToRefreshFactor;
    }

    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return mCurrentPos > getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mOffsetToKeepHeaderWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading : mHeaderHeight;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public float getLastPercent() {
        final float oldPercent = mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
        return oldPercent;
    }

    public float getCurrentPercent() {
        final float currentPercent = mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
        return currentPercent;
    }

    public boolean willOverTop(int to) {
        return to < POS_START;
    }
}