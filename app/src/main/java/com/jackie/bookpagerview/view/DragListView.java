package com.jackie.bookpagerview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Jackie on 2018/4/19.
 */
public class DragListView extends ListView {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;  //item镜像的布局参数
    private int mDragPosition;
    private int mDownX;
    private int mDownY;
    private int mWidth;//整个布局的宽度
    private int mHeight;//整个布局的高度
    private static final String TAG = "DragListView";
    private boolean isDrag;
    private View mStartDragItemView;  //刚开始拖拽的item对应的view
    private Bitmap mDragBitmap;
    private ImageView mDragView;  //用于拖拽的镜像
    private Handler handler = new Handler();
    public DragListView(Context context) {
        super(context);
    }

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
    }

    public DragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    
    //移动的Runnable
    private Runnable mMoveRunnable = new Runnable() {
        @Override
        public void run() {
            isDrag = true;
            mStartDragItemView.setVisibility(View.INVISIBLE);
            //创建镜像
            createDragItem(mDragBitmap,mDownY);
        }
    };
    //滚动的Runnable
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
               
        }
    };
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                if (mDownX>mWidth/10*9) {
                    mDragPosition = pointToPosition(mDownX,mDownY);
                    Log.i(TAG, "onTouchEvent: "+mDragPosition);
                    handler.post(mMoveRunnable);
                    mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                    if (mStartDragItemView!=null){
                        mStartDragItemView.setDrawingCacheEnabled(true);
                        mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                        mStartDragItemView.destroyDrawingCache();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: ===============");
                //拖动镜像
                if (mStartDragItemView!=null) {
                    onDragItem((int) ev.getX(),(int) ev.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                mDownX = 0;
                mDownY = 0;
                handler.removeCallbacks(mMoveRunnable);
                mStartDragItemView = null;
                onStopDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                mDownX = 0;
                mDownY = 0;
                handler.removeCallbacks(mMoveRunnable);
                mStartDragItemView = null;
                onStopDrag();
                break;

        }


        return super.onTouchEvent(ev);
    }
    //开始拖拽
    private void onDragItem(int moveX,int moveY){
        //移动的时候更新镜像
        mWindowParams.y = moveY;
        mWindowManager.updateViewLayout(mDragView,mWindowParams);
        onSwapItem(moveX,moveY);
    }
    //停止拖拽
    private void onStopDrag(){
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null){
            view.setVisibility(View.VISIBLE);
        }
        //移除镜像
        if (mDragView != null){
            mWindowManager.removeView(mDragView);
        }
        if (onChangeListener != null){
            onChangeListener.onAfterChange();
        }

    }
    //交换顺序
    private void onSwapItem(int postionX,int postionY){
        int swapPosition = pointToPosition(postionX,postionY);
        if (swapPosition != mDragPosition && swapPosition != INVALID_POSITION){
            if (onChangeListener!=null){
                onChangeListener.onChange(mDragPosition,swapPosition);
            }
            mDragPosition = swapPosition;
        }
    }


    //创建拖动的镜像
    private void createDragItem(Bitmap bitmap,int y){
        mWindowParams = new WindowManager.LayoutParams();
//        mWindowParams.format = PixelFormat.;
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.alpha = 1.0f;
        mWindowParams.x = 0;
        mWindowParams.y = y;
        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mDragView = new ImageView(getContext());
        mDragView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragView,mWindowParams);
    }

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public interface OnChangeListener{
        void onChange(int from,int to);
        void onAfterChange();
    }
    
}
