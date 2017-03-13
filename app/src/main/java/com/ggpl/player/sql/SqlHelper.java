package com.ggpl.player.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangxiaoming on 2017/2/15.
 * 数据管理类
 */

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "player.db";
    private static final int DATABASE_VERSION = 1;

    public static final String[] TABLE_NAME = new String[]{"player","filedir","module"};


    public SqlHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME[0]+
                " (playerid integer primary key autoincrement, path varchar(50), wm varchar(50),title varchar(50), size varchar(50),time varchar(50),dir varchar(50),logtime varchar(50))");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME[1]+
                " (dirid integer primary key autoincrement, dirname varchar(50),ishiden varchar(5),logtime varchar(50))");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME[2]+
                " (mid integer primary key autoincrement, imgurl varchar(50),h5url varchar(50),ishiden varchar(5),name varchar(50),desc varchar(50),logtime varchar(50))");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
