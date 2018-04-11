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
        mPinkPaint.setStyle(Paint.Style.STROKE);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPinkPaint.setStrokeWidth(8);
        mPinkPaint.setColor(Color.parseColor("#E41172"));
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#E41172"));
        mLinePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mOutRadious = (mWidth/10*7)/2;  //(宽度的五分之三)/2



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutSidePink(canvas);
    }

    //绘制外部粉红色温度体,圆弧
    private void drawOutSidePink(Canvas canvas){
        float x = 0;
        float y = mHeight/20f * 12.5f;
        RectF rectF = new RectF((float) (mWidth/10f*1.5),y, (float) (mWidth/10*1.5+mWidth/10*7),y+mWidth/10*7);
        canvas.drawArc(rectF,-60,300,false,mPinkPaint);
        canvas.drawLine(
                (float) ((mWidth/10f*1.5)+(mOutRadious-Math.cos(Math.PI/3)*mOutRadious)),
                y+(mOutRadious -(float) (Math.sin(Math.PI/3)*mOutRadious)+3),
                (float) ((mWidth/10f*1.5)+(mOutRadious-Math.cos(Math.PI/3)*mOutRadious)),
                mHeight/9,
                mPinkPaint);
        canvas.drawLine(
                (float) ((mWidth/10f*1.5)+(mOutRadious+Math.cos(Math.PI/3)*mOutRadious)),
                y+(mOutRadious -(float) (Math.sin(Math.PI/3)*mOutRadious)+3),
                (float) ((mWidth/10f*1.5)+(mOutRadious+Math.cos(Math.PI/3)*mOutRadious)),
                mHeight/9,
                mPinkPaint);
        RectF topRectF = new RectF(
                (float) ((mWidth/10f*1.5)+(mOutRadious-Math.cos(Math.PI/3)*mOutRadious)-1),
                10,
                (float) ((mWidth/10f*1.5)+(mOutRadious+Math.cos(Math.PI/3)*mOutRadious)+1),
                mHeight/9*2);
        canvas.drawArc(topRectF,180,180,false,mPinkPaint);


        //绘制边框
        RectF rectF1 = new RectF(x,y,getWidth()-1,getHeight()-1);
        canvas.drawRect(rectF1,mLinePaint);
    }

    //绘制内部粉红色
    private void drawInnerPink(Canvas canvas){
        float x = 0;
        float y = mHeight/3 * 2;
        RectF rectF = new RectF(mWidth/4,y,mWidth/5+mWidth/5*3,y+mWidth/5*3);
        canvas.drawArc(rectF,-60,300,false,mPinkPaint);
    }
    //绘制两边的线
    private void drawDoubleSideLine(Canvas canvas){

    }
    //绘制三个爱心
    private void drawThreeLove(Canvas canvas){

    }



}
