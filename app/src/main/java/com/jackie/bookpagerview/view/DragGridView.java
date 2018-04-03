package com.jackie.bookpagerview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * Created by Jackie on 2018/4/3.
 */

public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";
    private int mDragPosition;  //拖拽的position
    private int mDownX;
    private int mDownY;
    private int drawResponseSeconds = 1000;
    private Handler mHandler = new Handler();
    private Vibrator mVibrator;  //振动器
    private WindowManager mWindowManager;
    private boolean isDrag;     //是否拖拽
    private View mStartDragItemView; //刚开始拖拽的item对应的view
    private WindowManager.LayoutParams mWindowParams;  //item镜像的布局参数
    private Bitmap mDragBitmap;  //拖拽的item对应的Bitmap
    private int statusHeight;
    private ImageView mDragImageView;  //用于拖拽的镜像
    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop ;

    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft;

    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top;

    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left;
    public DragGridView(Context context) {
        super(context);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //振动器初始化
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusHeight = getStatusHeight(context);
    }
    //用来处理是否为长安的Runnable
    private Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            isDrag = true;
            mVibrator.vibrate(50);  //震动50毫秒
            mStartDragItemView.setVisibility(INVISIBLE);
            createDragImg(mDragBitmap,mDownX,mDownY);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                //pointToPostion是AbsListView提供的方法，可以根据坐标查找位置
                mDragPosition = pointToPosition(mDownX,mDownY);
                Log.i(TAG, "dispatchTouchEvent:   点击位置:"+mDragPosition);
                if (mDragPosition == AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }
                //使用handler延时执行mLongClickRunnable，如果手抬上去的话，没超过一秒，在action_up上移除，很6，
                // 之前都是计算时间来实现的，感觉愚蠢之极
                mHandler.postDelayed(mLongClickRunnable,drawResponseSeconds);
                //根据position获取该item对应的view
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mOffset2Left = (int) (ev.getRawX() - mDownX);
                mOffset2Top = (int) (ev.getRawY() - mDownY);
                //FIXME:滚动

                //开启mDragItemView绘图缓存
                mStartDragItemView.setDrawingCacheEnabled(true);
                //获取mDragItemView在缓存中的Bitmap对象
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                //释放绘图缓存，避免出现重复的镜像
                mStartDragItemView.destroyDrawingCache();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                break;
            default:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    //创建拖动的镜像
    private void createDragImg(Bitmap bitmap,int x,int y){
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.format = PixelFormat.TRANSLUCENT;  //图片之外其他地方透明
        mWindowParams.gravity = Gravity.TOP|Gravity.LEFT;
        mWindowParams.alpha = 0.55f;
        mWindowParams.x = x - mPoint2ItemLeft +mOffset2Left;
        mWindowParams.y = y - mPoint2ItemTop + mOffset2Top - statusHeight;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView,mWindowParams);
    }


    /**
     * 设置长按的时间，长按多久进行拖拽
     * @param drawResponseSeconds
     */
    public void setDrawResponseSeconds(int drawResponseSeconds) {
        this.drawResponseSeconds = drawResponseSeconds;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
