package com.dansejijie.library.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.dansejijie.library.R;

/**
 * Created by tygzx on 17/3/3.
 */

public class TestInputEvent extends Activity {

    private static final String TAG=TestInputEvent.class.getSimpleName();

    TextView textView;
    public static void start(Context context){
        Intent intent=new Intent(context,TestInputEvent.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_input_event);
//        textView= (TextView) findViewById(R.id.test_input_event_text);
//
//        textView.setKeyListener(new KeyListener() {
//            @Override
//            public int getInputType() {
//                return 0;
//            }
//
//            @Override
//            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
//                return false;
//            }
//
//            @Override
//            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
//                return false;
//            }
//
//            @Override
//            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
//                return false;
//            }
//
//            @Override
//            public void clearMetaKeyState(View view, Editable content, int states) {
//
//            }
//        });
//
//        textView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                Log.i(TAG,"keyCode:"+keyCode);
//                return false;
//            }
//        });
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        InputDevice device= event.getDevice();
//        textView.setText("deviceId:"+device.getId()+" deviceName:"+device.getName()+ "deviceType:"+device.getKeyboardType());
//        return super.dispatchKeyEvent(event);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        textView.setText(keyCode+"");
//        return super.onKeyDown(keyCode, event);
//
//
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()!=59&&event.getKeyCode()!=66){
            Log.i(TAG,"keyCode"+event.getKeyCode()+" label:"+event.getDisplayLabel()+" repeatCount:"+event.getRepeatCount());
        }
        return super.dispatchKeyEvent(event);
    }


}
