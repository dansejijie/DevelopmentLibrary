package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by tygzx on 17/4/27.
 */

public class PageSaveUtil {

    private static final String fileName="page_info";

    public static void savePageInfo(Context context, HashMap<String,Integer>chapters)throws Exception{

        ObjectOutputStream oos=new ObjectOutputStream(context.openFileOutput(fileName,context.MODE_PRIVATE));
        oos.writeObject(chapters);
        oos.close();
    }

    public static HashMap<String,Integer> getPageInfo(Context context)throws Exception{

        ObjectInputStream ois=new ObjectInputStream(context.openFileInput(fileName));
        HashMap<String,Integer>map= (HashMap<String,Integer>) ois.readObject();
        ois.close();
        return map;
    }
}
