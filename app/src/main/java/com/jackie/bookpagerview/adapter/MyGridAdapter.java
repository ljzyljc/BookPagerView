package com.jackie.bookpagerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.camera.CameraViewPagerActivity;
import com.jackie.bookpagerview.camera.ImageBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jackie on 2018/4/3.
 */

public class MyGridAdapter extends BaseAdapter {
    private static final String TAG = "MyGridAdapter";
    private LayoutInflater inflater;
    private ArrayList<ImageBean> mList;
    private Context context;
    private ArrayList<ImageBean> mChooseList;
    public MyGridAdapter(Context context, ArrayList<ImageBean> mList){
        inflater = LayoutInflater.from(context);
        this.mList = mList;
        this.context = context;
        mChooseList = new ArrayList<>();
    }

    public ArrayList<ImageBean> getmChooseList() {
        return mChooseList;
    }

    //是否含有
    private boolean isContainsImg(ImageBean imageBean){
        if (mChooseList.size() == 0){
            return false;
        }
        for (int i = 0;i<mChooseList.size();i++){
            if (imageBean.getPath().equals(mChooseList.get(i).getPath())){
                return true;
            }
        }
        return false;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        Glide.with(context).load(new File(mList.get(position).getPath())).into(viewHolder.imageview);
        viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CameraViewPagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",mList);
                bundle.putString("position", String.valueOf(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.i(TAG, "onCheckedChanged: ----true");
                    //如果选中后，不包含的时候，就添加进去
                    mChooseList.add(mList.get(position));
                }else{
                    Log.i(TAG, "onCheckedChanged: ------false");
                    if (isContainsImg(mList.get(position))){
                        Log.i(TAG, "onCheckedChanged: ----remove-------");
                        mChooseList.remove(mList.get(position));
                    }
                }
            }
        });

        return convertView;
    }
}

    class ViewHolder{
        ImageView imageview;
        CheckBox checkBox;
    }
