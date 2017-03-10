package com.dansejijie.library.widget.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by tygzx on 17/2/28.
 */

public class  CustomScrollView extends AbstractScrollView{


    protected View mHeaderView;

    protected boolean mMeasureAllChildren = false;

    protected final ArrayList<View> mMatchParentChildren = new ArrayList<View>(1);

    private PtrIndicator mPtrIndicator;




    //当出现过度下拉的时候，在空白位置显示需要填充的颜色
    protected int mEmptyColor= Color.RED;

    protected Rect mEmptyRect=new Rect(0,0,0,0);//保存需要填充的位置信息

    protected Paint mPaint=new Paint();

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initScrollView() {
        super.initScrollView();
        mPtrIndicator=new PtrIndicator();
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    public void addContentView(View content){
        addView(content);
    }

    public void addHeaderView(View child){
        mHeaderView=child;
        addView(mHeaderView);
    }

    public void removeHeaderView(View child){
        removeView(child);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        if (getChildAt(0)==mHeaderView){
            i=0;
        }else {
            i=1;
        }
        return super.getChildDrawingOrder(childCount, i);
    }

    public void setPullToRefresh(boolean b){
        mPtrIndicator.setPullToRefresh(b);
    }

    public void setPullToFooterRefresh(boolean b){
        mPtrIndicator.setPullToFooterRefresh(b);
    }

    public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
        mPtrIndicator.setPtrUIHandler(ptrUIHandler);
    }

    @SuppressWarnings({"unused"})
    public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
        mPtrIndicator.removePtrUIHandler();
    }

    final public void refreshComplete() {

        mPtrIndicator.setRefreshComplete();
        if (mPtrIndicator.isRefreshStatusComplete()){
            if (mScroller.springBack(0,getScrollY(),0,getScrollRangeX(),0,getScrollRangeY())) {
                postInvalidateOnAnimation();
            }
        }
    }

    final public void refreshFooterComplete(){

        mPtrIndicator.setRefreshFooterComplete();
        if (mPtrIndicator.isRefreshStatusFooterInit()){
            if (mScroller.springBack(0,getScrollY(),0,getScrollRangeX(),0,getScrollRangeY())) {
                postInvalidateOnAnimation();
            }
        }
    }


    /**
     * 判断当前是否正在越边界
     * @return
     */
    private boolean isOverScrollY(){
        return getScrollY()<0||getScrollY()>getScrollRangeY();
    }

    public void setEmptyColor(int color){
        mEmptyColor=color;
    }

    /**
     *计算过度滑动后空白位置
     * 由于onDraw会受scrollX,scrollY影响，所以在计算的时候考虑这两值
     * 坐标映射关系，下面函数计算后的mEmptyRect,在X和Y坐标上分别经过-ScrollX,-ScrollY,得到值重新值画在canvas上
     * 下面的算式经过了简化，不具有参考价值
     */
    public void calculateEmptyRect(){

        int parentWidth=getWidth()-getPaddingLeft()-getPaddingRight();
        int parentHeight=getHeight()-getPaddingTop()-getPaddingBottom();

        if (getScrollY()<0){//Top
            mEmptyRect.set(getPaddingLeft(),getScrollY()+getPaddingTop(),parentWidth+getPaddingLeft(),getPaddingTop());
        }else if (getScrollY()>getScrollRangeY()){
            mEmptyRect.set(getPaddingLeft(),abstractGetContentView().getMeasuredHeight()+getPaddingTop(),parentWidth+getPaddingLeft(),getPaddingTop()+parentHeight+getScrollY());
        }else if (getScrollX()<0){
            mEmptyRect.set(getScrollX()+getPaddingLeft(),getPaddingTop(),getPaddingLeft(),parentHeight+getPaddingTop());
        }else if (getScrollX()>getScrollRangeX()){
            mEmptyRect.set(abstractGetContentView().getMeasuredWidth()+getPaddingLeft(),getPaddingTop(),getPaddingLeft()+parentWidth+getScrollX(),parentHeight+getPaddingTop());
        }else {
            mEmptyRect.set(0,0,0,0);
        }
    }



    @Override
    protected void abstractOnFinishInflate() {

        final int childCount = getChildCount();
        if (childCount > 2) {
            throw new IllegalStateException("ScrollView only can host 2 elements");
        } else if (childCount == 2) {
            // not specify header or content
            if (mHeaderView == null) {

                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                if (child1 instanceof PtrUIHandler) {
                    mHeaderView = child1;
                } else if (child2 instanceof PtrUIHandler) {
                    mHeaderView = child2;
                } else {
                    // both are not specified
                    if (mHeaderView == null) {
                        mHeaderView = child1;
                    }
                }
            }
        } else if (childCount == 0) {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
            addContentView(errorView);
        }
    }

    @Override
    protected void abstractOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (mMeasureAllChildren || child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == FrameLayout.LayoutParams.MATCH_PARENT ||
                            lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Check against our foreground's minimum height and width
        final Drawable drawable = null;//TODO
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View mContentView=abstractGetContentView();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin  - mHeaderView.getMeasuredHeight();
            int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();

            //居中处理
            int parentWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
            int childWidth=mHeaderView.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int offsetX=(parentWidth-childWidth)/2+lp.leftMargin+paddingLeft;
            right=offsetX+mHeaderView.getMeasuredWidth();
            mHeaderView.layout(offsetX, top, right, bottom);

            mPtrIndicator.setHeaderHeight(mHeaderView.getMeasuredHeight());
        }
        if (mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin ;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            mContentView.layout(left, top, right, bottom);

            mPtrIndicator.setScrollRange(getScrollRangeY());
        }

        mPtrIndicator.setCurrentScrollPoint(getScrollX(),getScrollY());
        mPtrIndicator.setPreScrollPoint(getScrollX(),getScrollY());
    }

    @Override
    protected void abstractMeasureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        throw new IllegalStateException("current method is called");
    }


    @Override
    protected void abstractMeasureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {

//        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//
//        final int childWidthMeasureSpec=getChildMeasureSpec(parentWidthMeasureSpec,0,lp.width);
//        final int childHeightMeasureSpec=getChildMeasureSpec(parentHeightMeasureSpec,0,lp.height);
//        child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
//        Log.i(TAG,"height:"+child.getMeasuredHeight()+" width:"+child.getMeasuredWidth());

        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);



//        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//
//                final int usedTotalWidth = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed;
//        final int childWidthMeasureSpec = child instanceof ViewGroup
//                ? MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotalWidth), MeasureSpec.UNSPECIFIED)
//                : getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
//
//        final int usedTotalHeight = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed;
//        final int childHeightMeasureSpec = child instanceof ViewGroup
//                ? MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotalHeight), MeasureSpec.UNSPECIFIED)
//                :getChildMeasureSpec(parentHeightMeasureSpec,getPaddingTop()+getPaddingBottom()+lp.bottomMargin+lp.topMargin,lp.height);
//
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }



    @Override
    protected View abstractGetContentView() {

        for (int i=0;i<getChildCount();i++){
            if (getChildAt(i)!=mHeaderView){
                return getChildAt(i);
            }
        }
        return getChildAt(0);
    }

    @Override
    protected boolean abstractOverScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        //Log.i(TAG,"abstractOverScrollBy:deltaY:"+deltaY+" scrollY:"+scrollY+" scrollY:"+scrollY);

        maxOverScrollX=300;
        maxOverScrollY=300;

        final int overScrollMode = getOverScrollMode();
        final boolean canScrollHorizontal =
                computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        final boolean canScrollVertical =
                computeVerticalScrollRange() > computeVerticalScrollExtent();
        final boolean overScrollHorizontal = overScrollMode == OVER_SCROLL_ALWAYS ||
                (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollHorizontal);
        final boolean overScrollVertical = overScrollMode == OVER_SCROLL_ALWAYS ||
                (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical);

        int newScrollX = scrollX + deltaX;
        if (!overScrollHorizontal) {
            maxOverScrollX = 0;
        }

        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }

        // Clamp values if at the limits and record
        final int left = -maxOverScrollX;
        final int right = maxOverScrollX + scrollRangeX;
        final int top = -maxOverScrollY;
        final int bottom = maxOverScrollY + scrollRangeY;

        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }

        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }

        /***********内部自己添加的判断事件************/
        /************当要到超过边缘时，先设置到边缘，下次允许超过边缘,该方法便于刚到边缘的检测*********/
        resetParentHasNestedStateWhenChildReachBoard();
        int oldScrollX=getScrollX();
        int oldScrollY=getScrollY();
        if (oldScrollY>0&&newScrollY<0){
            newScrollY=0;
            topReachBoard=true;
        }
        if (oldScrollY==0){
            topReachBoard=true;
        }
        if (oldScrollY<getScrollRangeY()&&newScrollY>getScrollRangeY()){
            newScrollY=getScrollRangeY();
            bottomReachBoard=true;
        }
        if (oldScrollY==getScrollRangeY()){
            bottomReachBoard=true;
        }

        if (oldScrollX>0&&newScrollX<0){
            newScrollX=0;
            leftReachBoard=true;
        }
        if (oldScrollX==0){
            leftReachBoard=true;
        }
        if (oldScrollX<getScrollRangeX()&&newScrollX>getScrollRangeX()){
            newScrollX=getScrollRangeX();
            rightReachBoard=true;
        }

        if (oldScrollX==getScrollRangeX()){
            rightReachBoard=true;
        }

        /**********当父View有NestParent的情况下，则子View在该方向上不进行过度滑动******/

        if (orientation==VERTICAL){
            if (parentTopHasNested&&newScrollY<0){
                newScrollY=0;
            }else if (parentBottomHasNested&&newScrollY>getScrollRangeY()){
                newScrollY=getScrollRangeY();
            }
        }else {
            if (parentLeftHasNested&&newScrollX<0){
                newScrollX=0;
            }else if (parentRightHasNested&&newScrollX>getScrollRangeX()){
                newScrollX=getScrollRangeX();
            }
        }



        /*******************当此View作为父View，且被子View通知过嵌套滑动的话，则父View不过度滑动************************/
        if(notifiedScrollingAsParent||notifiedScrollingFlingAsParent){//最好区分上下左右

            if (orientation==VERTICAL){
                if (newScrollY<0){
                    newScrollY=0;
                }else if (newScrollY>getScrollRangeY()){
                    newScrollY=getScrollRangeY();
                }
            }else {
                if (newScrollX<0){
                    newScrollX=0;
                }else if (newScrollX>getScrollRangeX()){
                    newScrollX=getScrollRangeX();
                }
            }
        }

        /*************结束**************/


        abstractOnOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

        return clampedX || clampedY;
    }

    @Override
    protected void abstractOnOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        // Treat animating scrolls differently; see #computeScroll() for why.
        if (!mScroller.isFinished()) {
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            abstractScrollTo(scrollX,scrollY);
            postInvalidateOnAnimation();
            //invalidateParentIfNeeded();
            if (clampedY||clampedX) {
                mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0, getScrollRangeY());
            }
        } else {
            abstractScrollTo(scrollX,scrollY);
        }

        awakenScrollBars();
    }

    @Override
    protected void abstractScrollTo(int scrollX, int scrollY) {

        if (orientation==VERTICAL){
            scrollX=0;
            mPtrIndicator.setPreScrollPoint(scrollX,scrollY);
        }else {
            scrollY=0;
        }

        int oldScrollX=getScrollX();
        int oldScrollY=getScrollY();

        scrollTo(scrollX,scrollY);

        int directionX=getScrollX()-oldScrollX;
        int directionY=getScrollY()-oldScrollY;
        if (directionX==0&&directionY==0){
            return;
        }

        //由于leftReachBoaed等一开始就全部被设置true里，但是若快速拖动且方向为Y，则在判断isFling()为true时，必然topReachBoard和bottomReachBoard被设置为false,但是leftReachBoard还是true，所以用orientation加强过滤
        if ((((leftReachBoard||rightReachBoard)&&orientation==HORIZONTAL)||((topReachBoard||bottomReachBoard)&&orientation==VERTICAL))){

            if (orientation==VERTICAL){
                startNestedScroll(SCROLL_AXIS_VERTICAL);
            }else {
                startNestedScroll(SCROLL_AXIS_HORIZONTAL);
            }

            if (mChildHelper.hasNestedScrollingParent()){
                float vx=0;
                float vy=0;
                if (orientation==VERTICAL){
                    vy=mScroller.getCurrVelocity();//获得的值都是正数，所以要区分下方向
                    vy=directionY<0?-vy:vy;
                }else {
                    vx=mScroller.getCurrVelocity();
                    vx=directionX<0?-vx:vx;
                }
                mScroller.abortAnimation();
                vx=vx/2;
                vy=vy/2;//减缓速度
                dispatchNestedFling(vx,vy,false);
                stopNestedScroll();
            }

        }else {
            //Log.i(TAG,"mChildHelper.hasNestedScrollingParent():"+mChildHelper.hasNestedScrollingParent()+" isFling():"+isFling());
        }

        //计算过度下拉后的空白位置
        calculateEmptyRect();

        mPtrIndicator.setCurrentScrollPoint(getScrollX(),getScrollY());
    }

    @Override
    protected void abstractOnTouchMove(MotionEvent ev) {
        mPtrIndicator.onMove(ev.getX(),ev.getY(),isOverScrollY());
    }

    @Override
    protected int abstractOnTouchMoveProcessDeltaY(int deltaY) {
        return deltaY;
    }

    @Override
    protected void abstractOnTouchDown(MotionEvent ev) {
        mPtrIndicator.onPressDown(ev.getX(),ev.getY());

        mPtrIndicator.processOnMove(ev.getX(),ev.getY(),0,0);

    }

    @Override
    protected void abstractOnTouchUp() {
        mPtrIndicator.onRelease();
    }

    @Override
    protected void abstractOnTouchUpAndScroll(int initialVelocityX,int initialVelocityY) {

        if (orientation==VERTICAL){
            if ((Math.abs(initialVelocityY) > mMinimumVelocity)&&!mPtrIndicator.isCrossRefreshLineFromTopToBottom()) {

                if (initialVelocityY>0&&topReachBoard){//// TODO: 17/3/6 速度向下是正数,这里应该有缺陷的
                    return;
                }
                if (initialVelocityY<0&&bottomReachBoard){
                    return;
                }
                flingWithNestedDispatch(0,-initialVelocityY);

            }else {
                if(mPtrIndicator.isRefreshStatusReadyLoading()&&!mPtrIndicator.isUnderTouch()&&mPtrIndicator.isCrossRefreshLineFromTopToBottom()){
                    smoothScrollTo(0,-mPtrIndicator.getOffsetToRefresh());
                }else if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0, getScrollRangeY())){
                    postInvalidateOnAnimation();
                }
            }
        }else {
            if ((Math.abs(initialVelocityX) > mMinimumVelocity) ) {

                if (initialVelocityX>0&&leftReachBoard){
                    return;
                }
                if (initialVelocityX<0&&rightReachBoard){
                    return;
                }
                flingWithNestedDispatch(-initialVelocityX,0);

            }else {

                if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0, getScrollRangeY())){
                    postInvalidateOnAnimation();
                }

            }
        }

    }

    @Override
    protected void abstractOnTouchCancel() {
        mPtrIndicator.onRelease();
        if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRangeY())) {
            postInvalidateOnAnimation();
        }
    }

    @Override
    protected void abstractDraw(Canvas canvas) {

        mPaint.setColor(mEmptyColor);
    }

    @Override
    protected void abstractOnNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final int oldScrollY = getScrollY();
        final int oldScrollX = getScrollX();

        //用来处理在嵌套滑动中，若是父类，则不过度滑动
        if (orientation==VERTICAL){
            if (getScrollY()+dyUnconsumed<0){
                dyUnconsumed=-getScrollY();
            }else if (getScrollY()+dyUnconsumed>getScrollRangeY()){
                dyUnconsumed=getScrollRangeY()-getScrollY();
            }
        }else {
            if (getScrollX()+dxUnconsumed<0){
                dxUnconsumed=-getScrollX();
            }else if (getScrollX()+dxUnconsumed>getScrollRangeX()){
                dxUnconsumed=getScrollRangeX()-getScrollX();
            }
        }
        abstractOverScrollBy(dxUnconsumed,dyUnconsumed,getScrollX(),getScrollY(),getScrollRangeX(),getScrollRangeY(),mOverscrollDistance,mOverflingDistance,true);
        //scrollBy(dxConsumed, dyUnconsumed);

        final int myConsumedY = getScrollY() - oldScrollY;
        final int myUnconsumedY = dyUnconsumed - myConsumedY;
        final int myConsumedX=getScrollX()-oldScrollX;
        final int myUnconsumedX=dxUnconsumed-myConsumedX;
        //Log.i(TAG,"onNestedScroll"+" consumedY:"+myConsumedY);
        dispatchNestedScroll(myConsumedX, myConsumedY, myUnconsumedX, myUnconsumedY, null);

    }

    @Override
    protected void abstractOnNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        if (orientation==VERTICAL){
            if (getScrollY()+dy<0){
                dy=-getScrollY();
            }else if (getScrollY()+dy>getScrollRangeY()){
                dy=getScrollRangeY()-getScrollY();
            }
        }else {
            if (getScrollX()+dx<0){
                dx=-getScrollX();
            }else if (getScrollX()+dx>getScrollRangeX()){
                dx=getScrollRangeX()-getScrollX();
            }
        }

        abstractOverScrollBy(dx,dy,getScrollX(),getScrollY(),getScrollRangeX(),getScrollRangeY(),mOverscrollDistance,mOverflingDistance,true);
    }
}
