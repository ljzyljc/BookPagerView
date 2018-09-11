package com.jackie.bookpagerview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.jackie.bookpagerview.R;

import java.util.ArrayList;

import static android.view.View.MeasureSpec.EXACTLY;

/**
 * Created by Jackie on 2018/9/6.
 * https://www.jianshu.com/p/c4601bab860a
 * 策略坐标轴
 */
public class StrategyAxisView extends View {
    private static final String TAG = "StrategyAxisView";
    private Paint mPaint;
    private Paint mLinePaint;
    private Paint mCirclePaint;
    private Paint mWhitePaint;
    private float mWidth;
    private float mHeight;

    private int count = 13;
    private float mMaxValue = 20;
    private int mMinValue = 0;
    private float mPerYLength; //平均每一份Y的长度
    private int mPerXLength; //平均每一份X
    private float[] mDateArray = {15, 17, 14, 14.5f, 17, 14.8f, 14.1f, 13, 11.5f, 7, 6, 8, 3};
    private float mWhiteRadius;
    private float mBlueRadius;
    private Path pathBlueLine;
    private Paint mPathPaint;
    private Shader mShader;
    private ValueAnimator mAnimator;
    private ValueAnimator mForeverAnimator;
    private float foreverValue;

    public void setForeverValue(float foreverValue) {
        this.foreverValue = foreverValue;
        invalidate();
    }

    public void startAnimation() {
        Log.i("jackie","----2---null");
        if (mAnimator != null) {
            Log.i("jackie","----2---");
            mAnimator.start();
        }
//        if (mForeverAnimator != null){
//            mForeverAnimator.start();
//        }
    }

    public StrategyAxisView(Context context) {
        super(context);
        init(context);
    }

    public StrategyAxisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StrategyAxisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == EXACTLY && heightSpecMode == EXACTLY) {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            Log.i(TAG, "onMeasure: -----" + mWidth + "     " + mHeight);
        }
        //每一份的长度（Y轴）
        mPerYLength = mHeight / (mMaxValue);
        //每一份的长度（X轴）
        mPerXLength = (int) (mWidth / (count - 1));
        //path相关的属性动画
        mAnimator = ObjectAnimator.ofFloat(this,"value",0f, 1f);
        mAnimator.setDuration(1000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //必须进行判断，否则最后为空，空白
                if (value < 0.99 && value > 0) {
                    invalidate();
                }
            }
        });
        //流动效果动画
        mForeverAnimator = ObjectAnimator.ofFloat(this,"foreverValue",0f,1f);
        mForeverAnimator.setDuration(1000);
        mForeverAnimator.setRepeatCount(12000);
        mShader = new LinearGradient(0, mHeight, mWidth, mHeight, Color.parseColor("#2f5dd9")
                , Color.parseColor("#3acfd5"), Shader.TileMode.CLAMP);
        initPoint();
    }
    private ArrayList<Point> pointArrayList = new ArrayList<>();
    private void initPoint(){
        if (pointArrayList != null && pointArrayList.size() >0 ){
            return;
        }
        for (int i = 0; i < mDateArray.length; i++) {
            startX = i * mPerXLength;
            startY = (mMaxValue - mDateArray[i]) * mPerYLength;
            Log.i(TAG, "initPoint: ------"+startX+"--"+startY);
            pointArrayList.add(new Point(startX,startY));
            if (i + 1 == (mDateArray.length - 1)) {
                endX = (i + 1) * mPerXLength;
                endY = (mMaxValue - mDateArray[i + 1]) * mPerYLength;
                pointArrayList.add(new Point(endX,endY));
                Log.i(TAG, "initPoint: ---2---"+endX+"--"+endY);
                break;
            }
        }

        for (int i = 0;i<pointArrayList.size();i++){
            mmP.add(new PointF(pointArrayList.get(i).x,pointArrayList.get(i).y));
        }
        Log.i(TAG, "init: -------"+mmP.size());
        calculateControlPoint(mmP);
    }
    ArrayList<PointF> mmP = new ArrayList<>();
    private void init(Context context) {
        mPaint = new Paint();
        mLinePaint = new Paint();
        mWhitePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(getResources().getDimension(R.dimen.x3));
        mLinePaint.setColor(Color.parseColor("#3acfd5"));
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.parseColor("#2D6BD9"));
        mWhiteRadius = getResources().getDimension(R.dimen.x4);
        mBlueRadius = getResources().getDimension(R.dimen.x2);
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setAntiAlias(true);
        pathBlueLine = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setColor(Color.parseColor("#3acfd5"));
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(getResources().getDimension(R.dimen.x3));
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        pathLine = new Path();

    }
    private Path pathLine;

    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;
    private float value = 0;

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ------------");
        //绘制线和圆点
//        pathLine.reset();   //必须重置才有效果，否则之前在都在
//        pathLine.moveTo(0, mHeight);
//        for (int i = 0; i < mDateArray.length; i++) {
////            Log.i(TAG, "onDraw: -----" + i);
//            startX = i * mPerXLength;
//            startY = (mMaxValue - mDateArray[i]) * mPerYLength;
//            endX = (i + 1) * mPerXLength;
//            endY = (mMaxValue - mDateArray[i + 1]) * mPerYLength;
////            Log.i(TAG, "onDraw: " + startX + "------" + startY + "------" + endX + "------" + endY);
////            canvas.drawLine(startX, startY, endX, endY, mLinePaint);
////              pathLine.lineTo(startX, startY);
//              //绘制线
//            if (i==0){
//                pathBlueLine.moveTo(startX,startY);
//                pathLine.moveTo(startX, startY);
//            }else {
//                pathBlueLine.moveTo(startX,startY);
//                pathBlueLine.quadTo((startX+endX)/3, (startY+endY)/3,endX,endY);
//                pathLine.lineTo(startX, startY);
//            }
//            //绘制圆点
////            canvas.drawCircle(startX+mBlueRadius/2,startY,mWhiteRadius,mWhitePaint);
////            canvas.drawCircle(startX+mBlueRadius/2,startY,mBlueRadius,mCirclePaint);
//            //绘制线下方的背景（渐变）
////            pathBack.lineTo(startX, startY);
//
//            if (i + 1 == (mDateArray.length - 1)) {
//                //绘制最后一个圆点
////                canvas.drawCircle((i+1) * mPerXLength+mBlueRadius/2,(mMaxValue - mDateArray[i+1]) * mPerYLength,mWhiteRadius,mWhitePaint);
////                canvas.drawCircle((i+1) * mPerXLength+mBlueRadius/2,(mMaxValue - mDateArray[i+1]) * mPerYLength,mBlueRadius,mCirclePaint);
//                pathLine.lineTo(mWidth,endY);
//                pathLine.lineTo(mWidth,mHeight);
//                pathLine.lineTo(0,mHeight);
//                pathBlueLine.lineTo(mWidth,endY);
//                pathBlueLine.lineTo(mWidth,mHeight);
////                pathBlueLine.lineTo(0,mHeight);
////                pathBlueLine.close();
////                pathLine.close();
//                Log.i(TAG, "onDraw: ------end --------------------");
//                break;
//            }
//        }
//        canvas.drawPath(pathLine,mLinePaint);
        //FIXME:--------------绘制圆滑曲线-----------------------
        pathBlueLine.reset();
        Point firstPointF = pointArrayList.get(0);
        pathBlueLine.moveTo(firstPointF.x,mHeight);
        pathBlueLine.lineTo(firstPointF.x,firstPointF.y);
        for (int i = 0;i < pointArrayList.size() * 2;i = i + 2){
            if (i > mControlPointList.size() -1){
                break;
            }
            PointF leftCon = mControlPointList.get(i);
            PointF rightCon = mControlPointList.get(i+1);
            Point rightPoint = pointArrayList.get(i/2 + 1);
            pathBlueLine.cubicTo(leftCon.x,leftCon.y,rightCon.x,rightCon.y,rightPoint.x,rightPoint.y);

        }
        PointF lastPoint = mmP.get(mmP.size()-1);
        pathBlueLine.setLastPoint(lastPoint.x,mHeight);

        canvas.drawPath(pathBlueLine,mPathPaint);
//        //FIXME:背景 path进行裁剪
//        PathMeasure pathMeasure=new PathMeasure();
//        pathMeasure.setPath(pathLine,false);
//        Path segPath=new Path();
//        float length = value*pathMeasure.getLength();
//        //float[] effectArray = new float[]{};
//        //设置流动效果
////        DashPathEffect dashPathEffect=new DashPathEffect(new float[]{3f,3f},20*foreverValue+3);
////        mLinePaint.setPathEffect(dashPathEffect);
//        pathMeasure.getSegment(0f,length,segPath,true);
//        //得到当前的path的x,y坐标
//        float[] mArrayXY = new float[]{0f,0f};
//        pathMeasure.getPosTan(length,mArrayXY,null);
//        segPath.lineTo(mArrayXY[0],mHeight);
//        segPath.lineTo(0,mHeight);
//        segPath.close();
//
//        mLinePaint.setShader(mShader);
//        //FIXME:对line进行裁剪
//        PathMeasure linePathMeasure=new PathMeasure();
//        linePathMeasure.setPath(pathBlueLine,false);
//        Path lineSegPath=new Path();
//        float lineLength = value * linePathMeasure.getLength();
////        Log.i(TAG, "onDraw: -------"+lineLength);
//        linePathMeasure.getSegment(0f,lineLength,lineSegPath,true);
//        float[] mlineArrayXY = new float[]{0f,0f};
//        pathMeasure.getPosTan(lineLength,mlineArrayXY,null);
////        Log.i(TAG, "onDraw: --------"+mlineArrayXY[0]);
//        lineSegPath.lineTo(mArrayXY[0],mHeight);
//        lineSegPath.lineTo(0,mHeight);
//        lineSegPath.close();
//
//        //绘制线
////        canvas.drawPath(pathBlueLine,mPathPaint);
//        //FIXME:---在添加动画的时候，需要保证这两个path的长度一致，才能动画一致
//        float lastValue = 0;
//        if (value > 0 && value <0.999 ){
//            canvas.drawPath(lineSegPath,mPathPaint);
//            //绘制背景
//            canvas.drawPath(segPath,mLinePaint);
//        //FIXME:绘制圆点

//            for (int i = 0;i<pointArrayList.size();i++){
//                if ((pointArrayList.get(i).getX() > lastValue && pointArrayList.get(i).getX() < mArrayXY[0] )
//                        || (pointArrayList.get(i).getX() == mArrayXY[0] )){
//                    if ( i == pointArrayList.size() - 1){
//                        canvas.drawCircle(mWidth -mBlueRadius,pointArrayList.get(i).y - mBlueRadius/2,mWhiteRadius,mWhitePaint);
//                        canvas.drawCircle(mWidth -mBlueRadius,pointArrayList.get(i).y- mBlueRadius/2,mBlueRadius,mCirclePaint);
//                        continue;
//                    }
//                    canvas.drawCircle(pointArrayList.get(i).x - mBlueRadius/2,pointArrayList.get(i).y - mBlueRadius/2,mWhiteRadius,mWhitePaint);
//                    canvas.drawCircle(pointArrayList.get(i).x - mBlueRadius/2,pointArrayList.get(i).y- mBlueRadius/2,mBlueRadius,mCirclePaint);
//
//                }
//
//            }

//        }
//        lastValue = mArrayXY[0];

//        if (value > 0) {
//
//            pathBack.lineTo(endX, endY);
//            pathBack.lineTo(endX, mHeight);
//            canvas.drawPath(pathBack, mPathPaint);
//        }
//        pathBack.close();
//        绘制渐变效果
//        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        canvas.drawPath(pathBack,mPathPaint);

//        for (int i = 0; i < mDateArray.length * value; i++) {
//        for (int i = 0; i < mDateArray.length; i++) {
//            Log.i(TAG, "onDraw: -----" + i);
//            startX = i * mPerXLength;
//            startY = (mMaxValue - mDateArray[i]) * mPerYLength;
//            endX = (i + 1) * mPerXLength;
//            endY = (mMaxValue - mDateArray[i + 1]) * mPerYLength;
//            Log.i(TAG, "onDraw: " + startX + "------" + startY + "------" + endX + "------" + endY);
//            canvas.drawLine(startX, startY, endX, endY, mLinePaint);
//            //绘制圆点
//            canvas.drawCircle(startX + mBlueRadius / 2, startY, mWhiteRadius, mWhitePaint);
//            canvas.drawCircle(startX + mBlueRadius / 2, startY, mBlueRadius, mCirclePaint);
//            //绘制线下方的背景（渐变）
////            pathBack.lineTo(startX,startY);
//
//            if (i + 1 == (mDateArray.length - 1)) {
//                //绘制最后一个圆点
////                canvas.drawCircle((i+1) * mPerXLength+mBlueRadius/2,(mMaxValue - mDateArray[i+1]) * mPerYLength,mWhiteRadius,mWhitePaint);
////                canvas.drawCircle((i+1) * mPerXLength+mBlueRadius/2,(mMaxValue - mDateArray[i+1]) * mPerYLength,mBlueRadius,mCirclePaint);
////                pathBack.lineTo(mWidth,endY);
////                pathBack.lineTo(mWidth,mHeight);
//                break;
//            }
//        }

    }
    private float SMOOTHNESS = 0.2f;  //FIXME:控制曲线平滑的点
    private ArrayList<PointF> mControlPointList = new ArrayList<>();
    private void calculateControlPoint(ArrayList<PointF> arrayList){
        mControlPointList.clear();
        Log.i(TAG, "calculateControlPoint: ---"+arrayList.size());
        for (int i = 0;i<arrayList.size();i++){
            if (i == 0){
                //第一项
                PointF nextPoint = arrayList.get(i+1);
                float controlX = arrayList.get(i).x + (nextPoint.x - arrayList.get(i).x) * SMOOTHNESS;
                float controlY = arrayList.get(i).y;
                mControlPointList.add(new PointF(controlX,controlY));
            }else if (i == arrayList.size() - 1){
                //最后一项
                PointF lastPoint = arrayList.get(i-1);
                float controlX = arrayList.get(i).x - (arrayList.get(i).x - lastPoint.x) * SMOOTHNESS;
                float controlY = arrayList.get(i).y;
                mControlPointList.add(new PointF(controlX,controlY));

            }else{
                //中间项
                PointF point = arrayList.get(i);
                PointF lastPoint = arrayList.get(i-1);
                PointF nextPoint = arrayList.get(i+1);
                float k = (nextPoint.y - lastPoint.y)/(nextPoint.x - lastPoint.x);
                float b = point.y - k * point.x;
                //添加前控制点
                float lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float lastControlY = k * lastControlX + b;
                mControlPointList.add(new PointF(lastControlX,lastControlY));
                //添加后控制点
                float nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float nextControlY = k * nextControlX + b;
                mControlPointList.add(new PointF(nextControlX,nextControlY));


            }

        }
        Log.i(TAG, "calculateControlPoint: --------"+mControlPointList.size());


    }



    class Point{
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }


}
