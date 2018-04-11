package com.jackie.bookpagerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jackie on 2018/4/11.
 */

public class WeatherGlassView extends View {
    private Paint mPinkPaint;
    private Paint mLinePaint;
    private int mWidth;
    private int mHeight;
    private float mOutRadious;
    private float mInnerRadiou;

    public WeatherGlassView(Context context) {
        super(context);
        init(context);
    }

    public WeatherGlassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherGlassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        mPinkPaint = new Paint();
        mPinkPaint.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPinkPaint.setStrokeWidth(10);
        mPinkPaint.setColor(Color.CYAN);
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();




    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutSidePink(canvas);
    }

    //绘制外部粉红色温度体,圆弧
    private void drawOutSidePink(Canvas canvas){
        float x = 0;
        float y = mHeight/2;
        RectF rectF = new RectF(x,y,getWidth()-x,getHeight());
        canvas.drawArc(rectF,0,140,false,mPinkPaint);
        canvas.drawRect(rectF,mLinePaint);
    }

    //绘制内部粉红色
    private void drawInnerPink(Canvas canvas){

    }
    //绘制两边的线
    private void drawDoubleSideLine(Canvas canvas){

    }
    //绘制三个爱心
    private void drawThreeLove(Canvas canvas){

    }



}
