package com.jackie.bookpagerview.camera;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;
import com.jackie.bookpagerview.BaseActivity;
import com.jackie.bookpagerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/4/2.
 */

public class CameraGridView extends BaseActivity{
    private static final String TAG = "CameraGridView";
    @ViewInject(R.id.gridview)
    GridView gridview;
    private List<ImageBean> mListPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_gridview);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        mListPath = new ArrayList<>();
        PhotoUtils.loadImageForSDCard(this, new PhotoUtils.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                Log.i(TAG, "onSuccess: "+folders.get(0).getImages().size());
                MyGridAdapter adapter = new MyGridAdapter(CameraGridView.this,folders.get(0).getImages());
                gridview.setAdapter(adapter);
            }
        });


    }


    private class MyGridAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<ImageBean> mList;
        private Context context;
        public MyGridAdapter(Context context, List<ImageBean> mList){
            inflater = LayoutInflater.from(context);
            this.mList = mList;
            this.context = context;
        }
        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_grid,null);
                viewHolder.imageview = convertView.findViewById(R.id.imageview);
                viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageview.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));


            return convertView;
        }
    }

    class ViewHolder{
        ImageView imageview;
        CheckBox checkBox;
    }
}
