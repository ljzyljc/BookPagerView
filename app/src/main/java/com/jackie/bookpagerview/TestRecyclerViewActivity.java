//package com.jackie.bookpagerview;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.jackie.bookpagerview.view.MyRecyclerViewItem;
//
///**
// * Created by Jackie on 2018/6/25.
// */
//public class TestRecyclerViewActivity extends Activity{
//
//    private RecyclerView mRecyclerView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler);
//
//        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(new MyApr());
//
//        TextView textView;
////        textView.
//    }
//
//
//    public class MyApr extends RecyclerView.Adapter<Hodler>{
//
//        @Override
//        public Hodler onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new Hodler(LayoutInflater.from(TestRecyclerViewActivity.this).inflate(R.layout.activity_tiem, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(final Hodler holder, final int position) {
//            holder.show.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(TestRecyclerViewActivity.this, "编号："+position, Toast.LENGTH_LONG).show();
//                }
//            });
//            holder.click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(TestRecyclerViewActivity.this, "删除："+position, Toast.LENGTH_LONG).show();
//                }
//            });
//            //恢复状态
//            holder.recyclerViewItem.reset();
//        }
//
//        @Override
//        public int getItemCount() {
//            return 10;
//        }
//    }
//    public class Hodler extends RecyclerView.ViewHolder{
//
//        public TextView show;
//        public TextView click;
//        public MyRecyclerViewItem recyclerViewItem;
//
//        public Hodler(View itemView) {
//            super(itemView);
//            recyclerViewItem=(MyRecyclerViewItem) itemView.findViewById(R.id.scroll_item);
//            show=(TextView) itemView.findViewById(R.id.show);
//            click=(TextView) itemView.findViewById(R.id.click);
//        }
//    }
//}
