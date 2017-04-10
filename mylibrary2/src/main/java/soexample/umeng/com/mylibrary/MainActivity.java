package soexample.umeng.com.mylibrary;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ViewGroup;

/**
 * Created by dansejijie on 17/4/9.
 */

public class MainActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CustomView customView=new CustomView(this);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customView.setLayoutParams(layoutParams);
        setContentView(customView);
    }
}
