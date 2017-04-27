package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by tygzx on 17/4/27.
 */

public class PDFApplication  {

    public static void init(Context context){
        FileDownloader.init(context);
    }
}
