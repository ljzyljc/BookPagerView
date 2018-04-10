package com.jackie.bookpagerview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jackie.bookpagerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/9.
 */

public class MagicBezierCircle extends View {

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
    Matrix matrix;
    private Bitmap bitmap;
    private Paint mBitmapPaint;
    public MagicBezierCircle(Context context) {
        super(context);
        init();
    }

    public MagicBezierCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagicBezierCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mRadious = 50;
        mMagicDistance = mRadious*blackMagic;
        mStretchDistance = mRadious/2;
        initData();
    }
    private void initData(){
        mPointDatas = new ArrayList<>();
        mPointControlls = new ArrayList<>();

        mPointDatas.add(new PointF(mCenterX, mCenterY - mRadious));
        mPointDatas.add(new PointF(mCenterX + mRadious, mCenterY));
        mPointDatas.add(new PointF(mCenterX, mCenterY + mRadious));
        mPointDatas.add(new PointF(mCenterX - mRadious, mCenterY));

        mPointControlls.add(new PointF(mCenterX + mMagicDistance, mCenterY - mRadious));   //顶部右侧最高点
        mPointControlls.add(new PointF(mCenterX + mRadious, mCenterY - mMagicDistance));   //顶部右侧第二高的点

        mPointControlls.add(new PointF(mCenterX + mRadious, mCenterY + mMagicDistance));
        mPointControlls.add(new PointF(mCenterX + mMagicDistance, mCenterY + mRadious));

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
//        canvas.drawBitmap(bitmap,mCenterX-bitmap.getWidth()/2,mCenterY-bitmap.getHeight()/2,mBitmapPaint);

        mCurrTime += 20;

        if (mCurrTime <= mDuration/140 * 18){   //0-75      //A弹出
            mPointDatas.get(0).y -= 1*aTop/mCount;
            mPointControlls.get(0).x += 1*aTotal/mCount;
            mPointControlls.get(7).x -= 1*bTotal/mCount;

            postInvalidateDelayed((long) mPiece);
            Log.i(TAG, "onDraw: -----1----"+mCurrTime+"-------"+mDuration);
        }else if (mCurrTime> mDuration/140 * 18 && mCurrTime <= mDuration/140 * 36){    //75-150   A收回
            mPointDatas.get(0).y += 1*aTop/mCount;
            mPointControlls.get(0).x -= 1*aTotal/mCount;
            mPointControlls.get(7).x += 1*bTotal/mCount;
            Log.i(TAG, "onDraw: -----2----"+mCurrTime+"-------"+mDuration);
            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/140 * 36 && mCurrTime <= mDuration/140 * 54){    //150 - 225 B弹出
            //修正A
            mPointDatas.get(0).y = mCenterY - mRadious;
            mPointControlls.get(0).x = mCenterX + mMagicDistance;
            mPointControlls.get(7).x = mCenterX - mMagicDistance;

            //B
            mPointDatas.get(0).y -= 1*aTop/mCount;
            mPointControlls.get(0).x += 0.5*aTotal/mCount;
            mPointControlls.get(6).y -= 1*bTotal/mCount;
            mPointControlls.get(7).x -= 1*bTotal/mCount;

            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/140 * 54 && mCurrTime <= mDuration/140 * 72){ //225 - 300 B收回

            //B
            mPointDatas.get(0).y += 1*aTop/mCount;
            mPointControlls.get(0).x -= 0.5*aTotal/mCount;
            mPointControlls.get(6).y += 1*bTotal/mCount;
            mPointControlls.get(7).x += 1*bTotal/mCount;

            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/140 * 72 && mCurrTime <= mDuration/140*108) { //300-500 C变大---变小
            //修正B
            mPointDatas.get(0).y = mCenterY - mRadious;
            mPointControlls.get(0).x = mCenterX + mMagicDistance;
            mPointControlls.get(6).y = mCenterY - mMagicDistance;
            mPointControlls.get(7).x = mCenterX - mMagicDistance;
            if (mCurrTime> mDuration/140 * 90 && mCurrTime <= mDuration/140 * 108){
                mPointDatas.get(3).x -= bei*lTop/mCount;  //变大
                mPointControlls.get(5).y += 1*cBottom/mCount;
                mPointControlls.get(7).x -= 1*bTotal/mCount;
            }else{
                mPointDatas.get(3).x += bei*lTop/mCount;  //变小
                mPointControlls.get(5).y -= 1*cBottom/mCount;
                mPointControlls.get(7).x += 1*bTotal/mCount;
            }


//            mPointControlls.get(5).y -= cBottom/mCount;
            //B
//            mPointControlls.get(6).y += bTotal/mCount;
//            mPointControlls.get(7).x += bTotal/mCount;
            postInvalidateDelayed((long) mPiece);
        }else if (mCurrTime> mDuration/140 * 108 && mCurrTime <= mDuration){        //500-700 C收回
            if (mCurrTime> mDuration/140 * 108 && mCurrTime <= mDuration/140 * 124){
                mPointDatas.get(3).x -= bei*lTop/mCount;  //变大
                mPointControlls.get(5).y += 1*cBottom/mCount;
                mPointControlls.get(7).x -= 1*bTotal/mCount;
            }else{
                mPointDatas.get(3).x += bei*lTop/mCount;  //变小
                mPointControlls.get(5).y -= 1*cBottom/mCount;
                mPointControlls.get(7).x += 1*bTotal/mCount;
            }
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
    private float bei = 0.6f;
    private float cBottom = 200;
    private float aTop = 80;
    private float lTop = 70;
    private float aTotal = 200;
    private float bTotal = 200;

    private int mDuration = 1400;
    public int mCurrTime = 0;  //当前已进行时间
    private float mCount = 100f;//将总时间划分多少块
    private float mPiece = mDuration / mCount; //每一块的时间 ；
    private static final String TAG = "MagicBezierCircle";

}
