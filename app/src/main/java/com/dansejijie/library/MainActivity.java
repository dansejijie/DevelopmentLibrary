package com.dansejijie.library;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dansejijie.library.test.TestInputEvent;
import com.dansejijie.library.test.TestScrollView;
import com.dansejijie.library.widget.TestScrollViewActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<Action>mDatas;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        recyclerView= (RecyclerView) findViewById(R.id.app_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter=new MainAdapter(this, mDatas, null);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        }));
    }
}
