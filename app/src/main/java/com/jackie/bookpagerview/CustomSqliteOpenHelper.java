package com.jackie.bookpagerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Jackie on 2018/3/27.
 */

public class CustomSqliteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "CustomSqliteOpenHelper";
    private static final String DATEBASE_NAME = "book_store.db";  //数据库名字
    private static final int DATEBASE_VERSION = 4;                //数据库版本号
    //创建数据库表
    private static final String CREATE_TABLE = "create table bookStore (id integer primary key autoincrement,book_name text,author text,price real)";

    public CustomSqliteOpenHelper(Context context){
        this(context,DATEBASE_NAME,null,DATEBASE_VERSION);
        Log.i(TAG, "CustomSqliteOpenHelper: -----");
    }

    public CustomSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate: ----创建数据库--");
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "onUpgrade: ------------执行了更新---------");
        sqLiteDatabase.execSQL("ALTER TABLE bookStore ADD className varchar(20)");
    }
    ContentValues values;
    public void insertDatebase(SQLiteDatabase sqLiteDatabase){
        String sql = "insert into bookStore(book_name,author) values ('霍乱时期的爱情','未知')";
        //--------------------------------第二张写法----------------开启事务，insert------------------------
//        sqLiteDatabase.beginTransaction();   //开启事务
//        for(int i=0;i<10000;i++) {
//        //简单写法
//        sqLiteDatabase.execSQL("insert into bookStore(book_name,author) values ('霍乱时期的爱情','未知')");
//            //安卓自己的写法
////            values = new ContentValues();
////            values.put("book_name", "霍乱时期的爱情");
////            values.put("author", "未知");
//            sqLiteDatabase.insert("bookStore", null, values);
//        }
//        sqLiteDatabase.endTransaction();
        //--------------------------------------------第三种写法------------------------------------- best way
        //使用SQLiteStatement
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();
        for(int i=0;i<10;i++) {
            statement.executeInsert();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

}
