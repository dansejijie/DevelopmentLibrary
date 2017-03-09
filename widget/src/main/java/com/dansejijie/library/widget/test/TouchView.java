package com.dansejijie.library.widget.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tygzx on 17/3/9.
 */

public class TouchView extends View {

    private String info;

    private Paint paint=new Paint();

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        if (info!=null){
            float height=paint.measureText(info,0,1);
            paint.setColor(Color.RED);
            canvas.drawText(info,0,height,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int actionMask= MotionEventCompat.getActionMasked(event);
        switch (actionMask){
            case MotionEvent.ACTION_DOWN:
                info="DOWN";
                invalidate();
                return false;
            case MotionEvent.ACTION_MOVE:
                info="X:"+event.getX()+" Y:"+event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                info="UP";
                invalidate();
                return false;
        }
        return false;
    }
}
