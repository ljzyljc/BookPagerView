package com.jackie.bookpagerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jackie.bookpagerview.R;
import com.jackie.bookpagerview.adapter.LeftMenuAdapter;
import com.jackie.bookpagerview.bean.MenuItem;

/**
 * Created by Jackie on 2018/4/17.
 */

public class LeftMenuFragment extends ListFragment {
    private static final int SIZE_MENU_ITEM = 3;

    private MenuItem[] mItems = new MenuItem[SIZE_MENU_ITEM];

    private LeftMenuAdapter mAdapter;
    private LayoutInflater mInflater;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MenuItem menuItem;
        mInflater = LayoutInflater.from(getActivity());
        for (int i=0;i<SIZE_MENU_ITEM;i++){
            menuItem = new MenuItem("测试--"+i,false,R.mipmap.composer_icn_plus,
                    R.mipmap.composer_icn_plus);
            mItems[i] = menuItem;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mMenuItemSelectedListener!=null){
            mMenuItemSelectedListener.menuItemSelected(((MenuItem) getListAdapter().getItem(position)).text);
        }
        mAdapter.setSelected(position);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(0xffffffff);
        setListAdapter(mAdapter = new LeftMenuAdapter(getActivity(), mItems));

    }

    //选择回调的接口
    public interface OnMenuItemSelectedListener {
        void menuItemSelected(String title);
    }
    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }
}
