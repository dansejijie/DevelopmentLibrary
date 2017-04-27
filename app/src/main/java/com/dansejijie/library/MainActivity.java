package com.dansejijie.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dansejijie.library.activity.Camera2Demo;
import com.dansejijie.library.activity.CameraActivity;
import com.dansejijie.library.bean.Dog;
import com.dansejijie.library.test.TestInputEvent;
import com.dansejijie.library.test.TestScrollView;
import com.dansejijie.library.utils.download.FileDownloadActivity;
import com.dansejijie.library.widget.PlatActivity;
import com.dansejijie.library.widget.TestScrollViewActivity;
import com.dansejijie.library.widget.activity.pdfReader.PDFViewActivity;
import com.dansejijie.library.widget.activity.PDFWebViewActivity;
import com.dansejijie.library.widget.test.Tiger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<Action>mDatas;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initDatas();
        recyclerView= (RecyclerView) findViewById(R.id.app_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter=new MainAdapter(this, mDatas, null);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        Dog dog=new Dog();
        dog.setName("wang");
        Tiger tiger=new Tiger("wang wang");
        tiger.setName("wang");



        SimpleDraweeView imageView= (SimpleDraweeView) findViewById(R.id.app_image_view);

        imageView.setImageURI("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=2&spn=0&di=213489658910&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=819201812%2C3553302270&os=4058329962%2C3718712582&simid=3386491690%2C470645892&adpicid=0&lpn=0&ln=1991&fr=&fmq=1489562822436_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fm2.quanjing.com%2F2m%2Falamyrf005%2Fb1fw89.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bq7wg3tg2_z%26e3Bv54AzdH3Ffiw6jAzdH3Fk8uobl_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0");

    }

    public String getString() {
        return "Robust";
    }

    private void initDatas() {

        mDatas= Arrays.asList(new Action("testScrollView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestScrollViewActivity.start(MainActivity.this);
            }
        }),new Action("testScrollViewTest", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestScrollView.start(MainActivity.this);
            }
        }),new Action("testInputEvent",new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TestInputEvent.start(MainActivity.this);
            }
        }),new Action("camera", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraActivity.start(MainActivity.this);
            }
        }),new Action("plat", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatActivity.start(MainActivity.this);

            }
        }),new Action("camera2demo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera2Demo.start(MainActivity.this);
            }
        }),new Action("PDFView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFViewActivity.start(MainActivity.this);
            }
        }),new Action("PDFWebView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFWebViewActivity.start(MainActivity.this);
            }
        }),new Action("FileDownloadActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloadActivity.start(MainActivity.this);
            }
        }));
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        Log.i("TAG","code:"+event.getKeyCode());
//        return super.dispatchKeyEvent(event);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.i("TAG","code:"+event.getKeyCode());
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
//        Log.i("TAG","code:"+event.getKeyCode());
//        return super.dispatchKeyShortcutEvent(event);
//    }
//
//    @Override
//    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
//        Log.i("TAG","code:"+event.getKeyCode());
//        return super.onKeyShortcut(keyCode, event);
//    }
}
