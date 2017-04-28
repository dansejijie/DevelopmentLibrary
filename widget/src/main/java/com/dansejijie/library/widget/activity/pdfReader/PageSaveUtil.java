package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by tygzx on 17/4/27.
 */

public class PageSaveUtil {

    private static final String fileName="page_info";

    public static void savePageInfo(Context context, HashMap<String,Integer>chapters)throws Exception{

        File file=new File(context.getCacheDir(),fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
        //ObjectOutputStream oos=new ObjectOutputStream(context.openFileOutput(fileName,context.MODE_PRIVATE));
        oos.writeObject(chapters);
        oos.flush();
    }

    public static HashMap<String,Integer> getPageInfo(Context context)throws Exception{

        File file=new File(context.getCacheDir(),fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
        HashMap<String,Integer>map= (HashMap<String,Integer>) ois.readObject();
        ois.close();
        return map;
    }
}
