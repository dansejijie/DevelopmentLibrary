package com.dansejijie.library.widget.camera;

import android.view.MotionEvent;

/**
 * Created by tygzx on 17/3/17.
 */

public interface CameraViewImpl {




    public double getRatio();

    public void setCameraType(final int type);

    public void setCaptureMode(final int captureMode);

    public void setCaptureQuality(String captureQuality);

    public void setTorchMode(int torchMode);

    public void setFlashMode(int flashMode);



}
