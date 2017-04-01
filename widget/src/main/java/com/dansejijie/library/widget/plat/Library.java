package com.dansejijie.library.widget.plat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.Toast;

import com.dansejijie.library.widget.R;

import java.util.List;

/**
 * Created by tygzx on 17/2/10.
 */

public class Library extends plat {


    private Bitmap mSeatUnSelectedComputerBitmap;

    private Bitmap mSeatOtherSelectedComputerBitmap;

    private Bitmap mSeatSelfSelectedComputerBitmap;

    private Bitmap mSeatUnSelectedBitmap;

    private Bitmap mSeatOtherSelectedBitmap;

    private Bitmap mSeatSelfSelectedBitmap;

    private Bitmap mSeatCloseBitmap;

    private Bitmap mSeatCloseComputerBitmap;


    public Library(Context context) {
        super(context);
    }

    public Library(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Library(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        mSeatCloseBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_seat_close);
        mSeatCloseComputerBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_close_computer);

        mSeatOtherSelectedBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_lock);
        mSeatOtherSelectedComputerBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_lock_computer);

        mSeatSelfSelectedBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_full);
        mSeatSelfSelectedComputerBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_full_computer);

        mSeatUnSelectedBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_free);
        mSeatUnSelectedComputerBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_seat_free_computer);
    }

    @Override
    protected boolean drawMarkerNewBitmap(Canvas canvas, RectF destRect, Marker marker) {

        if (marker instanceof Seat){
            Seat seat= (Seat) marker;
            switch (seat.getStatus()){
                case Seat.STATUS_UNSELECTED:
                    if (mSeatUnSelectedBitmap!=null){
                        bitmapRect.set(0,0,mSeatUnSelectedBitmap.getWidth(),mSeatUnSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatUnSelectedBitmap,bitmapRect,destRect,paint);
                    }
                    break;

                case Seat.STATUS_UNSELECTED_COMPUTER:
                    if (mSeatUnSelectedBitmap!=null&&mSeatUnSelectedComputerBitmap!=null){
                        bitmapRect.set(0,0,mSeatUnSelectedBitmap.getWidth(),mSeatUnSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatUnSelectedBitmap,bitmapRect,destRect,paint);
                        canvas.drawBitmap(mSeatUnSelectedComputerBitmap,bitmapRect,destRect,paint);
                    }
                    break;

                case Seat.STATUS_SELF_SELECTED:

                    if (mSeatSelfSelectedBitmap!=null){
                        bitmapRect.set(0,0,mSeatSelfSelectedBitmap.getWidth(),mSeatSelfSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatSelfSelectedBitmap,bitmapRect,destRect,paint);
                    }
                    break;
                case Seat.STATUS_SELF_COMPUTER_SELECTED:

                    if (mSeatSelfSelectedBitmap!=null&&mSeatSelfSelectedComputerBitmap!=null){
                        bitmapRect.set(0,0,mSeatSelfSelectedBitmap.getWidth(),mSeatSelfSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatSelfSelectedBitmap,bitmapRect,destRect,paint);
                        canvas.drawBitmap(mSeatSelfSelectedComputerBitmap,bitmapRect,destRect,paint);
                    }
                    break;

                case Seat.STATUS_OTHER_SELECTED:

                    if (mSeatOtherSelectedBitmap!=null){
                        bitmapRect.set(0,0,mSeatOtherSelectedBitmap.getWidth(),mSeatOtherSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatOtherSelectedBitmap,bitmapRect,destRect,paint);
                    }

                    break;
                case Seat.STATUS_OTHER_COMPUTER_SELECTED:

                    if (mSeatOtherSelectedBitmap!=null&&mSeatOtherSelectedComputerBitmap!=null){
                        bitmapRect.set(0,0,mSeatOtherSelectedBitmap.getWidth(),mSeatOtherSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatOtherSelectedBitmap,bitmapRect,destRect,paint);
                        canvas.drawBitmap(mSeatOtherSelectedComputerBitmap,bitmapRect,destRect,paint);
                    }

                    break;
                case Seat.STATUS_CLOSE:

                    if (mSeatCloseBitmap!=null){
                        bitmapRect.set(0,0,mSeatCloseBitmap.getWidth(),mSeatCloseBitmap.getHeight());
                        canvas.drawBitmap(mSeatCloseBitmap,bitmapRect,destRect,paint);
                    }
                    break;
                case Seat.STATUS_CLOSE_COMPUTER:

                    if (mSeatCloseBitmap!=null&&mSeatCloseComputerBitmap!=null){
                        bitmapRect.set(0,0,mSeatCloseBitmap.getWidth(),mSeatCloseBitmap.getHeight());
                        canvas.drawBitmap(mSeatCloseBitmap,bitmapRect,destRect,paint);
                        canvas.drawBitmap(mSeatCloseComputerBitmap,bitmapRect,destRect,paint);
                    }
                    break;
                case Seat.STATUS_KEEP:

                    if (mSeatSelfSelectedBitmap!=null){
                        bitmapRect.set(0,0,mSeatSelfSelectedBitmap.getWidth(),mSeatSelfSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatSelfSelectedBitmap,bitmapRect,destRect,paint);
                    }
                    break;
                case Seat.STATUS_KEEP_COMPUTER:

                    if (mSeatSelfSelectedBitmap!=null&&mSeatSelfSelectedComputerBitmap!=null){
                        bitmapRect.set(0,0,mSeatSelfSelectedBitmap.getWidth(),mSeatSelfSelectedBitmap.getHeight());
                        canvas.drawBitmap(mSeatSelfSelectedBitmap,bitmapRect,destRect,paint);
                        canvas.drawBitmap(mSeatSelfSelectedComputerBitmap,bitmapRect,destRect,paint);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected boolean drawMarkerText(Canvas canvas, RectF destRect, Marker marker) {

        return false;
    }

    @Override
    protected void handleMarkerTap(Marker marker) {

        if (marker instanceof Seat){
            Seat seat= (Seat) marker;
            switch (seat.getStatus()){
                case Seat.STATUS_UNSELECTED:
                    if (selectedMarkers.size()>=maxSelected){
                        Toast.makeText(getContext(),"超过最大预约座位数",Toast.LENGTH_SHORT);
                        return;
                    }
                    seat.setStatus(Seat.STATUS_SELF_SELECTED);
                    selectedMarkers.add(marker);
                    if (seatlistener!=null){
                        seatlistener.onSeatSelected(selectedMarkers);
                    }
                    break;
                case Seat.STATUS_UNSELECTED_COMPUTER:
                    if (selectedMarkers.size()>=maxSelected){
                        Toast.makeText(getContext(),"超过最大预约座位数",Toast.LENGTH_SHORT);
                        return;
                    }
                    seat.setStatus(Seat.STATUS_SELF_COMPUTER_SELECTED);
                    selectedMarkers.add(marker);
                    if (seatlistener!=null){
                        seatlistener.onSeatSelected(selectedMarkers);
                    }
                    break;
                case Seat.STATUS_SELF_SELECTED:
                    seat.setStatus(Seat.STATUS_UNSELECTED);
                    selectedMarkers.remove(marker);
                    if (seatlistener!=null){
                        seatlistener.onSeatSelected(selectedMarkers);
                    }
                    break;
                case Seat.STATUS_SELF_COMPUTER_SELECTED:
                    seat.setStatus(Seat.STATUS_UNSELECTED_COMPUTER);
                    selectedMarkers.remove(marker);
                    if (seatlistener!=null){
                        seatlistener.onSeatSelected(selectedMarkers);
                    }
                    break;
            }
        }

    }

    protected boolean hasSelected(Marker marker){
        for (Marker marker1:selectedMarkers){
            if (marker1.getX()==marker.getX()&&marker1.getY()==marker.getY()){
                return true;
            }
        }
        return false;
    }

    public void setSeatUnSelectedBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatUnSelectedBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatSelfSelectedBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatSelfSelectedBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatOtherSelectedBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatOtherSelectedBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatUnSelectedComputerBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatUnSelectedComputerBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatSelfSelectedComputerBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatSelfSelectedComputerBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatOtherSelectedComputerBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatOtherSelectedComputerBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatCloseBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatCloseBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatCloseComputerBitmap(final Uri uri1) {
        mFrescoImageGetter.getNetBitmap (uri1, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatCloseComputerBitmap=bitmap;
                    invalidate();
                }
            }
            @Override
            public void failed(Uri uri) {

            }
        });
    }

    @Override
    public void setMarkers(List<Marker> markers) {
        super.setMarkers(markers);
        selectedMarkers.clear();
        for (Marker marker:markers){
            if (marker instanceof Seat){
                Seat seat= (Seat) marker;
                if (seat.getStatus()==Seat.STATUS_SELF_SELECTED||seat.getStatus()==Seat.STATUS_SELF_COMPUTER_SELECTED){
                    selectedMarkers.add(marker);
                }
            }
        }
    }


    private onSeatSelectedListener seatlistener;

    public void setOnSeatSelectedListener(onSeatSelectedListener listener){
        this.seatlistener=listener;
    }

    public interface onSeatSelectedListener{
        void onSeatSelected(List<Marker> list);
    }


}
