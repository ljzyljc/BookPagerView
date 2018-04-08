package com.jackie.bookpagerview.camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.TestPopActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/3.
 */

public class CameraViewPagerActivity extends BaseActivity {
    private static final String TAG = "CameraViewPagerActivity";
    @ViewInject(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<ImageBean> imageBeanList;
    private int mCurrrentPostion;
    private PopupWindow popWindow;
    ImageAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        Bundle bundle = getIntent().getExtras();
        imageBeanList = (ArrayList<ImageBean>) bundle.getSerializable("list");
        mCurrrentPostion = Integer.valueOf(bundle.getString("position"));
        Log.i(TAG, "onCreate: "+imageBeanList.size()+"-----"+mCurrrentPostion);
        adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrrentPostion);

    }


    class ImageAdapter extends PagerAdapter{
        private Context context;
        public ImageAdapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return imageBeanList == null ? 0 : imageBeanList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            ImageView iv = new ImageView(context);
//            Glide.with(context).load(new File(imageBeanList.get(position).getPath())).into(iv);
            PhotoView iv = new PhotoView(context);
            Glide.with(context).load(new File(imageBeanList.get(position).getPath())).into(iv);
            container.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: -------------");
                    showPopwindow(position);
                }
            });
            return iv;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
    private void showPopwindow(final int position) {
//        View parent = this.findViewById(R.id.linear_main);
        final View popView = LayoutInflater.from(CameraViewPagerActivity.this).inflate(R.layout.activity_popup, null);
        Button btn_camera_pop_cancel = (Button)popView.findViewById(R.id.btn_camera_pop_cancel);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        btn_camera_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ------1-----"+imageBeanList.size());
                if (position == imageBeanList.size()-1) {
                    viewPager.setCurrentItem(position-1);
                }else{
                    viewPager.setCurrentItem(position+1);
                }
                imageBeanList.remove(position);
                Log.i(TAG, "onClick: ------2-----"+imageBeanList.size());
                adapter.notifyDataSetChanged();
                Toast.makeText(CameraViewPagerActivity.this,"删除了"+position,Toast.LENGTH_SHORT).show();
                if (popWindow!=null) {
                    popWindow.dismiss();
                }
            }
        });

        popWindow = new PopupWindow(popView,width,height);
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(true);// 设置同意在外点击消失
        popWindow.setHeight(120);
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//        popWindow.showAsDropDown(findViewById(android.R.id.content));
    }
}
