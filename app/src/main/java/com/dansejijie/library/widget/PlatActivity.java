package com.dansejijie.library.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dansejijie.library.widget.plat.Library;
import com.dansejijie.library.widget.plat.Marker;
import com.dansejijie.library.widget.plat.Seat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tygzx on 17/3/20.
 */

public class PlatActivity extends Activity {

    public static void start(Context context){
        Intent intent=new Intent(context,PlatActivity.class);
        context.startActivity(intent);
    }

    Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.dansejijie.library.R.layout.activity_plat);
        library= (Library) findViewById(com.dansejijie.library.R.id.activity_plat_plat);


        List<Marker>seats= Arrays.<Marker>asList(
                new Seat(10,10),
                new Seat(4,4,1));

        library.setRowAndColumn(20,20);
        library.setMarkers(seats);
        //library.setScrollEnable(false);
    }
}
