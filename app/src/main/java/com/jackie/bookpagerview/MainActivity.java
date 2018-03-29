package com.jackie.bookpagerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.booklibrary.BookPageView;
import com.jackie.booklibrary.uitls.AnnotateUtils;
import com.jackie.booklibrary.uitls.ViewInject;

public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.view_book_page)
    private BookPageView bookPageView;
//    private BookPageView bookPageView;
    private String style;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnnotateUtils.injectViews(this,getWindow().getDecorView());
        bookPageView = (BookPageView) findViewById(R.id.view_book_page);
        bookPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        float x = event.getX();
                        float y = event.getY();
                        float width = bookPageView.getViewWidth();
                        float height = bookPageView.getViewHeight();
                        if(x<=width/3){//左
                            style = bookPageView.STYLE_LEFT;
//                            Toast.makeText(PageActivity.this,"点击了左部",Toast.LENGTH_SHORT).show();
                            bookPageView.setTouchPoint(x,y,style);

                        }else if(x>width/3 && y<=height/3){//上
                            style = bookPageView.STYLE_TOP_RIGHT;
//                            Toast.makeText(PageActivity.this,"点击了上部",Toast.LENGTH_SHORT).show();
                            bookPageView.setTouchPoint(x,y,style);

                        }else if(x>width*2/3 && y>height/3 && y<=height*2/3){//右
                            style = bookPageView.STYLE_RIGHT;
//                            Toast.makeText(PageActivity.this,"点击了右部",Toast.LENGTH_SHORT).show();
                            bookPageView.setTouchPoint(x,y,style);

                        }else if(x>width/3 && y>height*2/3){//下
                            style = bookPageView.STYLE_LOWER_RIGHT;
//                            Toast.makeText(PageActivity.this,"点击了下部",Toast.LENGTH_SHORT).show();
                            bookPageView.setTouchPoint(x,y,style);

                        }else if(x>width/3 && x<width*2/3 && y>height/3 && y<height*2/3){//中
                            style = bookPageView.STYLE_MIDDLE;
//                            Toast.makeText(PageActivity.this,"点击了中部",Toast.LENGTH_SHORT).show();
//                            bookPageView.setTouchPoint(x,y,bookPageView.STYLE_MIDDLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        bookPageView.setTouchPoint(event.getX(),event.getY(),style);
                        break;
                    case MotionEvent.ACTION_UP:
                        bookPageView.startCancelAnim();
                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        bookPageView.startCancelAnim();
//                        break;
                }
                return false;

            }
        });
    }
}
