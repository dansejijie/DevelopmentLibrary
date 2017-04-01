package com.dansejijie.library.widget.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.util.Arrays;

/**
 * Created by tygzx on 17/3/17.
 */

public class Camera2View extends TextureView implements
        TextureView.SurfaceTextureListener,
        CameraViewImpl {

    private Handler mHandler;

    private HandlerThread mThreadHandler;

    private Size mPreviewSize;

    private CaptureRequest.Builder mPreviewBuilder;

    private ImageReader mImageReader;


    public Camera2View(Context context) {
        super(context);
        initView();
        initLooper();
    }

    public Camera2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initLooper();
    }

    public Camera2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initLooper();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void initView(){
        setSurfaceTextureListener(this);
    }

    public void initLooper(){
        mThreadHandler=new HandlerThread("Camera2");
        mThreadHandler.start();
        mHandler=new Handler(mThreadHandler.getLooper());
    }

    /**************************SurfaceTextureListener*******************************/
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        try {
            CameraManager manager= (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            manager.openCamera("0",mCameraDeviceStateCallback,mHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    private CameraDevice.StateCallback mCameraDeviceStateCallback=new CameraDevice.StateCallback(){
        @Override
        public void onOpened(CameraDevice camera) {

            SurfaceTexture texture=getSurfaceTexture();
            texture.setDefaultBufferSize(getMeasuredWidth(),getMeasuredHeight());
            Surface surface=new Surface(texture);

            mImageReader=ImageReader.newInstance(getMeasuredWidth(),getMeasuredHeight(), ImageFormat.JPEG,2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener,mHandler);
            
            try {
                mPreviewBuilder=camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewBuilder.addTarget(surface);
                camera.createCaptureSession(Arrays.asList(surface),mSessionStateCallback,mHandler);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    CameraCaptureSession.StateCallback mSessionStateCallback=new CameraCaptureSession.StateCallback(){
        @Override
        public void onConfigured(CameraCaptureSession session) {

            try {
                session.setRepeatingRequest(mPreviewBuilder.build(),mSessionCaptureCallback,mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    CameraCaptureSession.CaptureCallback mSessionCaptureCallback=new CameraCaptureSession.CaptureCallback(){

    };

    ImageReader.OnImageAvailableListener mOnImageAvailableListener=new ImageReader.OnImageAvailableListener(){
        @Override
        public void onImageAvailable(ImageReader reader) {

        }
    };



    public double getRatio() {
        return 0;
    }

    @Override
    public void setCameraType(int type) {

    }

    @Override
    public void setCaptureMode(int captureMode) {

    }

    @Override
    public void setCaptureQuality(String captureQuality) {

    }

    @Override
    public void setTorchMode(int torchMode) {

    }

    @Override
    public void setFlashMode(int flashMode) {

    }

}
