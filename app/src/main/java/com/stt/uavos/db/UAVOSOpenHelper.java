package com.stt.uavos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ description:建表等操作
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class UAVOSOpenHelper extends SQLiteOpenHelper {

    /**
     * 任务表建表语句
     */
    private static final String CREATE_TASK = "create table if not exists task("
            + "id integer primary key autoincrement, "
            + "task_id text,"
            + "task_name text,"
            + "task_mode text, "
            + "probe_type text,"
            + "start_time text,"
            + "end_time text"
            + ")";

    /**
     * 任务详情表建表语句
     */
    public String getTaskDetailTabName(String taskId) {

        String sqlTab = "create table " + taskId
                + "( id integer primary key autoincrement, "
                + "data text, "
                + "date text, "
                + "time text, "
                + "lat text, "
                + "lng text, "
                + "height text, "
                + "yspeed text, "
                + "xspeed text, "
                + "zspeed text)";
        return sqlTab;

    }


    public UAVOSOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);//创建任务表
    }

    // 数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
