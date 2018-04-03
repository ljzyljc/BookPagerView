package com.jackie.bookpagerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.camera.CameraGridView;
import com.jackie.bookpagerview.camera.CameraViewPagerActivity;
import com.jackie.bookpagerview.camera.ImageBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jackie on 2018/4/3.
 */

public class DragAdapter extends BaseAdapter{
    private static final String TAG = "MyGridAdapter";
    private LayoutInflater inflater;
    private ArrayList<ImageBean> mList;
    private Context context;
    public DragAdapter(Context context, ArrayList<ImageBean> mList){
        inflater = LayoutInflater.from(context);
        this.mList = mList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : 10;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_main,null);
            viewHolder.imageview = convertView.findViewById(R.id.imageview);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(new File(mList.get(position).getPath())).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.personal_add).into(viewHolder.imageview);
//        viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //如果是点击了最后一个，即加号
//                if (position == mList.size()-1){
//                    Intent intent = new Intent(context,CameraGridView.class);
//                    ((Activity)context).startActivityForResult(intent,1000);
//                    return;
//                }
//                Intent intent = new Intent(context,CameraViewPagerActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("list",mList);
//                bundle.putString("position", String.valueOf(position));
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
        viewHolder.checkBox.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder{
        ImageView imageview;
        CheckBox checkBox;
    }
}
