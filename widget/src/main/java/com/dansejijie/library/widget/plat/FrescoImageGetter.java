package com.dansejijie.library.widget.plat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by tygzx on 17/2/10.
 */

public class FrescoImageGetter {


    private final int MAX_WIDTH=1024;
    private final int MAX_HEIGHT=1024;

    private static FrescoImageGetter instance;

    private static Context context;

    private static ImagePipeline imagePipeline;

    private FrescoImageGetter(Context context){
        this.context=context;
    }

    public synchronized static FrescoImageGetter getInstance(Context context){
        if (instance==null){
            synchronized (FrescoImageGetter.class){
                if (instance==null){
                    instance=new FrescoImageGetter(context);
                    Fresco.initialize(context);
                    imagePipeline= Fresco.getImagePipeline();
                }
            }
        }
        return instance;
    }

    public void getNetBitmap(final Uri uri, final CallBack callBack){

        ImageRequest imageRequest= ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(MAX_WIDTH,MAX_HEIGHT)).build();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, UiThreadImmediateExecutorService.getInstance());
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                callBack.success(uri,bitmap);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                callBack.failed(uri);
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }


    public interface CallBack{

        void success(Uri uri, Bitmap bitmap);

        void failed(Uri uri);
    }

}
