package com.dansejijie.library.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tygzx on 17/3/3.
 */

public class TCustomScrollView extends CustomScrollView {

    public TCustomScrollView(Context context) {
        super(context);
    }

    public TCustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TCustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initScrollView() {
        super.initScrollView();
        TAG="TCustomScrollView";
    }
}
