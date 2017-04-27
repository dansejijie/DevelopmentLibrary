package com.dansejijie.library.widget.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dansejijie.library.widget.R;

/**
 * Created by tygzx on 17/4/27.
 */

public class PDFWebViewActivity extends Activity {

    public static void start(Context context){
        Intent intent=new Intent(context,PDFWebViewActivity.class);
        context.startActivity(intent);
    }

    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_webview);
        webView= (WebView) findViewById(R.id.pdf_webview);
        webView.loadUrl("https://www.baidu.com/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
    }


    @Override
    public void onBackPressed() {
        //后退网页
        webView.goBack();
    }
}
