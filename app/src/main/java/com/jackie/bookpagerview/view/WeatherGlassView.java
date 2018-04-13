package com.jackie.bookpagerview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jackie.bookpagerview.R;

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
    private Paint mInnerCirclePaint;
    private Path mInnerArcPath;
    private Path mInnerRectPath;

    private Paint mDoubleLinePaint;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Paint mBitmapPaint;
    private Paint mWavePaint;
    private Paint mInnerWavePaint;

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
        mPinkPaint.setAntiAlias(true);
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#E41172"));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mInnerCirclePaint.setColor(Color.parseColor("#E41172"));
        mInnerCirclePaint.setAntiAlias(true);
        //用于绘制内部的path
        mInnerArcPath = new Path();
        mInnerRectPath = new Path();
        mDoubleLinePaint = new Paint();
        mDoubleLinePaint.setStrokeWidth(6);
        mDoubleLinePaint.setStyle(Paint.Style.STROKE);
        mDoubleLinePaint.setColor(Color.parseColor("#E41172"));
        mDoubleLinePaint.setAntiAlias(true);
        mBitmapPaint = new Paint();
        mWavePaint = new Paint();
        mWavePaint.setColor(Color.parseColor("#E41172"));
        mWavePaint.setStyle(Paint.Style.FILL);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.love1);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.love);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.love4);
        mInnerWavePaint = new Paint();
        mInnerWavePaint.setStrokeWidth(1);
        mInnerWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mInnerWavePaint.setColor(Color.parseColor("#E41172"));
        mInnerWavePaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mOutRadious = (mWidth/10*7)/2;
        mInnerRadiou = (mWidth/4);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutSidePink(canvas);
        drawDoubleSideLine(canvas);
        drawInnerPink(canvas);
    }

    //绘制外部粉红色温度体,圆弧
    private void drawOutSidePink(Canvas canvas){
        float x = 0;
        float y = mHeight/20f * 12.5f;
        //圆弧的矩阵
        RectF rectF = new RectF((float) (mWidth/10f*1.5),y, (float) (mWidth/10*1.5+mWidth/10*7),y+mWidth/10*7);
        canvas.drawArc(rectF,-60,300,false,mPinkPaint);
        //绘制两条竖线
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
        //绘制顶部圆弧
        RectF topRectF = new RectF(
                (float) ((mWidth/10f*1.5)+(mOutRadious-Math.cos(Math.PI/3)*mOutRadious)),
                10,
                (float) ((mWidth/10f*1.5)+(mOutRadious+Math.cos(Math.PI/3)*mOutRadious)),
                mHeight/9*2);
        canvas.drawArc(topRectF,180,180,false,mPinkPaint);


        //绘制边框
//        RectF rectF1 = new RectF(x,y,getWidth()-1,getHeight()-1);
//        canvas.drawRect(rectF1,mLinePaint);
    }

    private float addCount = 10;
    //绘制内部粉红色
    private void drawInnerPink(Canvas canvas){
        mInnerRectPath.reset();
        float x = mWidth/2;
        float y = (float) (mHeight/20f * 12.5 + mWidth/10*7/2);  //内圆的Y坐标

        canvas.drawCircle(x,y,mInnerRadiou,mInnerCirclePaint);
        mInnerRectPath.moveTo((float) (x - Math.cos(Math.PI/2.7)*mInnerRadiou),y);  //左下
        mInnerRectPath.lineTo((float) (x - Math.cos(Math.PI/2.7)*mInnerRadiou),y-mHeight/5*3); //左上
        //设置两个一阶贝塞尔曲线
        //控制点的x,y
        float firstControlX = (float) ((float) (x - Math.cos(Math.PI/2.7)*mInnerRadiou) + ((float) x -(x - Math.cos(Math.PI/2.7)*mInnerRadiou))/2);
        float firstControlY = y-mHeight/5*3+addCount;

        float secondControlX = (float) (x + ((float) x -(x - Math.cos(Math.PI/2.7)*mInnerRadiou))/2);
        float secondControlY = y-mHeight/5*3-addCount;

        mInnerRectPath.quadTo(firstControlX,firstControlY,x,y-mHeight/5*3);
        mInnerRectPath.lineTo(x,y-mHeight/5*3);
//
        mInnerRectPath.quadTo(secondControlX,secondControlY,(float) (x + Math.cos(Math.PI/2.7)*mInnerRadiou),y-mHeight/5*3);
        mInnerRectPath.lineTo((float) (x + Math.cos(Math.PI/2.7)*mInnerRadiou),y-mHeight/5*3);
        mInnerRectPath.lineTo((float) (x + Math.cos(Math.PI/2.7)*mInnerRadiou),y);
        mInnerRectPath.lineTo((float) (x - Math.cos(Math.PI/2.7)*mInnerRadiou),y);
//        RectF rectF = new RectF(
//                (float) (x - Math.cos(Math.PI/2.7)*mInnerRadiou),  //左
//                y-mHeight/5*3,                                     //上
//                (float) (x + Math.cos(Math.PI/2.7)*mInnerRadiou),  //右
//                y);                                                //下
//        Log.i("jc", "drawInnerPink: -------"+"top:"+(y-mHeight/5*3)+"bottom"+y);
//        canvas.drawRect(rectF,mInnerCirclePaint);
        canvas.drawPath(mInnerRectPath,mInnerWavePaint);
        handler.sendEmptyMessageDelayed(0x123,10);

    }
    private boolean isAdd = true;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            handler.removeCallbacksAndMessages(null);
            Log.i("jackie", "handleMessage: -------");
            if (isAdd){
                if (addCount == 10){
                    isAdd = false;
                    addCount --;
                }
                if (isAdd){
                    addCount ++;
                }
            }else{
                if (addCount == -10){
                    isAdd = true;
                    addCount ++;
                }
                if (!isAdd){
                    addCount --;
                }
            }
            postInvalidate();
        }
    };
    //绘制两边的线
    private void drawDoubleSideLine(Canvas canvas){
        //最低点
        float mBottom = mHeight/20f * 12.5f;
        float mTop = mHeight/9;

        float perWidth = (mBottom - mTop)/10;

        float mRight = (float) ((mWidth/10f*1.5)+(mOutRadious-Math.cos(Math.PI/3)*mOutRadious));

        //绘制左边的线
        for (int i = 0; i <11 ; i++) {
            if (i == 0 || i== 5 || i== 10) {
                canvas.drawLine(mRight/10*2, mTop + i * perWidth, mRight, mTop + i * perWidth, mDoubleLinePaint);
            }else{
                canvas.drawLine(mRight/10*6, mTop + i * perWidth, mRight, mTop + i * perWidth, mDoubleLinePaint);
            }

        }
        float mYouRight = (float) ((mWidth/10f*1.5)+(mOutRadious+Math.cos(Math.PI/3)*mOutRadious));
        //绘制右边的线
        for (int i = 0; i <11 ; i++) {
            if (i == 0 || i== 5 || i== 10) {
                canvas.drawLine(mYouRight, mTop + i * perWidth, mYouRight + mRight/10*8, mTop + i * perWidth, mDoubleLinePaint);
            }else{
                canvas.drawLine(mYouRight, mTop + i * perWidth, mYouRight + mRight/10*4, mTop + i * perWidth, mDoubleLinePaint);
            }

        }
        //绘制三个爱心
        canvas.drawBitmap(bitmap, (float) (mYouRight+mRight/10*4.5), (float) (mTop+perWidth*8.7),mBitmapPaint);

        canvas.drawBitmap(bitmap1, (float) (mYouRight+mRight/10*4.5), (float) (mTop+perWidth*3.5),mBitmapPaint);

        canvas.drawBitmap(bitmap2, (float) (mYouRight+mRight/10*4), (float) (mTop-1.5*perWidth),mBitmapPaint);
    }


//    private void drawThreeLove(Canvas canvas){
//
//
//    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
}
