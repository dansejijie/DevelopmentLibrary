package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import java.io.File;
import java.util.HashMap;

/**
 * Created by tygzx on 17/4/27.
 */

public class CustomPDFView extends PDFView implements OnPageChangeListener,OnLoadCompleteListener{


    private static final String TAG=CustomPDFView.class.getSimpleName();
    private HashMap<String,Integer> chapterMap;
    private String filePath;
    private int page;

    private ProgressBar progressBar;
    private TextView tv_erroInfo;




    public CustomPDFView(Context context, AttributeSet set) {
        super(context, set);


        try {

            chapterMap=PageSaveUtil.getPageInfo(context);

            if (chapterMap==null){
                chapterMap=new HashMap<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (chapterMap==null){
                chapterMap=new HashMap<>();
            }
        }
    }

    public void open(Uri uri){

        if (progressBar!=null){
            removeView(progressBar);
        }

        if (tv_erroInfo!=null){
            removeView(tv_erroInfo);
        }



        if (uri.toString().startsWith("http://")){
            String fileName= new String(Base64.encode(uri.toString().getBytes(),Base64.DEFAULT))+".pdf";

            String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ "/"+getContext().getPackageName()+"/"+fileName;
            File file=new File(path);
            if (file.exists()){

                filePath=path;
                if (chapterMap.containsKey(filePath)) {
                    page = chapterMap.get(filePath);
                }

                Uri uri2=Uri.fromFile(file);

                fromUri(uri2).defaultPage(page)
                        .onPageChange(CustomPDFView.this)
                        .enableAnnotationRendering(true)
                        .onLoad(CustomPDFView.this)
                        .scrollHandle(new DefaultScrollHandle(getContext()))
                        .onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                fitToWidth();
                            }
                        })
                        .load();

            }else {
                FileDownloader.getImpl()
                        .create(uri.toString())
                        //.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        //.addHeader("Accept-Encoding","gzip, deflate, sdch")
                        //.addHeader("Accept-Language","en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4")
                        //.addHeader("Host","onlinelibrary.wiley.com")
                        //.addHeader("Proxy-Connection","keep-alive")
                        //.addHeader("Referer","http://onlinelibrary.wiley.com/book/10.1002/9781118295472")
                        //.addHeader("Upgrade-Insecure-Requests","1")
                        //.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        //.addHeader("Cookie","s_vnum=1495866106042%26vn%3D1; OLProdServerID=1024; EuCookie=\"this site uses cookies\"; dtCookie=3CF6E5FC3176F467AE488375AD2028C4|b25saW5lbGlicmFyeS53aWxleS5jb218MQ; JSESSIONID=31B994098376FDAF484ED1B77CFC5A5A.f01t04; WOLSIGNATURE=585562dc-6781-472e-bd40-6ed1fc9fcc3a; REPORTINGWOLSIGNATURE=2706690724253925649; dtLatC=62; __utma=235730399.574461212.1493275123.1493275123.1493275123.1; __utmb=235730399.4.10.1493275123; __utmc=235730399; __utmz=235730399.1493275123.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); s_prevChannel=BOOKS; s_prevProp1=ABSTRACT; s_prevProp2=BOOK%20CHAPTER%20SUMMARY; s_cc=true; __atuvc=3%7C17; __atuvs=590191f9421ad8eb002; dtPC=-; s_fid=588A9172112C305A-28ADC36CB7C79E3E; s_invisit=true; s_visit=1")
                        .setPath(path)
                        .setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                Log.d(TAG,"pengding");
                                if (progressBar==null){
                                    progressBar=new ProgressBar(getContext());
//                                    progressBar.setMax(100);
//                                    progressBar.setProgress(20);
                                    RelativeLayout.LayoutParams layoutParams=new LayoutParams(100,100);
                                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    progressBar.setLayoutParams(layoutParams);
                                }

                                addView(progressBar);

                            }

                            @Override
                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                Log.d(TAG,"connected");
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                Log.d(TAG,"progress:"+soFarBytes+" total:"+totalBytes);
                            }

                            @Override
                            protected void blockComplete(BaseDownloadTask task) {

                                Log.d(TAG,"blockComplete");
                            }

                            @Override
                            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {

                                Log.d("TAG","retry");
                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {

                                if (progressBar!=null){
                                    removeView(progressBar);
                                }

                                if (tv_erroInfo!=null){
                                    removeView(tv_erroInfo);
                                }

                                filePath = task.getPath();
                                Log.e(TAG,"completed path:"+filePath);

                                if (chapterMap.containsKey(filePath)) {
                                    page = chapterMap.get(filePath);
                                }
                                Uri uri2=Uri.fromFile(new File(filePath));
                                fromUri(uri2).defaultPage(page)
                                        .onPageChange(CustomPDFView.this)
                                        .enableAnnotationRendering(true)
                                        .onLoad(CustomPDFView.this)
                                        .scrollHandle(new DefaultScrollHandle(getContext()))
                                        .onRender(new OnRenderListener() {
                                            @Override
                                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                                fitToWidth();
                                            }
                                        })
                                        .load();



                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                Log.e(TAG,"paused");
                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {

                                if (progressBar!=null){
                                    removeView(progressBar);
                                }
                                if (tv_erroInfo==null){
                                    tv_erroInfo=new TextView(getContext());
                                    RelativeLayout.LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    tv_erroInfo.setLayoutParams(layoutParams);
                                    tv_erroInfo.setTextSize(12*getContext().getResources().getDisplayMetrics().density);
                                    tv_erroInfo.setText("加载失败");
                                }

                                addView(tv_erroInfo);

                                Log.e(TAG,"error");
                                e.printStackTrace();
                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {

                                Log.e(TAG,"warn");
                            }
                        }).start();
            }
        }else {

            filePath=uri.toString();
            if (chapterMap.containsKey(filePath)) {
                page = chapterMap.get(filePath);
            }

            fromUri(uri).defaultPage(page)
                    .onPageChange(CustomPDFView.this)
                    .enableAnnotationRendering(true)
                    .onLoad(CustomPDFView.this)
                    .scrollHandle(new DefaultScrollHandle(getContext()))
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                            fitToWidth();
                        }
                    })
                    .load();
        }

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            chapterMap.put(filePath,page);
            PageSaveUtil.savePageInfo(getContext(),chapterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**********onPageChanged*************/
    @Override
    public void onPageChanged(int page, int pageCount) {
        this.page=page;
    }

    /*************OnLoadCompleteListener*************/
    @Override
    public void loadComplete(int nbPages) {
        Log.d(TAG,"loadComplete page:"+nbPages);
    }



}
