package com.jackie.bookpagerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.bean.MenuItem;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by Jackie on 2018/4/17.
 */

public class LeftMenuAdapter extends ArrayAdapter<MenuItem> {

    private LayoutInflater mInflater;

    private int mSelected;


    public LeftMenuAdapter(Context context, MenuItem[] objects) {
        super(context, -1, objects);

        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_left_menu, parent, false);
        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.id_item_icon);
        TextView title = (TextView) convertView.findViewById(R.id.id_item_title);
        title.setText(getItem(position).text);
        iv.setImageResource(getItem(position).icon);
        convertView.setBackgroundColor(Color.TRANSPARENT);

        if (position == mSelected) {
            iv.setImageResource(getItem(position).iconSelected);
            convertView.setBackgroundColor(Color.parseColor("#00ff00"));
        }

        return convertView;
    }

    public void setSelected(int position) {
        this.mSelected = position;
        notifyDataSetChanged();
    }
}
