package com.dansejijie.library.widget.activity.pdfReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dansejijie.library.widget.R;

/**
 * Created by tygzx on 17/4/28.
 */

public class CustomRelativeLayoutActivity extends Activity {


    public static void start(Context context){
        Intent intent=new Intent(context,CustomRelativeLayoutActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_layout);
    }
}
