package com.jackie.bookpagerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/9.
 */

public class MagicNewBezierCircle extends View {

    private float blackMagic = 0.551915024494f;
    private int mWidth;
    private int mHeight;
    private float mCenterX;
    private float mCenterY;
    private float mRadious = 50;
    private float mMagicDistance;
    private Path mPath;
    private Paint mPaint;
    private float mStretchDistance = mRadious/2;
    private List<PointF> mPointDatas; //放置四个数据点的集合
    private List<PointF> mPointControlls;//放置8个控制点的集合


    public MagicNewBezierCircle(Context context) {
        super(context);
        init();
    }

    public MagicNewBezierCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagicNewBezierCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FF8E8F"));
//        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;
        mRadious = 60;
        mMagicDistance = mRadious*blackMagic;
        mStretchDistance = mRadious/2;
        initData();
    }
    private void initData(){
        mPointDatas = new ArrayList<>();
        mPointControlls = new ArrayList<>();

        mPointDatas.add(new PointF(mCenterX, mCenterY - mRadious));    //上
        mPointDatas.add(new PointF(mCenterX+mRadious/2, mCenterY - mRadious/2));    //东北
        mPointDatas.add(new PointF(mCenterX + mRadious, mCenterY));    //右
        mPointDatas.add(new PointF(mCenterX+mRadious/2, mCenterY + mRadious/2));    //东南
        mPointDatas.add(new PointF(mCenterX, mCenterY + mRadious));    //下
        mPointDatas.add(new PointF(mCenterX-mRadious/2, mCenterY + mRadious/2));    //西南
        mPointDatas.add(new PointF(mCenterX - mRadious, mCenterY));    //左
        mPointDatas.add(new PointF(mCenterX-mRadious/2, mCenterY - mRadious/2));    //西北

        mPointControlls.add(new PointF(mCenterX + mMagicDistance, mCenterY - mRadious));   //顶部右侧最高点
        mPointControlls.add(new PointF(mCenterX + mRadious, mCenterY - mMagicDistance));   //顶部右侧第二高的点

        mPointControlls.add(new PointF(mCenterX + mRadious, mCenterY + mMagicDistance));   //东南侧较高点
        mPointControlls.add(new PointF(mCenterX + mMagicDistance, mCenterY + mRadious));   //东南侧较低点

        mPointControlls.add(new PointF(mCenterX - mMagicDistance, mCenterY + mRadious));  //西南侧较低点
        mPointControlls.add(new PointF(mCenterX - mRadious, mCenterY + mMagicDistance));  //西南侧较高点

        mPointControlls.add(new PointF(mCenterX - mRadious, mCenterY - mMagicDistance));    //西北侧较低点
        mPointControlls.add(new PointF(mCenterX - mMagicDistance, mCenterY - mRadious));    //西北侧较高点
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
//        canvas.translate(mRadious,mRadious);
//绘制控制点
//        for (int i = 0; i < mPointControlls.size(); i++) {
//            canvas.drawPoint(mPointControlls.get(i).x, mPointControlls.get(i).y, mPaint);
//        }

        //利用贝塞尔曲线实现画圆
        mPath.moveTo(mPointDatas.get(0).x,mPointDatas.get(0).y);
        for (int i=0;i<mPointDatas.size();i++) {
            if (i == mPointDatas.size()-1){
                mPath.cubicTo(mPointControlls.get(2*i).x, mPointControlls.get(2*i).y,
                        mPointControlls.get(2*i+1).x, mPointControlls.get(2*i+1).y,
                        mPointDatas.get(0).x, mPointDatas.get(0).y);
            }else {
                mPath.cubicTo(mPointControlls.get(2 * i).x, mPointControlls.get(2 * i).y,
                        mPointControlls.get(2*i+1).x, mPointControlls.get(2*i+1).y,
                        mPointDatas.get(i+1).x, mPointDatas.get(i+1).y);
            }


        }

        canvas.drawPath(mPath,mPaint);

        mCurrTime += 20;
        //A弹出
        if (mCurrTime <= mDuration/6){   //0-50
            mPointDatas.get(0).y -= aTop/mCount;

            mPointControlls.get(0).x += aTotal/mCount;
            postInvalidateDelayed((long) mPiece);
            Log.i(TAG, "onDraw: -----1----"+mCurrTime+"-------"+mDuration);
        }else if (mCurrTime> mDuration/6 && mCurrTime <= mDuration/3){    //50-100   A弹出，B弹出
            mPointDatas.get(0).y -= aTop/mCount;
            mPointControlls.get(0).x += aTotal/mCount;

            //B
            mPointControlls.get(6).y -= bTotal/mCount;
            mPointControlls.get(7).x -= bTotal/mCount;

                postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/3 && mCurrTime <= mDuration/2){    //100-150  A收回，B弹出，C弹出
            mPointDatas.get(0).y += aTop/mCount*3;
            mPointControlls.get(0).x -= aTotal/mCount*3;

            mPointDatas.get(3).x -= lTop/mCount;


            mPointControlls.get(5).y += cBottom/mCount;
            //B
            mPointControlls.get(6).y -= bTotal/mCount;
            mPointControlls.get(7).x -= bTotal/mCount;

            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/2 && mCurrTime <= mDuration/3 * 2){ //150-200 A收回，B收回，C弹出
            mPointDatas.get(0).y -= aTop/mCount;
            mPointControlls.get(0).x += aTotal/mCount;

            mPointDatas.get(3).x -= lTop/mCount;

            mPointControlls.get(5).y += cBottom/mCount;
            //B
            mPointControlls.get(6).y += bTotal/mCount*3;
            mPointControlls.get(7).x += bTotal/mCount*3;

            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/3 * 2 && mCurrTime <= mDuration/6 * 5) { //200-250 B收回，C收回
            mPointDatas.get(3).x += lTop/mCount*3;

            mPointControlls.get(5).y -= cBottom/mCount*3;
            //B
            mPointControlls.get(6).y -= bTotal/mCount;
            mPointControlls.get(7).x -= bTotal/mCount;
            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/6 * 5 && mCurrTime <= mDuration){        //250-300 C收回
            mPointDatas.get(3).x -= lTop/mCount;
            mPointControlls.get(5).y += cBottom/mCount;
            postInvalidateDelayed((long) mPiece);
        }else{
            //最后修正值，因为每次除存在误差
            mPointDatas.get(0).y = mCenterY - mRadious;
            mPointControlls.get(0).x = mCenterX + mMagicDistance;
            mPointDatas.get(3).x = mCenterX - mRadious;
            mPointControlls.get(5).y = mCenterY + mMagicDistance;
            mPointControlls.get(6).y = mCenterY - mMagicDistance;
            mPointControlls.get(7).x = mCenterX - mMagicDistance;
            postInvalidateDelayed((long) mPiece);
        }

    }

    private float cBottom = 200;
    private float aTop = 80;
    private float lTop = 70;
    private float aTotal = 200;
    private float bTotal = 200;

    private int mDuration = 600;
    public int mCurrTime = 0;  //当前已进行时间
    private float mCount = 100f;//将总时间划分多少块
    private float mPiece = mDuration / mCount; //每一块的时间 ；
    private static final String TAG = "MagicBezierCircle";

}
