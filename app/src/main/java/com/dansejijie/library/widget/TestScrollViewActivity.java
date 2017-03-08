package com.dansejijie.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dansejijie.library.widget.scrollview.AbstractScrollView;
import com.dansejijie.library.widget.scrollview.CustomScrollView;
import com.dansejijie.library.R;
import com.dansejijie.library.widget.scrollview.PtrIndicator;
import com.dansejijie.library.widget.scrollview.PtrUIHandler;
import com.dansejijie.library.widget.scrollview.TCustomScrollView;

import junit.framework.Test;

/**
 * Created by tygzx on 17/2/28.
 */

public class TestScrollViewActivity extends Activity {

    private static final String TAG=TestScrollViewActivity.class.getSimpleName();

    TCustomScrollView customScrollView;
    LinearLayout linearLayout;
    Button verticalBtn,horizontalBtn;

    public static void start(Context context){
        Intent intent=new Intent(context,TestScrollViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scroll_view);
//        customScrollView= (TCustomScrollView) findViewById(R.id.test_scroll_view_view);
//        customScrollView.TAG="TCustomScrollView";
//        linearLayout= (LinearLayout) findViewById(R.id.test_scroll_view_container);
//        verticalBtn= (Button) findViewById(R.id.test_scroll_view_vertical);
//        horizontalBtn= (Button) findViewById(R.id.test_scroll_view_horizontal);
//        verticalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customScrollView.setOrientation(CustomScrollView.VERTICAL);
//            }
//        });
//        horizontalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customScrollView.setOrientation(CustomScrollView.HORIZONTAL);
//            }
//        });
//
//        ImageView imageView=new ImageView(TestScrollViewActivity.this);
//        AbstractScrollView.MarginLayoutParams lp=new AbstractScrollView.MarginLayoutParams(400,300);
//        lp.setMargins(30,0,30,0);
//        imageView.setLayoutParams(lp);
//        imageView.setBackgroundColor(Color.RED);
//        customScrollView.setOrientation(CustomScrollView.VERTICAL);
//        customScrollView.addHeaderView(imageView);
//        customScrollView.setPullToRefresh(true);
//        customScrollView.setPullToFooterRefresh(true);
//        customScrollView.addPtrUIHandler(new PtrUIHandler() {
//            @Override
//            public void onUIReset() {
//                //Log.i(TAG," onUIReset");
//            }
//
//            @Override
//            public void onUIRefreshPrepare() {
//                //Log.i(TAG," onUIRefreshPrepare");
//            }
//
//            @Override
//            public void onUIReleaseToRefresh() {
//                //Log.i(TAG," onUIReleaseToRefresh");
//            }
//
//            @Override
//            public void onUIRefreshBegin() {
//                //Log.i(TAG," onUIRefreshBegin");
//                customScrollView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImageView imageView=new ImageView(TestScrollViewActivity.this);
//                        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
//                        imageView.setLayoutParams(lp);
//                        imageView.setBackgroundColor(Color.GREEN);
//                        linearLayout.addView(imageView,0);
//                        customScrollView.refreshComplete();
//                        //Log.i(TAG,"refreshComplete");
//                    }
//                },1500);
//            }
//
//            @Override
//            public void onUIRefreshComplete() {
//                //Log.i(TAG," onUIRefreshComplete");
//            }
//
//            @Override
//            public void onUIPositionChange() {
//                //Log.i(TAG," onUIPositionChange");
//            }
//
//            @Override
//            public void onUIRefreshFooterBegin() {
//                //Log.i(TAG," onUIRefreshFooterBegin");
//                customScrollView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImageView imageView=new ImageView(TestScrollViewActivity.this);
//                        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
//                        imageView.setLayoutParams(lp);
//                        imageView.setBackgroundColor(Color.GREEN);
//                        linearLayout.addView(imageView);
//                        customScrollView.refreshFooterComplete();
//                        //Log.i(TAG,"refreshFooterComplete");
//                    }
//                },500);
//            }
//        });
    }
}
