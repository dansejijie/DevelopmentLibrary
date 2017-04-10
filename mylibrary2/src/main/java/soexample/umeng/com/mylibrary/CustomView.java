package soexample.umeng.com.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dansejijie on 17/4/9.
 */

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint fontPaint=new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        fontPaint.setTextSize(20);
        fontPaint.setAntiAlias(true);

        fontPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText("默认",0,20,fontPaint);

        fontPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("黑体",0,40,fontPaint);

        fontPaint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText("等宽",0,60,fontPaint);

        fontPaint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("sans serif",0,80,fontPaint);

        Typeface typeface1=Typeface.createFromAsset(getContext().getAssets(),"fonts/fzltchjt");
        fontPaint.setTypeface(typeface1);
        canvas.drawText("方正兰亭超黑简体",0,20,fontPaint);

    }
}
