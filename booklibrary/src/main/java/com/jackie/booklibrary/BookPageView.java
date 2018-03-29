package com.jackie.booklibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.jackie.booklibrary.bean.MyPoint;


/**
 * Created by Jackie on 2018/3/28.
 */

public class BookPageView extends View{
    private Paint pointPaint;//绘制各标识点的画笔
    private Paint bgPaint;//背景画笔
    private Paint mAPaint; //A区域画笔
    private Path mAPath;
    private Paint mBPaint; //B区域画笔
    private Path mBPath;
    private Paint mCPaint;  //C区域画笔
    private Path mCPath;
    private MyPoint a,f,g,e,h,c,j,b,k,d,i;

    private Bitmap bitmap; //缓存bitmap
    private Canvas bitmapCanvas;

    private int defaultWidth;//默认宽度
    private int defaultHeight;//默认高度
    private int viewWidth;
    private int viewHeight;
    public static final String STYLE_LEFT = "STYLE_LEFT";//点击左边区域
    public static final String STYLE_RIGHT = "STYLE_RIGHT";//点击右边区域
    public static final String STYLE_MIDDLE = "STYLE_MIDDLE";//点击中间区域
    public static final String STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT";//f点在右上角
    public static final String STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT";//f点在右下角
    private Scroller scroller;
    private String style;
    private Paint textPaint;//绘制文字画笔
    private Paint pathCContentPaint;//绘制C区域内容画笔
    public BookPageView(Context context) {
        super(context);
        init(context);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
//        defaultWidth = 600;
//        defaultHeight = 1000;
//
//        viewWidth = defaultWidth;
//        viewHeight = defaultHeight;

        a = new MyPoint();
        f = new MyPoint();
        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();
//        calcPointsXY(a,f);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setTextSize(25);

        bgPaint = new Paint();
        bgPaint.setColor(Color.GREEN);
        mAPaint = new Paint();
        mAPaint.setAntiAlias(true);
        mAPaint.setColor(Color.GREEN);
        mAPath = new Path();

        mCPaint = new Paint();
        mCPaint.setColor(Color.YELLOW);
        mCPaint.setAntiAlias(true);//设置抗锯齿
        mCPath = new Path();
        mCPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));  //取交集

        mBPaint = new Paint();
        mBPaint.setColor(Color.BLUE);
        mBPaint.setAntiAlias(true);//设置抗锯齿
//        mBPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)); //在最底层
        mBPath = new Path();

        //文本textPaint初始化
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setSubpixelText(true);//设置自像素。如果该项为true，将有助于文本在LCD屏幕上的显示效果。
        textPaint.setTextSize(30);

        pathCContentPaint = new Paint();
        pathCContentPaint.setColor(Color.YELLOW);
        pathCContentPaint.setAntiAlias(true);//设置抗锯齿

        scroller = new Scroller(context,new LinearInterpolator());  //以常量速率滑动即可

    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            float x = scroller.getCurrX();
            float y = scroller.getCurrY();
            if (style.equals(STYLE_TOP_RIGHT)){
                setTouchPoint(x,y,STYLE_TOP_RIGHT);
            }else{
                setTouchPoint(x,y,STYLE_LOWER_RIGHT);
            }
            if (scroller.getFinalX() == x && scroller.getFinalY() == y){
                setDefaultPath();
            }

        }

    }
    //取消翻页动画，计算滑动位置与时间
    public void startCancelAnim(){
        int dx,dy;
        //让a滑动到f点所在位置，留出1像素是为了防止当a和f重叠时出现View闪烁的情况
        if (style.equals(STYLE_TOP_RIGHT)){
            dx = (int) (viewWidth - 1 -a.x);
            dy = (int) (1- a.y);
        }else{
            dx = (int) (viewWidth - 1 -a.x);
            dy = (int) (viewHeight - 1 - a.y);
        }

        scroller.startScroll((int)a.x,(int)a.y,dx,dy,400);    //dx.dy为滑动的距离
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);

        viewWidth = width;
        viewHeight = height;
        a.x = -1;
        a.y = -1;
    }
    private int measureSize(int defaultSize,int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bitmap = Bitmap.createBitmap((int)viewWidth,(int)viewHeight, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        if (a.x == -1 && a.y == -1){
//            bitmapCanvas.drawPath(getPathDefault(),mAPaint);
            drawPathAContent(bitmapCanvas,getPathDefault(),mAPaint);
        }else {
            if (f.x == viewWidth && f.y == 0) {
//                bitmapCanvas.drawPath(getPathAFromTopRight(), mAPaint);
                drawPathAContent(bitmapCanvas,getPathAFromTopRight(),mAPaint);
                bitmapCanvas.drawPath(getPathC(),mCPaint);
                drawPathCContent(bitmapCanvas,getPathAFromTopRight(),pathCContentPaint);
                drawPathBContent(bitmapCanvas,getPathAFromTopRight(),mBPaint);
            }else if (f.x == viewWidth && f.y == viewHeight){
//                bitmapCanvas.drawPath(getPathAFromLowerRight(),mAPaint);
                drawPathAContent(bitmapCanvas,getPathAFromLowerRight(),mAPaint);
                bitmapCanvas.drawPath(getPathC(),mCPaint);
                drawPathCContent(bitmapCanvas,getPathAFromTopRight(),pathCContentPaint);
                drawPathBContent(bitmapCanvas,getPathAFromLowerRight(),mBPaint);
            }
//            bitmapCanvas.drawPath(getPathC(),mCPaint);
//            bitmapCanvas.drawPath(getPathB(),mBPaint);
        }
        canvas.drawBitmap(bitmap,0,0,null);

//        //绘制基本点
        drawMyPointAndBackGround(canvas);
    }
    /**
     * 绘制C区域内容
     * @param canvas
     * @param pathA
     * @param pathPaint
     */
    private void drawPathCContent(Canvas canvas, Path pathA, Paint pathPaint){
        Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(getPathB(),pathPaint);//绘制一个背景，path用B的就行
        contentCanvas.drawText("这是在A区域的内容...AAAA", viewWidth-260, viewHeight-100, textPaint);

        //结束绘制区域内的内容...

        canvas.save();
        canvas.clipPath(pathA);
//        canvas.clipPath(getPathC(), Region.Op.REVERSE_DIFFERENCE);//裁剪出C区域不同于A区域的部分
        canvas.clipPath(getPathC(),Region.Op.UNION);//裁剪出A和C区域的全集
        canvas.clipPath(getPathC(), Region.Op.INTERSECT);//取与C区域的交集

        float eh = (float) Math.hypot(f.x - e.x,h.y - f.y);
        float sin0 = (f.x - e.x) / eh;
        float cos0 = (h.y - f.y) / eh;
        //设置翻转和旋转矩阵
        float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };
        mMatrixArray[0] = -(1-2 * sin0 * sin0);
        mMatrixArray[1] = 2 * sin0 * cos0;
        mMatrixArray[3] = 2 * sin0 * cos0;
        mMatrixArray[4] = 1 - 2 * sin0 * sin0;

        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);//翻转和旋转
        mMatrix.preTranslate(-e.x, -e.y);//沿当前XY轴负方向位移得到 矩形A₃B₃C₃D₃
        mMatrix.postTranslate(e.x, e.y);//沿原XY轴方向位移得到 矩形A4 B4 C4 D4

        canvas.drawBitmap(contentBitmap, mMatrix, null);
        canvas.restore();
    }

    /**
     * 绘制A区域内容
     * @param canvas
     * @param pathA
     * @param pathPaint
     */
   private void drawPathAContent(Canvas canvas,Path pathA,Paint pathPaint){
       Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
       Canvas contentCanvas = new Canvas(contentBitmap);
       contentCanvas.drawPath(pathA,pathPaint);
       contentCanvas.drawText("这是在A区域的内容...AAAA", viewWidth-260, viewHeight-100, textPaint);

       canvas.save();
       canvas.clipPath(pathA, Region.Op.INTERSECT);
       canvas.drawBitmap(contentBitmap,0,0,null);
       canvas.restore();
   }

   private void drawPathBContent(Canvas canvas,Path pathA,Paint pathPaint){
       Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
       Canvas contentCanvas = new Canvas(contentBitmap);
       contentCanvas.drawPath(getPathB(),pathPaint);
       contentCanvas.drawText("这是在B区域的内容...AAAA", viewWidth-260, viewHeight-100, textPaint);

       canvas.save();
       canvas.clipPath(pathA);
       canvas.clipPath(getPathC(),Region.Op.UNION);  //裁剪出A和C区域的全集
       canvas.clipPath(getPathB(),Region.Op.REVERSE_DIFFERENCE); //裁剪出B区域中不同于AC区域的部分
       canvas.drawBitmap(contentBitmap,0,0,null);
       canvas.restore();
   }


    /**
     * 获取f点在右下角的pathA
     * @return
     */
    private Path getPathAFromLowerRight(){
        mAPath.reset();
        mAPath.lineTo(0,viewHeight);   //移动到左下角
        mAPath.lineTo(c.x,c.y);        //C点
        mAPath.quadTo(e.x,e.y,b.x,b.y);//从C到b画贝塞尔曲线，控制点为e
        mAPath.lineTo(a.x,a.y);        //移动到a点
        mAPath.lineTo(k.x,k.y);        //移动到k点
        mAPath.quadTo(h.x,h.y,j.x,j.y);//从k到j画贝塞尔曲线，控制点为h
        mAPath.lineTo(viewWidth,0);    //移动到右上角
        mAPath.close(); //闭合区域
        return mAPath;
    }

    /**
     * 获取f点在右上角的pathA
     * @return
     */
    private Path getPathAFromTopRight(){
        mAPath.reset();
        mAPath.lineTo(c.x,c.y);//移动到c点
        mAPath.quadTo(e.x,e.y,b.x,b.y);//从c到b画贝塞尔曲线，控制点为e
        mAPath.lineTo(a.x,a.y);//移动到a点
        mAPath.lineTo(k.x,k.y);//移动到k点
        mAPath.quadTo(h.x,h.y,j.x,j.y);//从k到j画贝塞尔曲线，控制点为h
        mAPath.lineTo(viewWidth,viewHeight);//移动到右下角
        mAPath.lineTo(0, viewHeight);//移动到左下角
        mAPath.close();
        return mAPath;
    }

    /**
     * 绘制区域C
     * @return
     */
    private Path getPathC(){
        mCPath.reset();
        mCPath.moveTo(i.x,i.y);//移动到i点
        mCPath.lineTo(d.x,d.y);//移动到d点
        mCPath.lineTo(b.x,b.y);//移动到b点
        mCPath.lineTo(a.x,a.y);//移动到a点
        mCPath.lineTo(k.x,k.y);//移动到k点
        mCPath.close();//闭合区域
        return mCPath;
    }
    /**
     * 绘制区域B
     * @return
     */
    private Path getPathB(){
        mBPath.reset();
        mBPath.lineTo(0, viewHeight);//移动到左下角
        mBPath.lineTo(viewWidth,viewHeight);//移动到右下角
        mBPath.lineTo(viewWidth,0);//移动到右上角
        mBPath.close();//闭合区域
        return mBPath;
    }



    private void drawMyPointAndBackGround(Canvas canvas){
        //为了看清楚点与View的位置关系绘制一个背景
//        canvas.drawRect(0,0,viewWidth,viewHeight,bgPaint);
        //绘制各标识点
//        canvas.drawText("a",a.x,a.y,pointPaint);
//        canvas.drawText("f",f.x,f.y,pointPaint);
//        canvas.drawText("g",g.x,g.y,pointPaint);
//
//        canvas.drawText("e",e.x,e.y,pointPaint);
//        canvas.drawText("h",h.x,h.y,pointPaint);
//
//        canvas.drawText("c",c.x,c.y,pointPaint);
//        canvas.drawText("j",j.x,j.y,pointPaint);
//
//        canvas.drawText("b",b.x,b.y,pointPaint);
//        canvas.drawText("k",k.x,k.y,pointPaint);
//
//        canvas.drawText("d",d.x,d.y,pointPaint);
//        canvas.drawText("i",i.x,i.y,pointPaint);
    }

    /**
     * 计算各点坐标
     * @param a
     * @param f
     */
    private void calcPointsXY(MyPoint a, MyPoint f){
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a,e,c,j);
        k = getIntersectionPoint(a,h,c,j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;
    }

    /**
     * 计算两线段相交点坐标
     * @param lineOne_My_pointOne
     * @param lineOne_My_pointTwo
     * @param lineTwo_My_pointOne
     * @param lineTwo_My_pointTwo
     * @return 返回该点
     */
    private MyPoint getIntersectionPoint(MyPoint lineOne_My_pointOne, MyPoint lineOne_My_pointTwo, MyPoint lineTwo_My_pointOne, MyPoint lineTwo_My_pointTwo){
        float x1,y1,x2,y2,x3,y3,x4,y4;
        x1 = lineOne_My_pointOne.x;
        y1 = lineOne_My_pointOne.y;
        x2 = lineOne_My_pointTwo.x;
        y2 = lineOne_My_pointTwo.y;
        x3 = lineTwo_My_pointOne.x;
        y3 = lineTwo_My_pointOne.y;
        x4 = lineTwo_My_pointTwo.x;
        y4 = lineTwo_My_pointTwo.y;

        float pointX =((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY =((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return  new MyPoint(pointX,pointY);
    }


    /**
     * 设置触摸点
     * @param x
     * @param y
     * @param style
     */
    public void setTouchPoint(float x, float y, String style){
        MyPoint touchPoint = new MyPoint(x,y);
        a.x = x;
        a.y = y;
        this.style = style;
        switch (style){
            case STYLE_TOP_RIGHT:
                f.x = viewWidth;
                f.y = 0;
                calcPointsXY(a,f);
                touchPoint = new MyPoint(x,y);
                if(calcPointCX(touchPoint,f)<0){//如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint();
                    calcPointsXY(a,f);
                }
                postInvalidate();
                break;
            case STYLE_LEFT:
            case STYLE_RIGHT:
                a.y = viewHeight-1;
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a,f);
                postInvalidate();
                break;
            case STYLE_LOWER_RIGHT:
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a,f);
                touchPoint = new MyPoint(x,y);
                if(calcPointCX(touchPoint,f)<0){//如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint();
                    calcPointsXY(a,f);
                }
                postInvalidate();
                break;
            default:
                break;
        }
    }

    /**
     * 回到默认状态
     */
    public void setDefaultPath(){
        a.x = -1;
        a.y = -1;
        postInvalidate();
    }

    /**
     * 绘制默认的界面
     * @return
     */
    private Path getPathDefault(){
        mAPath.reset();
        mAPath.lineTo(0, viewHeight);
        mAPath.lineTo(viewWidth,viewHeight);
        mAPath.lineTo(viewWidth,0);
        mAPath.close();
        return mAPath;
    }

    public float getViewWidth(){
        return viewWidth;
    }

    public float getViewHeight(){
        return viewHeight;
    }
    /**
     * 计算C点的X值
     * @param a
     * @param f
     * @return
     */
    private float calcPointCX(MyPoint a, MyPoint f){
        MyPoint g,e;
        g = new MyPoint();
        e = new MyPoint();
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        return e.x - (f.x - e.x) / 2;
    }
   /**
    * 如果c点x坐标小于0,根据触摸点重新测量a点坐标
    */
    private void calcPointAByTouchPoint(){
        float w0 = viewWidth - c.x;

        float w1 = Math.abs(f.x - a.x);
        float w2 = viewWidth * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

}
