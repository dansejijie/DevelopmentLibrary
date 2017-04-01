package com.dansejijie.library.widget.plat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tygzx on 17/2/9.
 * 对外提供三个方法
 * drawMarkerNewBitmap
 * drawMarkerText
 * handleMarkerTap
 */

public class plat extends View {


    public plat(Context context) {
        super(context);
        init();
    }

    public plat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public plat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {

        mSimpleGestureDetector = new SimpleGestureDetector(getContext(), mOnGestureListener);

        mZoomAnimation = new ZoomAnimation();

        mTransAnimation = new TranslationAnimation();

        mFlingAnimation = new FlingAnimation();

        mFrescoImageGetter = FrescoImageGetter.getInstance(getContext());
    }

    private static final String TAG = plat.class.getSimpleName();

    public final int DEFAULT_CELL_WIDTH_AND_HEIGHT = dip2Px(8);

    private int cellWidth = DEFAULT_CELL_WIDTH_AND_HEIGHT;

    private int cellHeight = DEFAULT_CELL_WIDTH_AND_HEIGHT;

    private int cellColumn = 0;

    private int cellRow = 0;

    private int allCellWidth = 0;

    private int allCellHeight = 0;

    private float drawdeltaX = 0;

    private float drawdeltaY = 0;

    private float drawScale = 1.0f;

    private float error = 2.0f;//图片和要求宽高的允许误差

    private Bitmap mBackgrounBitmap;

    private boolean isFirstDraw = true;

    public static boolean DEBUG = true;

    //上下左右边界
    private float boundaryLeft = 0;
    private float boundaryRight = 0;
    private float boundaryTop = 0;
    private float boundaryBottom = 0;

    //手势listener
    private SimpleGestureDetector mSimpleGestureDetector;

    private final float DEFAULT_VISUAL_WIDTH_HEIGHT = dip2Px(10);

    private float maxScale = 1.0f;

    private float minScale = 1.0f;

    private Animation curAnimation;

    private ZoomAnimation mZoomAnimation;

    private TranslationAnimation mTransAnimation;

    private FlingAnimation mFlingAnimation;

    /**
     * 需要画的座位集合
     *
     * @param context
     */
    protected List<Marker> markers = new ArrayList<>();

    protected int maxSelected = Integer.MAX_VALUE;

    protected List<Marker> selectedMarkers = new ArrayList<>();


    /**
     * 在canvas.drawBitmap里若是不加paint,會出現圖片画到控件外面的情况
     *
     * @param canvas
     */
    Paint paint = new Paint();

    Paint forePaint = new Paint();

    Paint backPaint = new Paint();

    /**
     * 依次要画的前景色，背景色，目标位置
     **/
    Rect bitmapRect = new Rect();

    RectF destRect = new RectF();

    protected FrescoImageGetter mFrescoImageGetter;

    //缩放中心点
    private int zoomLevel = 1;//1:最适应控件，0:正常大小,2:原图的两倍

    private boolean enable = true;//屏蔽点击手势

    private boolean scrollEnable=true;//屏蔽伸缩，移动手势



    @Override
    protected void onDraw(Canvas canvas) {

        if (isFirstDraw) {
            /*计算初始座位图居中位置和缩放大小*/
            /**
             * 设置最大最小缩放为控件的一半和4倍大小
             */
            minScale = (getViewWidth() * 1.0f / 2) / allCellWidth;
            maxScale = (getViewWidth() * 1.0f * 4) / allCellWidth;
            float sx = allCellWidth * 1.0f / getViewWidth();
            float sy = allCellHeight * 1.0f / getViewHeight();
            drawScale = Math.max(sx, sy);
            drawdeltaX = (getViewWidth() - allCellWidth / drawScale) / 2;
            drawdeltaY = (getViewHeight() - allCellHeight / drawScale) / 2;

            boundaryLeft = 0;
            boundaryTop = 0;
            boundaryRight = boundaryLeft + getViewWidth();
            boundaryBottom = boundaryTop + getViewHeight();


            drawScale = 1.0f / drawScale;
            isFirstDraw = false;
        }

        canvas.save();
        canvas.translate(drawdeltaX, drawdeltaY);
        canvas.scale(drawScale, drawScale);
        drawBackground(canvas);
        drawMarker(canvas);
        canvas.restore();

    }

    private void drawBackground(Canvas canvas) {

        if (allCellWidth > 0 && allCellHeight > 0 && mBackgrounBitmap != null) {

            if (Math.abs(mBackgrounBitmap.getWidth() - allCellWidth) > error || Math.abs(mBackgrounBitmap.getHeight() - allCellHeight) > error) {
                setBackgroundBitmap(mBackgrounBitmap);
            }
            canvas.drawBitmap(mBackgrounBitmap, 0, 0, paint);
        } else if (allCellWidth > 0 && allCellHeight > 0) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Rect rect = new Rect();
            rect.set(0, 0, allCellWidth, allCellHeight);
            canvas.drawRect(rect, paint);
        }
    }

    private void drawMarker(Canvas canvas) {

        if (allCellWidth > 0 && allCellHeight > 0 && markers != null && markers.size() > 0) {

            for (Marker marker : markers) {

                float left = marker.getX() * cellWidth;
                float top = marker.getY() * cellHeight;

                float right = left + getMarketWidth(marker);
                float bottom = top + getMarketHeight(marker);

                destRect.set(left, top, right, bottom);
                /*画背景色*/
                Bitmap background = marker.getBackgroundBitmap();

                if (background != null) {
                    bitmapRect.set(0, 0, background.getWidth(), background.getHeight());
                    canvas.drawBitmap(background, bitmapRect, destRect, paint);
                } else if (marker.getBackgroundColor() != 0) {
                    backPaint.setColor(marker.getBackgroundColor());
                    canvas.drawRect(left, top, right, bottom, backPaint);
                }

                Bitmap foreground = marker.getForegroundBitmap();
                if (foreground != null) {
                    bitmapRect.set(0, 0, foreground.getWidth(), foreground.getHeight());
                    canvas.drawBitmap(foreground, bitmapRect, destRect, paint);
                } else if (marker.getForegroundColor() != 0) {
                    forePaint.setColor(marker.getForegroundColor());
                    canvas.drawRect(destRect, forePaint);
                }

                if (drawMarkerNewBitmap(canvas, destRect, marker)) {
                    //TODO
                }

                drawText(canvas, destRect, marker);

            }
        }
    }


    /**
     * 重写画Marker的话，则返回true
     *
     * @param canvas 已经经过转换的canvas
     * @param marker 需要画的marker
     * @return
     */
    protected boolean drawMarkerNewBitmap(Canvas canvas, RectF destRect, Marker marker) {

        return false;
    }

    private void drawText(Canvas canvas, RectF destRect, Marker marker) {

        if (!TextUtils.isEmpty(marker.getTitle())) {

            String title = marker.getTitle();
            int length = title.length();

            String txt = null;
//            String txt1 = null;
//            if (length >= 2) {
//                txt = title.substring(0, length / 2 + 1);
//                txt1 = title.substring(length / 2 + 1, title.length());
//            } else {
//                txt = title;
//            }
            txt=title;


            float left = destRect.left;
            float top = destRect.top;

            TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            txtPaint.setColor(Color.BLACK);
            txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
            txtPaint.setTextSize(getMarketHeight(marker) / 3);

            if (getMarketHeight(marker) / 3 * drawScale < DEFAULT_VISUAL_WIDTH_HEIGHT) {
                return;
            }

            //获取中间线
            float center = getMarketWidth(marker) / 2;
            float txtWidth = txtPaint.measureText(txt);
            float startX = left + getMarketWidth(marker) / 2 - txtWidth / 2;

            //只绘制一行文字
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + getMarketHeight(marker)), txtPaint);
//            if (txt1 == null) {
//                canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + getMarketHeight(marker)), txtPaint);
//            } else {
//                canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + center), txtPaint);
//                canvas.drawText(txt1, startX, getBaseLine(txtPaint, top + center, top + center + getMarketHeight(marker) / 2), txtPaint);
//            }
        }


        if (drawMarkerText(canvas, destRect, marker)) {
            //TODO
        }

    }

    /**
     * 画Marker的特殊文字，画了则返回true
     *
     * @param canvas   已经经过转换的canvas
     * @param destRect 画的位置
     * @param marker   需要画的marker
     * @return
     */
    protected boolean drawMarkerText(Canvas canvas, RectF destRect, Marker marker) {

        return false;
    }

    protected float getMaxScale() {
        return maxScale;
    }

    protected float getMinScale() {
        return minScale;
    }

    protected float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseline;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (scrollEnable){
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return mSimpleGestureDetector.onTouchEvent(event);

    }

    private SimpleGestureDetector.OnGestureListener mOnGestureListener = new SimpleGestureDetector.OnGestureListener() {

        @Override
        public void onDoubleTap(float x, float y) {

            if (scrollEnable){

                float zoom=1.8f;
                if (drawScale==maxScale){//当达到最大值的时候，缩小到初始化值
                    isFirstDraw=true;
                    postInvalidateOnAnimation();
                }else {
                    if (drawScale*zoom<=maxScale){
                        zoom=zoom*drawScale;
                    }else if (drawScale*zoom>maxScale){
                        zoom=maxScale;
                    }
                }
                animationStop();
                mZoomAnimation.start(x, y, zoom);
            }
        }

        @Override
        public void onFling(float vx, float vy) {

            if (scrollEnable){
                animationStop();
                mFlingAnimation.start(vx, vy);
            }

        }

        @Override
        public void onActionUp() {
        }

        @Override
        public void onActionDown() {
            animationStop();
        }

        @Override
        public void onZoom(float midX, float midY, float rScale) {
            if (scrollEnable){
                handleScale(midX, midY, rScale);
            }

        }

        @Override
        public void onDrag(float dx, float dy) {
            if (scrollEnable){
                handleDrag(dx, dy);
            }
        }

        @Override
        public void onSingleTap(float x, float y) {
            if (enable){
                handleSingleTap(x, y);
            }
        }

        @Override
        public void onSingleUp(float x, float y) {
            animationStop();
            mTransAnimation.start(x, y);
        }
    };

    //计算内容是否到达边界

    private boolean calcReachBound() {

        float left = getContentLeft();
        float top = getContentTop();
        float right = getContentRight();
        float bottom = getContentBottom();

        if (left > 0 || right - getViewWidth() < 0 || top > 0 || bottom - getViewHeight() < 0) {
            return true;
        }
        return false;
    }

    //计算x,y越过边界的数量
    private float[] calcXYBound(float x, float y) {

        float[] f = new float[2];

        float left = getContentLeft();
        float top = getContentTop();
        float right = getContentRight();
        float bottom = getContentBottom();

        float dx = 0;
        float dy = 0;
        //处理左移
        if (left > 0 && right - getViewWidth() > 0) {
            dx = Math.min(left, right - getViewWidth());
        }
        //处理右移
        if (left < 0 && right - getViewWidth() < 0) {
            dx = Math.max(left, right - getViewWidth());
        }

        //处理上移

        if (top > 0 && bottom - getViewHeight() > 0) {
            dy = Math.min(top, bottom - getViewHeight());
        }
        //处理下移
        if (top < 0 && bottom - getViewHeight() < 0) {
            dy = Math.max(top, bottom - getViewHeight());
        }
        /*当content的width或height小于控件对应值，该方向轴居中*/

        if (getContentWidth() < getViewWidth()) {
            dx = -((getViewWidth() - getContentWidth()) / 2 - getContentLeft());
        }
        if (getContentHeight() < getViewWidth()) {
            dy = -((getViewHeight() - getContentHeight()) / 2 - getContentTop());
        }

        f[0] = dx;
        f[1] = dy;
        return f;
    }


    /**
     * 处理单击选座位事件
     *
     * @param x
     * @param y
     */
    protected void handleSingleTap(float x, float y) {
        for (Marker marker : markers) {
            if (isDown(x, y, marker)) {
                handleMarkerTap(marker);
                invalidate();
                break;
            }
        }
    }


    public void setMaxSelected(int max) {
        maxSelected = max;
    }

    public void setZoomLevel(int level) {
        zoomLevel = level;
        invalidate();
    }


    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Marker> getSelectedMarkers() {
        return selectedMarkers;
    }

    /**
     * 当点击该marker时候，可以对marker处理状态
     *
     * @param marker
     */

    protected void handleMarkerTap(Marker marker) {
        Log.i(TAG, "点击了marker");
    }

    /**
     * 处理拖动
     *
     * @return 该拖动是否处理
     */
    private boolean handleDrag(float dx, float dy) {
        drawdeltaX += dx;
        drawdeltaY += dy;
        invalidate();
        return true;
    }

    private boolean handleFling(float dx, float dy) {

        if (calcReachBound()) {
            return false;
        }
        drawdeltaX += dx;
        drawdeltaY += dy;
        invalidate();
        return true;
    }

    /**
     * 处理回弹
     *
     * @param dx
     * @param dy
     * @return
     */

    private boolean handleRebound(float dx, float dy) {
        drawdeltaX += dx;
        drawdeltaY += dy;
        invalidate();
        return true;
    }

    /**
     * 处理缩放	普通缩放或缩放动画
     *
     * @param midX   midY 缩放中点坐标
     * @param rScale 相对当前缩放比例
     * @return 缩放是否被处理
     */
    private boolean handleScale(float midX, float midY, float rScale) {
        //Log.d(TAG, "handleScale");
        //获得绝对缩放比例
        float newScale = drawScale * rScale;
        //规范化缩放比例
        if (newScale >= getMaxScale()) {
            newScale = getMaxScale();
            rScale = newScale / drawScale;
        } else if (newScale <= getMinScale()) {
            newScale = getMinScale();
            rScale = newScale / drawScale;
        }
        if (newScale == drawScale) {
            return false;
        }
        drawScale = newScale;
        //计算缩放后边界
        //calcBoundaries();
        //计算缩放后新坐标
        drawdeltaX = calcScaledCoordinate(midX, drawdeltaX, rScale);
        drawdeltaY = calcScaledCoordinate(midY, drawdeltaY, rScale);

        invalidate();
        return true;
    }


    /**
     * 获得双击时相对于当前的缩放比例
     */
    private float getMaxMinZoom() {

        if (getMaxScale() <= getMinScale()) {
            return 1f;
        }
        if (drawScale < (getMaxScale() - getMinScale()) / 2f) {
            return getMaxScale() / drawScale;
        } else {
            return getMinScale() / drawScale;
        }
    }

    /**
     * 规范x坐标
     */
    private float boundX(float x) {
        if (x < boundaryLeft) {
            x = boundaryLeft;
        } else if (x > boundaryRight) {
            x = boundaryRight;
        }
        return x;
    }

    /**
     * 规范y坐标
     */
    private float boundY(float y) {
        if (y < boundaryTop) {
            y = boundaryTop;
        } else if (y > boundaryBottom) {
            y = boundaryBottom;
        }
        return y;
    }

    /**
     * 计算相对于某点缩放后的xy
     *
     * @param mid    缩放中点坐标
     * @param cur    当前坐标
     * @param rScale 相对当前缩放比例
     */
    private float calcScaledCoordinate(float mid, float cur, float rScale) {
        return (cur - mid) * rScale + mid;
    }

    public void setRowAndColumn(int row, int column) {//这里注意,Row越大，行越高
        if (row != 0 && column != 0) {
            cellColumn = column;
            cellRow = row;
            allCellWidth = cellColumn * cellWidth;
            allCellHeight = cellRow * cellHeight;
            isFirstDraw = true;
            invalidate();
        }
    }

    private abstract class Animation implements Runnable {

        protected final void postOnAnimation() {
            ViewCompat.postOnAnimation(plat.this, this);
        }

        protected void animationStart() {
            curAnimation = this;
        }

        protected void animationFinish() {
            curAnimation = null;
        }

        public void stop() {
            removeCallbacks(this);
            animationFinish();
        }

        public abstract boolean isFinished();
    }

    private abstract class AnimationImpl extends Animation {

        private long mStartTime;
        private int mDuration;
        private float rDuration;
        private boolean isFinished;

        @Override
        public final void run() {
            if (isFinished) {
                return;
            }
            int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
            if (timePassed < mDuration) {
                if (onUpdate(timePassed * rDuration)) {
                    postOnAnimation();
                }
            } else {
                onUpdate(1);
                isFinished = true;
                animationFinish();
            }
        }

        protected abstract boolean onUpdate(float fraction);

        protected void start(int duration) {
            isFinished = false;
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mDuration = duration;
            rDuration = 1f / duration;
            postOnAnimation();
            animationStart();
        }

        public void stop() {
            super.stop();
            isFinished = true;
        }

        @Override
        public final boolean isFinished() {
            return isFinished;
        }
    }

    private class FlingAnimation extends Animation {

        private Scroller mScroller = new Scroller(getContext());
        private int lastFlingX;
        private int lastFlingY;

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();
                int dx = x - lastFlingX;
                int dy = y - lastFlingY;
                lastFlingX = x;
                lastFlingY = y;
                if (!handleFling(dx, dy)) {
                    stop();
                    mTransAnimation.start(0, 0);
                }
            }
            if (!mScroller.isFinished()) {
                postOnAnimation();
            } else {
                animationFinish();
                mTransAnimation.start(0, 0);
            }
        }

        public void start(float vx, float vy) {
            lastFlingX = lastFlingY = 0;
            mScroller.fling(0, 0, (int) vx, (int) vy, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
            animationStart();
        }

        public void stop() {
            super.stop();
            mScroller.abortAnimation();
        }

        @Override
        public boolean isFinished() {
            return mScroller.isFinished();
        }
    }

    private class ZoomAnimation extends AnimationImpl {

        private float centerX;
        private float centerY;
        private float lastZoom;
        //目标缩放比例
        private float finalZoom;

        @Override
        protected boolean onUpdate(float fraction) {
            float zoom;
            if (finalZoom >= 1) {
                zoom = finalZoom * fraction + 1;
            } else {
                zoom = (finalZoom - 1) * fraction + 1;
            }
            float rZoom = zoom / lastZoom;
            lastZoom = zoom;

            if (rZoom * drawScale >= getMaxScale() || rZoom * drawScale <= getMinScale()) {
                stop();
            }
            if (fraction == 1) {
                handleScale(centerX, centerY, rZoom);
                //设置最终缩放
                setMaxOrMin(finalZoom > 1, centerX, centerY);
            } else {


                if (handleScale(centerX, centerY, rZoom)) {
                    return true;
                } else {
                    stop();
                }
            }
            return false;
        }

        /**
         * @param centerX   缩放中心x
         * @param centerY   缩放中心y
         * @param finalZoom 目标相对于当前的缩放比例
         */
        public void start(float centerX, float centerY, float finalZoom) {
            this.centerX = centerX;
            this.centerY = centerY;
            lastZoom = 1;
            this.finalZoom = finalZoom;
            super.start(500);
        }
    }

    private class TranslationAnimation extends AnimationImpl {

        //初始移动位置
        float src_x = 0.0f;
        float src_y = 0.0f;

        //要移动的距离
        float dest_dx = 0.0f;
        float dext_dy = 0.0f;

        //上次移动的距离
        float last_dx = 0.0f;
        float last_dy = 0.0f;


        @Override
        protected boolean onUpdate(float fraction) {
            float dx = fraction * dest_dx - last_dx;
            float dy = fraction * dext_dy - last_dy;
            last_dx = fraction * dest_dx;
            last_dy = fraction * dext_dy;
            handleRebound(dx, dy);
            if (fraction == 1) {
                stop();
                return false;
            }
            return true;
        }

        public void start(float x, float y) {

            float[] f = calcXYBound(x, y);
            if (f[0] == 0 && f[1] == 0) {
                return;
            }
            src_x = x;
            src_y = y;
            dest_dx = -f[0];
            dext_dy = -f[1];

            last_dx = 0;
            last_dy = 0;
            super.start(500);
        }
    }

    /**
     * 将图片放到最大或最小
     */
    private void setMaxOrMin(boolean max, float centerX, float centerY) {
        float newScale;
        if (max) {
            newScale = getMaxScale();
        } else {
            newScale = getMinScale();
        }
        if (newScale == drawScale) {
            return;
        }
        float rScale = newScale / drawScale;
        drawScale = newScale;
        //计算缩放后边界
        //calcBoundaries();
        //计算缩放后新坐标
        drawdeltaX = calcScaledCoordinate(centerX, drawdeltaX, rScale);
        drawdeltaY = calcScaledCoordinate(centerY, drawdeltaY, rScale);
        invalidate();
    }

    private void animationStop() {
        if (curAnimation != null) {
            curAnimation.stop();
            curAnimation = null;
        }
    }


    private void addChooseSeat(Marker marker) {
        if (!isHaveSelected(marker)) {
            markerSelected.add(marker);
        }
    }

    /**
     * 判断是否点击到座位上
     *
     * @param x      横坐标，像素点
     * @param y      纵坐标，像素点
     * @param marker
     * @return
     */
    private boolean isDown(float x, float y, Marker marker) {

        if (!marker.isClickable()) {
            return false;
        }

        float seat_width = marker.getWidth() * cellWidth * drawScale;
        float seat_height = marker.getHeight() * cellHeight * drawScale;

        float left = drawdeltaX + marker.getX() * cellWidth * drawScale;
        float top = drawdeltaY + marker.getY() * cellHeight * drawScale;
        float right = (left + seat_width);
        float bottom = (top + seat_height);

        return (x > left && x < right && y > top && y < bottom);
    }

    protected ArrayList<Marker> markerSelected = new ArrayList<>();

    protected boolean isHaveSelected(Marker marker) {
        for (Marker s : markerSelected) {
            if (s.isSame(marker)) {
                return true;
            }
        }
        return false;
    }

    protected void remove(Marker marker) {
        for (Marker s : markerSelected) {
            if (s.isSame(marker)) {
                markerSelected.remove(s);
                break;
            }
        }
    }

    public void setMarkers(List<Marker> markers) {
        this.markers.addAll(markers);

    }

    protected int getViewWidth() {

        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    protected int getViewHeight() {

        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    protected int dip2Px(float value) {
        return (int) (getResources().getDisplayMetrics().density * value);
    }

    /**
     * 返回整个画图内容的left,top,right,bottom
     *
     * @return
     */

    private float getContentLeft() {
        return drawdeltaX;
    }

    private float getContentTop() {
        return drawdeltaY;
    }

    private float getContentRight() {
        return drawdeltaX + cellWidth * cellColumn * drawScale;
    }

    private float getContentBottom() {
        return drawdeltaY + cellHeight * cellRow * drawScale;
    }

    private float getContentWidth() {
        return cellWidth * cellColumn * drawScale;
    }

    private float getContentHeight() {
        return cellHeight * cellRow * drawScale;
    }

    public void setBackgroundBitmap(Bitmap back) {

        if (allCellWidth == 0 || allCellHeight == 0) {
            mBackgrounBitmap = back;
        } else {

            Matrix temp = new Matrix();
            float radioWidth = allCellWidth * 1.0f / back.getWidth();
            float radioHeight = allCellHeight * 1.0f / back.getHeight();
            temp.postScale(radioWidth, radioHeight);
            mBackgrounBitmap = Bitmap.createBitmap(back, 0, 0, back.getWidth(), back.getHeight(), temp, false);
        }
        invalidate();
    }


    public void setBackgroundBitmap(Uri uri) {
        mFrescoImageGetter.getNetBitmap(uri, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri, Bitmap bitmap) {
                if (uri == uri) {
                    setBackgroundBitmap(bitmap);
                }
            }

            @Override
            public void failed(Uri uri) {
                Toast.makeText(getContext(), "背景图片加载", Toast.LENGTH_SHORT);
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*当空间大小改变的时候，重新布格局，重新补格局是在OnDraw里的*/
        isFirstDraw = true;
    }

    protected float getMarketWidth(Marker marker) {
        return marker.width * cellWidth;
    }

    protected float getMarketHeight(Marker marker) {
        return marker.getHeight() * cellHeight;
    }
}
