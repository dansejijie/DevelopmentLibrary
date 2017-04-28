package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by tygzx on 17/4/28.
 */

public class CustomRelativeLayout extends RelativeLayout{


    ProgressBar progressBar;
    ImageView imageView;

    public CustomRelativeLayout(Context context) {

        super(context);

        init();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void init(){

        progressBar=new ProgressBar(getContext());
        progressBar.setMax(100);
        progressBar.setProgress(20);
        RelativeLayout.LayoutParams layoutParams=new LayoutParams(100,100);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(layoutParams);

        imageView=new ImageView(getContext());
        imageView.setBackgroundColor(Color.BLUE);
        RelativeLayout.LayoutParams layoutParams2=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams2);


    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


        addView(progressBar);

//        addView(imageView);
//
//        progressBar.bringToFront();
//
//
//
//        CountDownTimer countDownTimer=new CountDownTimer(10000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (Looper.myLooper()==Looper.getMainLooper()){
//                    Log.e("TAG","onTickUI线程");
//                }else {
//                    Log.e("TAG","onTick子线程");
//                }
//                progressBar.setProgress((int) (100*(millisUntilFinished*1.0f/10000)));
//            }
//
//            @Override
//            public void onFinish() {
//                if (Looper.myLooper()==Looper.getMainLooper()){
//                    Log.e("TAG","onFinishUI线程");
//                }else {
//                    Log.e("TAG","onFinish子线程");
//                }
//                removeView(progressBar);
//            }
//        };
//        countDownTimer.start();


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
    }
}
