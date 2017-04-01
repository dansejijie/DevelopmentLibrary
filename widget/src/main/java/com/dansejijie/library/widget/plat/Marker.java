package com.dansejijie.library.widget.plat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by tygzx on 17/2/10.
 */

public class Marker {


    protected static final String TAG=Marker.class.getSimpleName();

    protected int id=-1;

    /**
     * 座位在x,y格子上
     */
    protected int x=0;

    protected int y=0;

    /*默认一个物品宽高占1个格子*/
    private final static int DEFAULT_WIDTH_HEIGHT=1;

    /*当前seat的宽高,以格子数量为计量单位*/

    protected int width=DEFAULT_WIDTH_HEIGHT;

    protected int height=DEFAULT_WIDTH_HEIGHT;

    protected boolean checked=false;

    protected boolean clickable=true;

    protected Bitmap foregroundBitmap;

    protected int foregroundColor;

    protected Bitmap backgroundBitmap;

    protected int backgroundColor;

    protected String title;

    protected FrescoImageGetter mFrescoImageGetter;

    public Marker(final int x, final int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setChecked(boolean b){
        checked=b;
    }

    public boolean isChecked(){
        return checked;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public Bitmap getForegroundBitmap() {
        return foregroundBitmap;
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setForegroundBitmap(Bitmap foregroundBitmap) {
        this.foregroundBitmap = foregroundBitmap;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.backgroundBitmap = backgroundBitmap;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isSame(Marker marker){
        return this.x==marker.getX()&&this.y==marker.getY();
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String name) {
        title = name;
    }

    public void setBitmap(Uri uri, final Context context, FrescoImageGetter.CallBack callBack){
        mFrescoImageGetter=FrescoImageGetter.getInstance(context);
        mFrescoImageGetter.getNetBitmap(uri, callBack);
    }

    public void setXAndY(final int x,final int y){
        this.x=x;
        this.y=y;
    }

    public boolean isClickable() {
        return clickable;
    }
}
