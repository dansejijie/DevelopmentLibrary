package com.dansejijie.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tygzx on 17/2/28.
 */

public class BaseActivity extends Activity {

    public static void start(Context context){
        Intent intent=new Intent(context,BaseActivity.class);
        context.startActivity(intent);
    }
}
