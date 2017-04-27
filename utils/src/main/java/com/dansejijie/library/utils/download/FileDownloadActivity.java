package com.dansejijie.library.utils.download;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dansejijie.library.utils.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by tygzx on 17/4/27.
 */

public class FileDownloadActivity extends Activity {


    Button button;
    TextView textView;

    private static final String TAG=FileDownloadActivity.class.getSimpleName();

    public static void start(Context context){
        Intent intent=new Intent(context,FileDownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download_layout);

        button= (Button) findViewById(R.id.file_download_download);
        textView= (TextView) findViewById(R.id.file_download_progress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile("http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.ch9/pdf");
            }
        });

    }

    public void downloadFile(String url){

        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/a/ch9.pdf";
        FileDownloader.getImpl()
                .create(url)
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
                        //textView.setText("pending");
                        //handler.sendEmptyMessage(1);
                        Log.e(TAG,"pengding");
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        //textView.setText("connected");
                        //handler.sendEmptyMessage(2);
                        Log.e(TAG,"connected");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        //textView.setText("current size"+soFarBytes+"total size:"+totalBytes);
                        //handler.sendEmptyMessage(3);
                        Log.e(TAG,"progress");
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        //textView.setText("complete");
                        //handler.sendEmptyMessage(4);
                        Log.e(TAG,"blockComplete");
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        //textView.setText("retry");
                        //handler.sendEmptyMessage(5);
                        Log.e("TAG","retry");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //textView.setText("completed");
                        //handler.sendEmptyMessage(6);
                        Log.e(TAG,"completed");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //textView.setText("paused");
                        //handler.sendEmptyMessage(7);
                        Log.e(TAG,"paused");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        //textView.setText("error:"+e.getMessage());
                        //handler.sendEmptyMessage(8);
                        Log.e(TAG,"error");
                        e.printStackTrace();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        //textView.setText("warn");
                        //handler.sendEmptyMessage(9);
                        Log.e(TAG,"warn");
                    }
                }).start();
    }
}
