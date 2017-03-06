package com.dansejijie.library.test;

import android.content.Context;
import android.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tygzx on 17/3/6.
 */

public class TestInputView extends View {

    private static final String TAG=TestInputEvent.class.getSimpleName();

    public TestInputView(Context context) {
        super(context);
    }

    public TestInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction()==KeyEvent.ACTION_DOWN){
            Log.i(TAG,"keyCode"+event.getKeyCode()+" label:"+event.getDisplayLabel()+" repeatCount:"+event.getRepeatCount());
        }

        return super.dispatchKeyEvent(event);
    }

}
