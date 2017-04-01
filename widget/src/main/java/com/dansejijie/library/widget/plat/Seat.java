package com.dansejijie.library.widget.plat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by tygzx on 17/2/10.
 */

public class Seat extends Marker {


    public Seat(final int x,final int y) {
        this(x,y,0);

    }

    public Seat(final int x,final int y,final int status){
        this(x,y,status,0,null);
    }

    public Seat(final int x,final int y,final int status,final int id,final String title){
        super(x,y);
        mStatus=status;
        this.id=id;
        this.title=title;
        initStatus();
    }

    public void initStatus(){
        this.width=2;
        this.height=2;
        clickable=true;
    }


    public void setWidthAndHeight(int width,int height){
        this.width=width;
        this.height=height;
    }

    public static final int STATUS_UNSELECTED=0;

    public static final int STATUS_UNSELECTED_COMPUTER=1;

    public static final int STATUS_SELF_SELECTED=2;

    public static final int STATUS_SELF_COMPUTER_SELECTED=3;

    public static final int STATUS_OTHER_SELECTED=4;

    public static final int STATUS_OTHER_COMPUTER_SELECTED=5;

    public static final int STATUS_CLOSE=6;

    public static final int STATUS_CLOSE_COMPUTER=7;

    public static final int STATUS_KEEP=8;

    public static final int STATUS_KEEP_COMPUTER=9;

    private int mStatus=STATUS_UNSELECTED;

    private boolean isChecked;

    private Bitmap mSeatUnSelectedBitmap;

    private Bitmap mSeatOtherSelectedBitmap;

    private Bitmap mSeatSelfSelectedBitmap;

    public void setStatus(int mSTATUS) {
        this.mStatus = mSTATUS;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getStatus() {

        return mStatus;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    public void setSeatUnSelectedBitmap(final Uri uri1, Context context) {
        setBitmap(uri1, context, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatUnSelectedBitmap=bitmap;
                }
            }

            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatSelfSelectedBitmap(final Uri uri1, Context context) {
        setBitmap(uri1, context, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatSelfSelectedBitmap=bitmap;
                }
            }

            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public void setSeatOtherSelectedBitmap(final Uri uri1, Context context) {
        setBitmap(uri1, context, new FrescoImageGetter.CallBack() {
            @Override
            public void success(Uri uri2, Bitmap bitmap) {
                if (uri1==uri2){
                    mSeatOtherSelectedBitmap=bitmap;
                }
            }

            @Override
            public void failed(Uri uri) {

            }
        });
    }

    public Bitmap getSeatUnSelectedBitmap() {
        return mSeatUnSelectedBitmap;
    }

    public Bitmap getSeatOtherSelectedBitmap() {
        return mSeatOtherSelectedBitmap;
    }

    public Bitmap getSeatSelfSelectedBitmap() {
        return mSeatSelfSelectedBitmap;
    }
}
