package com.dansejijie.library;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

    }

    private void initDatas() {

        mDatas= Arrays.asList(new Action("aa", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"aa",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
