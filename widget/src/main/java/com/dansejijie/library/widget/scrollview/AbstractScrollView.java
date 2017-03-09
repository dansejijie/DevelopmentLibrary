package com.dansejijie.library.widget.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by tygzx on 17/2/28.
 */

public abstract class AbstractScrollView extends ViewGroup implements NestedScrollingParent,NestedScrollingChild,ScrollingView{

    static final int ANIMATED_SCROLL_GAP = 250;

    static final float MAX_SCROLL_FACTOR = 0.5f;

    public String TAG = AbstractScrollView.class.getSimpleName();

    private long mLastScroll;

    private final Rect mTempRect = new Rect();
    protected EOverScroller mScroller;
    private EdgeEffect mEdgeGlowTop;
    private EdgeEffect mEdgeGlowBottom;


    /**
     * Position of the last motion event.
     */
    private int mLastMotionY;

    private int mLastMotionX;

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private boolean mIsLayoutDirty = true;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private View mChildToScrollTo = null;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private boolean mIsBeingDragged = false;

    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;

    /**
     * When set to true, the scroll view measure its child to make it fill the currently
     * visible area.
     */
    @ViewDebug.ExportedProperty(category = "layout")

    /**
     * Whether arrow scrolling is animated.
     */
    private boolean mSmoothScrollingEnabled = true;

    private int mTouchSlop;
    protected int mMinimumVelocity;
    private int mMaximumVelocity;

    protected int mOverscrollDistance = 200;
    protected int mOverflingDistance = 100;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = INVALID_POINTER;

    /**
     * Used during scrolling to retrieve the new offset within the window.
     */
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;
    private int mNestedXOffset;


    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;


    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    protected int orientation= VERTICAL;

    protected NestedScrollingParentHelper mParentHelper;
    protected NestedScrollingChildHelper mChildHelper;

    protected boolean leftReachBoard,rightReachBoard,topReachBoard,bottomReachBoard;

    protected boolean parentLeftHasNested,parentRightHasNested,parentTopHasNested,parentBottomHasNested;

    protected boolean notifiedScrollingAsParent=false;//被子View通知过嵌套滑动，则不再过渡滑动

    protected boolean notifiedScrollingFlingAsParent=false;

    //protected int notifiedScrollingByChildViewId=-1;//通知父嵌套滑动的子ViewId

    public AbstractScrollView(Context context) {
        this(context, null);
    }

    public AbstractScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getHorizontalFadingEdgeLength();
        if (getScrollX() < length) {
            return getScrollX() / (float) length;
        }

        return 1.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getHorizontalFadingEdgeLength();
        final int rightEdge = getWidth() - getPaddingRight();
        final int span = getChildAt(0).getRight() - getScrollX() - rightEdge;
        if (span < length) {
            return span / (float) length;
        }

        return 1.0f;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getVerticalFadingEdgeLength();
        if (getScrollY() < length) {
            return getScrollY() / (float) length;
        }

        return 1.0f;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }

        final int length = getVerticalFadingEdgeLength();
        final int bottomEdge = getHeight() - getPaddingBottom();
        final int span = abstractGetContentView().getBottom() - getScrollY() - bottomEdge;
        if (span < length) {
            return span / (float) length;
        }

        return 1.0f;
    }



    protected void initScrollView() {
        mScroller = new EOverScroller(getContext(),new CustomAccelerateDecelerate());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        Logger.init(TAG).methodCount(3).methodOffset(2);
    }


    /**
     * @return Returns true this ScrollView can be scrolled
     */
    private boolean canScroll() {
        View child = abstractGetContentView();
        if (child != null) {
            if (orientation==VERTICAL){
                int childHeight = child.getHeight();
                return getHeight() < childHeight + getPaddingTop() + getPaddingBottom();
            }else {
                int childWidth = child.getWidth();
                return getWidth() < childWidth + getPaddingLeft() + getPaddingRight();
            }

        }
        return false;
    }

    @Override
    protected void onFinishInflate() {
        abstractOnFinishInflate();
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        abstractOnMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event);
    }


    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollY = getScrollY();
            final View child = abstractGetContentView();
            return !(y < child.getTop() - scrollY
                    || y >= child.getBottom() - scrollY
                    || x < child.getLeft()
                    || x >= child.getRight());
        }
        return false;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        /*
        * Shortcut the most recurring case: the user is in the dragging
        * state and he is moving his finger.  We want to intercept this
        * motion.
        */
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        /*
         * Don't try to intercept touch if we can't scroll anyway.
         */
//        if (getScrollY() == 0 && !canScrollVertically(1)) {
//            return false;
//        }


        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                Logger.d("%s onInterceptTouchEvent:ACTION_MOVE",TAG);
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int y = (int) MotionEventCompat.getY(ev, pointerIndex);
                final int x = (int) MotionEventCompat.getX(ev, pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if ((yDiff > mTouchSlop &&orientation==VERTICAL&&(getNestedScrollAxes() & SCROLL_AXIS_VERTICAL) == 0/*)||(xDiff>mTouchSlop&&orientation==HORIZONTAL&&(getNestedScrollAxes()&SCROLL_AXIS_HORIZONTAL)==0*/)) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;

                    mLastMotionX = x;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    mNestedYOffset = 0;
                    mNestedXOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        Logger.e("%s onInterceptTouchEvent:requestDisallowInterceptTouchEvent:before",TAG);
                        parent.requestDisallowInterceptTouchEvent(true);
                        Logger.e("%s onInterceptTouchEvent:requestDisallowInterceptTouchEvent:after",TAG);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                Logger.d("%s onInterceptTouchEvent:ACTION_DOWN",TAG);
                final int y = (int) ev.getY();
                final int x = (int) ev.getX();

                if (!inChild((int) x, (int) y)) {
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }

                mLastMotionY = y;
                mLastMotionX = x;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);

                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished();

                if (orientation==VERTICAL){
                    startNestedScroll(SCROLL_AXIS_VERTICAL);
                }else {
                    startNestedScroll(SCROLL_AXIS_HORIZONTAL);
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
                Logger.d("%s onInterceptTouchEvent:ACTION_CANCEL",TAG);
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0,  getScrollRangeY())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }

                //stopNestedScroll();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                Logger.d("%s onInterceptTouchEvent:ACTION_POINTER_DOWN",TAG);
                final int index = ev.getActionIndex();
                mLastMotionX = (int) ev.getX(index);
                mLastMotionY= (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                Logger.d("%s onInterceptTouchEvent:ACTION_POINTER_UP",TAG);
                onSecondaryPointerUp(ev);
                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
                mLastMotionY=(int)ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }

        /*
        * The only time we want to intercept motion events is if we are in the
        * drag mode.
        */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(ev);

        MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
            mNestedXOffset = 0;
        }
        vtev.offsetLocation(mNestedXOffset, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                Logger.d("%s onTouchEvent:ACTION_DOWN",TAG);
                notifiedScrollingAsParent=false;
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionY = (int) ev.getY();
                mLastMotionX = (int) ev.getX();
                mActivePointerId =  MotionEventCompat.getPointerId(ev, 0);
                if (orientation==VERTICAL){
                    startNestedScroll(SCROLL_AXIS_VERTICAL);
                }else {
                    startNestedScroll(SCROLL_AXIS_HORIZONTAL);
                }
                abstractOnTouchDown(ev);
                Logger.e("%s onTouchEvent:mIsBeingDragged:%s",TAG,mIsBeingDragged+" ");
                break;
            }
            case MotionEvent.ACTION_MOVE:

                Logger.d("%s onTouchEvent:ACTION_MOVE",TAG);

                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                abstractOnTouchMove(ev);

                final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
                final int x = (int) MotionEventCompat.getX(ev, activePointerIndex);
                int deltaX = mLastMotionX - x;
                int deltaY = mLastMotionY - y;

                if (orientation==VERTICAL){
                    deltaX=0;
                }else if (orientation==HORIZONTAL){
                    deltaY=0;
                }
                //dispatchNestedPreScroll只有在子View到达边才会被调用
                if (leftReachBoard&&deltaX<0||rightReachBoard&&deltaX>0||topReachBoard&&deltaY<0||bottomReachBoard&&deltaY>0){

                    if (dispatchNestedPreScroll(deltaX, deltaY, mScrollConsumed, mScrollOffset)) {



                        deltaY -= mScrollConsumed[1];
                        deltaX -= mScrollConsumed[0];
                        vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                        mNestedXOffset += mScrollOffset[0];

                        if (orientation==VERTICAL){  //// TODO: 这里的正负要搞清楚，别弄错了
                            if (mScrollConsumed[1]<0){
                                parentTopHasNested=true;
                            }else if (mScrollConsumed[1]>0){
                                parentBottomHasNested=true;
                            }
                        }else {
                            if (mScrollConsumed[0]<0){
                                parentLeftHasNested=true;
                            }else if (mScrollConsumed[0]>0){
                                parentRightHasNested=true;
                            }
                        }
                    }

                }


                int delta =orientation==VERTICAL? deltaY:deltaX;
                if (!mIsBeingDragged && Math.abs(delta) > mTouchSlop) {
                    final ViewParent parent = getParent();

                    if (parent != null) {
                        Logger.e("%s onTouchEvent:requestDisallowInterceptTouchEvent:before",TAG);
                        parent.requestDisallowInterceptTouchEvent(true);
                        Logger.e("%s onTouchEvent:requestDisallowInterceptTouchEvent:after",TAG);
                    }

                    mIsBeingDragged = true;
                    if (delta > 0) {
                        delta -= mTouchSlop;
                    } else {
                        delta += mTouchSlop;
                    }
                }
                if (orientation==VERTICAL){
                    deltaY=delta;
                }else if (orientation==HORIZONTAL){
                    deltaX=delta;
                }
                //Log.i(TAG,"onTouchEvent:mIsBeingDragged:"+mIsBeingDragged);
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionY = y - mScrollOffset[1];
                    mLastMotionX = x - mScrollOffset[0];

                    final int oldY = getScrollY();
                    final int oldX = getScrollX();
                    final int rangeY = getScrollRangeY();
                    final int rangeX = getScrollRangeX();

                    // Calling overScrollBy will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    deltaY=abstractOnTouchMoveProcessDeltaY(deltaY);

                    if (abstractOverScrollBy(deltaX, deltaY, getScrollX(), getScrollY(), rangeX, rangeY, mOverscrollDistance, mOverscrollDistance, true)
                            && !hasNestedScrollingParent()) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }

                    final int scrolledDeltaY = getScrollY() - oldY;
                    final int scrolledDeltaX = getScrollX() - oldX;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    final int unconsumedX = deltaX - scrolledDeltaX;

                    if (dispatchNestedScroll(scrolledDeltaX, scrolledDeltaY, unconsumedX, unconsumedY, mScrollOffset)) {


                        mLastMotionY -= mScrollOffset[1];
                        mLastMotionX -= mScrollOffset[0];

                        vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                        mNestedXOffset += mScrollOffset[0];
                    }

                    mScrollOffset[0]=0;//todo 这里自己添加的，解决加速上滑的问题
                    mScrollOffset[1]=0;
                }

                break;
            case MotionEvent.ACTION_UP:
                Logger.d("%s onTouchEvent:ACTION_UP",TAG);
                abstractOnTouchUp();
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocityY = (int) velocityTracker.getYVelocity(mActivePointerId);
                    int initialVelocityX = (int) velocityTracker.getXVelocity(mActivePointerId);
                    abstractOnTouchUpAndScroll(initialVelocityX,initialVelocityY);
                }
                mActivePointerId = INVALID_POINTER;
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                Logger.d("%s onTouchEvent:ACTION_CANCEL",TAG);
                if (mIsBeingDragged && getChildCount() > 0) {
                    abstractOnTouchCancel();
                }
                mActivePointerId = INVALID_POINTER;
                endDrag();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                Logger.d("%s onTouchEvent:ACTION_POINTER_DOWN",TAG);
                final int index = ev.getActionIndex();
                mLastMotionY = (int) ev.getY(index);
                mLastMotionX= (int) ev.getX(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                Logger.d("%s onTouchEvent:ACTION_POINTER_UP",TAG);
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = (int) ev.getX(newPointerIndex);
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL: {
                    if (!mIsBeingDragged) {
                        final float vscroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
                        if (vscroll != 0) {
                            final int delta = (int) (vscroll * 0.5);//TODO
                            final int rangeX = getScrollRangeX();
                            final int rangeY = getScrollRangeY();
                            int oldScrollX = getScrollX();
                            int oldScrollY = getScrollY();
                            int newScrollX = oldScrollX - delta;
                            int newScrollY = oldScrollY - delta;
                            if (newScrollY < 0) {
                                newScrollY = 0;
                            } else if (newScrollY > rangeY) {
                                newScrollY = rangeY;
                            }
                            if (newScrollX < 0){
                                newScrollX = 0;
                            }else if (newScrollX > rangeY){
                                newScrollX = rangeX;
                            }
                            if (newScrollY != oldScrollY&&orientation==VERTICAL) {
                                super.scrollTo(getScrollX(), newScrollY);
                                return true;
                            }else if (newScrollX != oldScrollX&&orientation==HORIZONTAL){
                                super.scrollTo(newScrollX,getScrollY());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return super.onGenericMotionEvent(event);
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY,
//                                  boolean clampedX, boolean clampedY) {
//        // Treat animating scrolls differently; see #computeScroll() for why.
//        if (!mScroller.isFinished()) {
//            Log.i(TAG, "onOverScrolled:!mScroller.isFinished()");
//            final int oldX = getScrollX();
//            final int oldY = getScrollY();
//            abstractScrollTo(scrollX,scrollY);
//            postInvalidateOnAnimation();
//            //invalidateParentIfNeeded();
//            if (clampedY) {
//                mScroller.springBack(getScrollX(), getScrollY(), 0, 0, getScrollRangeX(), getScrollRangeY());
//            }
//        } else {
//            abstractScrollTo(scrollX,scrollY);
//        }
//
//        awakenScrollBars();
//    }

    protected int getScrollRangeX(){
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = abstractGetContentView();
            scrollRange = Math.max(0,
                    child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }

    protected int getScrollRangeY() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = abstractGetContentView();
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    /**
     * <p>
     * Finds the next focusable component that fits in the specified bounds.
     * </p>
     *
     * @param topFocus look for a candidate is the one at the top of the bounds
     *                 if topFocus is true, or at the bottom of the bounds if topFocus is
     *                 false
     * @param top      the top offset of the bounds in which a focusable must be
     *                 found
     * @param bottom   the bottom offset of the bounds in which a focusable must
     *                 be found
     * @return the next focusable component in the bounds or null if none can
     * be found
     */
    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {

        List<View> focusables = getFocusables(View.FOCUS_FORWARD);
        View focusCandidate = null;

        /*
         * A fully contained focusable is one where its top is below the bound's
         * top, and its bottom is above the bound's bottom. A partially
         * contained focusable is one where some part of it is within the
         * bounds, but it also has some part that is not within bounds.  A fully contained
         * focusable is preferred to a partially contained focusable.
         */
        boolean foundFullyContainedFocusable = false;

        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();

            if (top < viewBottom && viewTop < bottom) {
                /*
                 * the focusable is in the target area, it is a candidate for
                 * focusing
                 */

                final boolean viewIsFullyContained = (top < viewTop) &&
                        (viewBottom < bottom);

                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    final boolean viewIsCloserToBoundary =
                            (topFocus && viewTop < focusCandidate.getTop()) ||
                                    (!topFocus && viewBottom > focusCandidate
                                            .getBottom());

                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            /*
                             * We're dealing with only fully contained views, so
                             * it has to be closer to the boundary to beat our
                             * candidate
                             */
                            focusCandidate = view;
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view;
                            foundFullyContainedFocusable = true;
                        } else if (viewIsCloserToBoundary) {
                            /*
                             * Partially contained view beats another partially
                             * contained view if it's closer
                             */
                            focusCandidate = view;
                        }
                    }
                }
            }
        }

        return focusCandidate;

    }



    /**
     * @return whether the descendant of this scroll view is scrolled off
     * screen.
     */
    private boolean isOffScreen(View descendant) {
        return !isWithinDeltaOfScreen(descendant, 0, getHeight());
    }

    /**
     * @return whether the descendant of this scroll view is within delta
     * pixels of being on the screen.
     */
    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        if (orientation==VERTICAL){
            return (mTempRect.bottom + delta) >= getScrollY()
                    && (mTempRect.top - delta) <= (getScrollY() + height);
        }else {
            return (mTempRect.right + delta) >= getScrollX()
                    && (mTempRect.left - delta) <= (getScrollX() + getWidth());
        }

    }

    /**
     * Smooth scroll by a Y delta
     *
     * @param delta the number of pixels to scroll by on the Y axis
     */
    private void doScrollY(int delta) {
        if (delta != 0) {
            if (mSmoothScrollingEnabled) {
                smoothScrollBy(0, delta);
            } else {
                scrollBy(0, delta);
            }
        }
    }

    /**
     * Smooth scroll by a X delta
     *
     * @param delta the number of pixels to scroll by on the X axis
     */
    private void doScrollX(int delta) {
        if (delta != 0) {
            if (mSmoothScrollingEnabled) {
                smoothScrollBy(delta, 0);
            } else {
                scrollBy(delta, 0);
            }
        }
    }

    protected boolean flingWithNestedDispatch(int velocityX, int velocityY) {


        final boolean canFlingX=true;
        final boolean canFlingY=true;
        if (orientation==VERTICAL){
            if (canFlingX){
                fling(velocityX,velocityY);
            }
        }else {
            if (canFlingY){
                fling(velocityX,velocityY);
            }
        }
        return true;

    }

    /**
     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
     *
     * @param dx the number of pixels to scroll by on the X axis
     * @param dy the number of pixels to scroll by on the Y axis
     */
    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }
        if (orientation==VERTICAL){
            dx=0;
        }else {
            dy=0;
        }
        long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
        if (duration > ANIMATED_SCROLL_GAP) {
//            final int height = getHeight() - getPaddingBottom() - getPaddingTop();
//            final int bottom = abstractGetContentView().getHeight();
//            final int maxY = Math.max(0, bottom - height);
//            final int scrollY = getScrollY();
//
//            final int width = getWidth() - getPaddingRight() - getPaddingLeft();
//            final int right = abstractGetContentView().getWidth();
//            final int maxX = Math.max(0,right - width);
//            final int scrollX = getScrollX();
//            dy = Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY;
//            dx = Math.max(0,Math.min(scrollX + dx, maxX)) - scrollX;
            mScroller.startScroll(getScrollX(), getScrollY(), dx, dy);

            postInvalidateOnAnimation();
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollBy(dx, dy);
        }
        mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }

    /**
     * Like {@link #scrollTo}, but scroll smoothly instead of immediately.
     *
     * @param x the position where to scroll on the X axis
     * @param y the position where to scroll on the Y axis
     */
    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - getScrollX(), y - getScrollY());
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        abstractMeasureChild(child,parentWidthMeasureSpec,parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        abstractMeasureChildWithMargins(child, parentWidthMeasureSpec,widthUsed,parentHeightMeasureSpec,heightUsed);
    }


    /**
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    @Override
    public int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = abstractGetContentView().getBottom();
        final int scrollY = getScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    @Override
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    /**
     * <p>The scroll range of a scroll view is the overall width of all of its
     * children.</p>
     */
    @Override
    public int computeHorizontalScrollRange() {
        final int count = getChildCount();
        final int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        if (count == 0) {
            return contentWidth;
        }

        int scrollRange = abstractGetContentView().getRight();
        final int scrollX = getScrollX();
        final int overscrollRight = Math.max(0, scrollRange - contentWidth);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }

        return scrollRange;
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override
    public void computeScroll() {
        //Log.i(TAG, "computeScroll");
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where getScrollX()/Y is different from what the app
            //         thinks it is.
            //
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int rangeX = getScrollRangeX();
                final int rangeY = getScrollRangeY();
                final int overscrollMode = getOverScrollMode();
//                final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS || (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                abstractOverScrollBy(x - oldX, y - oldY, oldX, oldY, rangeX, rangeY,
                        mOverflingDistance, mOverscrollDistance, false);
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);

//                if (canOverscroll) {
//                    if (y < 0 && oldY >= 0) {
//                        mEdgeGlowTop.onAbsorb((int) mScroller.getCurrVelocity());
//                    } else if (y > range && oldY <= range) {
//                        mEdgeGlowBottom.onAbsorb((int) mScroller.getCurrVelocity());
//                    }
//                }
            }

            if (!awakenScrollBars()) {
                // Keep on drawing until the animation has finished.
                postInvalidateOnAnimation();
            }
        } else {

            //Log.i(TAG,"computeScroll:died");
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param child the View to scroll to
     */
    private void scrollToChild(View child) {
        child.getDrawingRect(mTempRect);

        /* Offset from child's local coordinates to ScrollView coordinates */
        offsetDescendantRectToMyCoords(child, mTempRect);

        int scrollDeltaY = computeScrollDeltaYToGetChildRectOnScreen(mTempRect);
        int scrollDeltaX = computeScrollDeltaXToGetChildRectOnScreen(mTempRect);

        if (scrollDeltaY != 0||scrollDeltaX != 0) {
            scrollBy(scrollDeltaX, scrollDeltaY);
        }
    }

    /**
     * If rect is off screen, scroll just enough to get it (or at least the
     * first screen size chunk of it) on screen.
     *
     * @param rect      The rectangle.
     * @param immediate True to scroll immediately without animation
     * @return true if scrolling was performed
     */
    private boolean scrollToChildRect(Rect rect, boolean immediate) {

        int scrollDeltaY = computeScrollDeltaYToGetChildRectOnScreen(mTempRect);
        int scrollDeltaX = computeScrollDeltaXToGetChildRectOnScreen(mTempRect);

        if (scrollDeltaY != 0||scrollDeltaX != 0) {
            if (immediate){
                scrollBy(scrollDeltaX,scrollDeltaY);
            }else {
                smoothScrollBy(scrollDeltaX,scrollDeltaY);
            }
            scrollBy(scrollDeltaX, scrollDeltaY);

            return true;
        }

        return false;
    }

    /**
     * Compute the amount to scroll in the X direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect The rect.
     * @return The scroll delta.
     */
    protected int computeScrollDeltaXToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;

        int fadingEdge = getHorizontalFadingEdgeLength();

        // leave room for left fading edge as long as rect isn't at very left
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }

        // leave room for right fading edge as long as rect isn't at very right
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }

        int scrollXDelta = 0;

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move right to get it in view: move right just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += (rect.left - screenLeft);
            } else {
                // get entire rect at right of screen
                scrollXDelta += (rect.right - screenRight);
            }

            // make sure we aren't scrolling beyond the end of our content
            int right = getChildAt(0).getRight();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move right to get it in view: move right just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= (screenRight - rect.right);
            } else {
                // entire rect at left
                scrollXDelta -= (screenLeft - rect.left);
            }

            // make sure we aren't scrolling any further than the left our content
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }

    /**
     * Compute the amount to scroll in the Y direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect The rect.
     * @return The scroll delta.
     */
    protected int computeScrollDeltaYToGetChildRectOnScreen(Rect rect) {

        if (getChildCount() == 0) return 0;

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;

        int fadingEdge = getVerticalFadingEdgeLength();

        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }

        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < abstractGetContentView().getHeight()) {
            screenBottom -= fadingEdge;
        }

        int scrollYDelta = 0;

        if (rect.bottom > screenBottom && rect.top > screenTop) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += (rect.top - screenTop);
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += (rect.bottom - screenBottom);
            }

            // make sure we aren't scrolling beyond the end of our content
            int bottom = abstractGetContentView().getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.height() > height) {
                // screen size chunk
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (screenTop - rect.top);
            }

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (!mIsLayoutDirty) {
            scrollToChild(focused);
        } else {
            // The child may not be laid out yet, we can't compute the scroll yet
            mChildToScrollTo = focused;
        }
        super.requestChildFocus(child, focused);
    }


    /**
     * When looking for focus in children of a scroll view, need to be a little
     * more careful not to give focus to something that is scrolled off screen.
     * <p>
     * This is more expensive than the default {@link android.view.ViewGroup}
     * implementation, otherwise this behavior might have been made the default.
     */
    @Override
    protected boolean onRequestFocusInDescendants(int direction,
                                                  Rect previouslyFocusedRect) {

        // convert from forward / backward notation to up / down / left / right
        // (ugh).
        if (direction == View.FOCUS_FORWARD) {
            direction = View.FOCUS_DOWN;
        } else if (direction == View.FOCUS_BACKWARD) {
            direction = View.FOCUS_UP;
        }

        final View nextFocus = previouslyFocusedRect == null ?
                FocusFinder.getInstance().findNextFocus(this, null, direction) :
                FocusFinder.getInstance().findNextFocusFromRect(this,
                        previouslyFocusedRect, direction);

        if (nextFocus == null) {
            return false;
        }

        if (isOffScreen(nextFocus)) {
            return false;
        }

        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
                                                 boolean immediate) {
        // offset into coordinate space of this scroll view
        rectangle.offset(child.getLeft() - child.getScrollX(),
                child.getTop() - child.getScrollY());

        return scrollToChildRect(rectangle, immediate);
    }

    @Override
    public void requestLayout() {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        View currentFocused = findFocus();
        if (null == currentFocused || this == currentFocused)
            return;

        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        if (isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
            currentFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, mTempRect);
            int scrollDeltaX = computeScrollDeltaXToGetChildRectOnScreen(mTempRect);
            int scrollDeltaY = computeScrollDeltaYToGetChildRectOnScreen(mTempRect);
            if (orientation == VERTICAL){
                doScrollY(scrollDeltaY);
            }else {
                doScrollX(scrollDeltaX);
            }

        }
    }

    /**
     * Return true if child is a descendant of parent, (or equal to the parent).
     */
    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }

        final ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent);
    }

    /**
     * Fling the scroll view
     *
     * @param velocityY The initial velocity in the Y direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the top.
     */
    public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int bottom = abstractGetContentView().getHeight();

            int width = getWidth() - getPaddingRight() - getPaddingLeft();
            int right = abstractGetContentView().getWidth();

            if (orientation==VERTICAL){
                mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, Math.max(0, bottom - height), 0, height / 2);
                postInvalidateOnAnimation();
            }else {
                mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0, Math.max(0, right - width), 0, 0, width / 2, 0);
                postInvalidateOnAnimation();
            }

        }
    }

    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll();
    }

//    /**
//     * {@inheritDoc}
//     * <p>
//     * <p>This version also clamps the scrolling to the bounds of our child.
//     */
//    @Override
//    public void scrollTo(int x, int y) {
//        //we rely on the fact the View.scrollBy calls scrollTo.
//        if (getChildCount() > 0) {
//            View child = abstractGetContentView();
//            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
//            y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());
//            if (x != getScrollX() || y != getScrollY()) {
//                super.scrollTo(x, y);
//            }
//        }
//
//    }


    @Override
    public void scrollBy(int x, int y) {
        abstractScrollTo(getScrollX() + x, getScrollY() + y);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
    }

    @Override
    public void draw(Canvas canvas) {
        abstractDraw(canvas);
        super.draw(canvas);
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- getScrollX() --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- getScrollX() --|
             */
            return child - my;
        }
        return n;
    }


    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         *
         * @see android.view.Gravity
         *
         * @attr ref android.R.styleable#FrameLayout_Layout_layout_gravity
         */
        public int gravity = -1;

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            //TypedArray a = c.obtainStyledAttributes(attrs, com.android.internal.R.styleable.FrameLayout_Layout);
            //gravity = a.getInt(com.android.internal.R.styleable.FrameLayout_Layout_layout_gravity, -1);
            //a.recycle();
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * Creates a new set of layout parameters with the specified width, height
         * and weight.
         *
         * @param width the width, either {@link #MATCH_PARENT},
         *        {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height the height, either {@link #MATCH_PARENT},
         *        {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param gravity the gravity
         *
         * @see android.view.Gravity
         */
        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source The layout params to copy from.
         */
        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);

            this.gravity = source.gravity;
        }
    }
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FrameLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new FrameLayout.LayoutParams(p);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    protected int getPaddingLeftWithForeground() {
        return getPaddingLeft();
    }

    protected int getPaddingRightWithForeground() {
        return getPaddingRight();
    }

    protected int getPaddingTopWithForeground() {
        return getPaddingTop();
    }

    protected int getPaddingBottomWithForeground() {
        return getPaddingBottom();
    }

    public void setOrientation(int orientation){
        this.orientation=orientation;
    }


    //子View
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {

        resetParentHasNestedStateWhenChildReachBoard();
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {

        resetParentHasNestedStateWhenChildReachBoard();
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        //Log.i(TAG,"dispatchNestedScroll");
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {

        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return false;
    }


    //父View
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        notifiedScrollingAsParent=true;
        if (orientation==VERTICAL){
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        }else {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0;
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);

        if (orientation==VERTICAL){
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        }else {
            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL);
        }

    }

    @Override
    public void onStopNestedScroll(View target) {
        notifiedScrollingAsParent=false;
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        abstractOnNestedScroll(target,dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed);


    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        //用来处理在嵌套滑动中，若是父类，则不过度滑动
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

        int oldScrollX=getScrollX();
        int oldScrollY=getScrollY();
        abstractOverScrollBy(dx,dy,getScrollX(),getScrollY(),getScrollRangeX(),getScrollRangeY(),mOverscrollDistance,mOverflingDistance,true);

        int deltaX=getScrollX()-oldScrollX;
        int deltaY=getScrollY()-oldScrollY;

        consumed[0]=deltaX;
        consumed[1]=deltaY;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {

        //Logger.e("%s onNestedFling",TAG);
        if (!consumed) {

            int oldScrollX=getScrollX();
            int oldScrollY=getScrollY();
            flingWithNestedDispatch((int) velocityX,(int) velocityY);

            int deltaY=getScrollY()-oldScrollY;
            int deltaX=getScrollX()-oldScrollX;
            if (deltaY<0){
                parentTopHasNested=true;
            }else if (deltaY>0){
                parentBottomHasNested=true;
            }

            if (deltaX<0){
                parentLeftHasNested=true;
            }else if (deltaX>0){
                parentRightHasNested=true;
            }

            notifiedScrollingFlingAsParent=true;

            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        int oldScrollX=getScrollX();
        int oldScrollY=getScrollY();

        boolean b= flingWithNestedDispatch((int) velocityX,(int) velocityY);

        int deltaY=getScrollY()-oldScrollY;
        int deltaX=getScrollX()-oldScrollX;
        if (deltaY<0){
            parentTopHasNested=true;
        }else if (deltaY>0){
            parentBottomHasNested=true;
        }

        if (deltaX<0){
            parentLeftHasNested=true;
        }else if (deltaX>0){
            parentRightHasNested=true;
        }


        return b;

    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }


    //重置父View嵌套状态，该状态用来描述当子View到达顶部时，若父View嵌套滑动过，则标记true
    protected void resetParentHasNestedStateWhenChildReachBoard(){//为了这个把interceptedTouchEvent的stopNestedScroll注释掉了
        leftReachBoard=false;
        rightReachBoard=false;
        topReachBoard=false;
        bottomReachBoard=false;
    }

    protected abstract void abstractMeasureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec);

    protected abstract void abstractMeasureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,int parentHeightMeasureSpec, int heightUsed);

    protected abstract void abstractOnMeasure(int widthMeasureSpec, int heightMeasureSpec);

    protected abstract void abstractOnFinishInflate();

    protected abstract View abstractGetContentView();

    /**
     * View 的横向或竖向滚动（滑动），最终都会调用这个函数来实现
     * @param scrollX
     * @param scrollY
     */
    protected abstract void abstractScrollTo(int scrollX,int scrollY);

    /**
     * 用来调整scrollX,scrollY,deltaX,deltaY
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */

    protected abstract boolean abstractOverScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent);

    protected abstract void abstractOnOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY);


    protected abstract void abstractOnTouchDown(MotionEvent ev);

    protected abstract void abstractOnTouchMove(MotionEvent ev);

    protected abstract int abstractOnTouchMoveProcessDeltaY(int deltaY);

    protected abstract void abstractOnTouchUp();

    protected abstract void abstractOnTouchUpAndScroll(int vx,int vy);

    protected abstract void abstractOnTouchCancel();

    protected abstract void abstractDraw(Canvas canvas);

    protected abstract void abstractOnNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed);

    protected abstract void abstractOnNestedPreScroll(View target,int dx,int dy,int[]consumed);
}
