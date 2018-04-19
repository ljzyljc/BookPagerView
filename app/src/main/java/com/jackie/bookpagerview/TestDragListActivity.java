package com.jackie.bookpagerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.jackie.bookpagerview.view.DragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jackie on 2018/4/19.
 */
public class TestDragListActivity extends BaseActivity {
    private List<String> mList;
    private ArrayAdapter adapter;
    private DragListView mDragListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drag);
        mList = new ArrayList<>();
        mDragListView = findViewById(R.id.mDragListView);
        for (int i=0;i<20;i++){
            mList.add("当前选项是："+i);
        }
        adapter = new ArrayAdapter(this,R.layout.item_adapter_draglist,mList);
        mDragListView.setAdapter(adapter);
        mDragListView.setOnChangeListener(new DragListView.OnChangeListener() {
            @Override
            public void onChange(int from, int to) {
                if (from == mList.size() || to == mList.size()){
                    return ;
                }
                //一个个往前推移，或者往后
                if (from < to){
                    for (int i= from;i<to;i++) {
                        Collections.swap(mList, i, i + 1);
                    }
                }else if (from > to){
                    for (int i = from;i>to;i--){
                        Collections.swap(mList,i,i-1);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAfterChange() {
                adapter.notifyDataSetChanged();
            }
        });
    }

}
