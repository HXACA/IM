package com.example.minions.im.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**下划线
 * Created by siner on 2017/6/29.
 */

public class LineEditText extends EditText{

    private Paint paint;
    public LineEditText(Context context, AttributeSet attrs)
    {
        super(context,attrs);

        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(0,this.getHeight()-1,this.getWidth()-1,this.getHeight()-1,paint);
        super.draw(canvas);

    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Log.i("info","xixi");

    }

}
