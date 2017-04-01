package com.dansejijie.library.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dansejijie.library.R;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by tygzx on 17/3/16.
 */

public class CameraActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback{


    public static void start(Context context){
        Intent intent=new Intent(context,CameraActivity.class);
        context.startActivity(intent);
    }
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.take_picture:
//                    if (mCameraView != null) {
//                        mCameraView.takePicture();
//                    }
//                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        //mCameraView = (CameraView) findViewById(R.id.camera);
        mCameraView=new CameraView(this);

        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(200, 200);
        mCameraView.setLayoutParams(layoutParams);
        ((LinearLayout)findViewById(R.id.activity_camera_container)).addView(mCameraView);
        if (mCameraView != null) {
            //mCameraView.addCallback(mCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA_PERMISSION:
//                if (permissions.length != 1 || grantResults.length != 1) {
//                    throw new RuntimeException("Error on requesting camera permission.");
//                }
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, R.string.camera_permission_not_granted,
//                            Toast.LENGTH_SHORT).show();
//                }
//                // No need to start camera here; it is handled by onResume
//                break;
//        }
//    }



//    private Handler getBackgroundHandler() {
//        if (mBackgroundHandler == null) {
//            HandlerThread thread = new HandlerThread("background");
//            thread.start();
//            mBackgroundHandler = new Handler(thread.getLooper());
//        }
//        return mBackgroundHandler;
//    }
//
//    private CameraView.Callback mCallback
//            = new CameraView.Callback() {
//
//        @Override
//        public void onCameraOpened(CameraView cameraView) {
//            Log.d(TAG, "onCameraOpened");
//        }
//
//        @Override
//        public void onCameraClosed(CameraView cameraView) {
//            Log.d(TAG, "onCameraClosed");
//        }
//
//        @Override
//        public void onPictureTaken(CameraView cameraView, final byte[] data) {
//            Log.d(TAG, "onPictureTaken " + data.length);
//            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
//                    .show();
//            getBackgroundHandler().post(new Runnable() {
//                @Override
//                public void run() {
//                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                            "picture.jpg");
//                    OutputStream os = null;
//                    try {
//                        os = new FileOutputStream(file);
//                        os.write(data);
//                        os.close();
//                    } catch (IOException e) {
//                        Log.w(TAG, "Cannot write to " + file, e);
//                    } finally {
//                        if (os != null) {
//                            try {
//                                os.close();
//                            } catch (IOException e) {
//                                // Ignore
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    };


}
