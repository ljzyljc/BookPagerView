package com.jackie.bookpagerview.camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        Bundle bundle = getIntent().getExtras();
        imageBeanList = (ArrayList<ImageBean>) bundle.getSerializable("list");
        mCurrrentPostion = Integer.valueOf(bundle.getString("position"));
        Log.i(TAG, "onCreate: "+imageBeanList.size()+"-----"+mCurrrentPostion);
        ImageAdapter adapter = new ImageAdapter(this);
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
            //此处移除掉最后一个是为了不加载那个加号
            return imageBeanList == null ? 0 : imageBeanList.size()-1;
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
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView iv = new ImageView(context);
//            Glide.with(context).load(new File(imageBeanList.get(position).getPath())).into(iv);
            PhotoView iv = new PhotoView(context);
            Glide.with(context).load(new File(imageBeanList.get(position).getPath())).into(iv);
            container.addView(iv);
            return iv;
        }
    }

}
