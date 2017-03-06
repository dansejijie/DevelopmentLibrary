package com.dansejijie.library.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dansejijie.library.R;

/**
 * Created by tygzx on 17/3/2.
 */

public class TestScrollView extends Activity {


    public static void start(Context context){
        Intent intent=new Intent(context,TestScrollView.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scroll_view_test);
    }
}
