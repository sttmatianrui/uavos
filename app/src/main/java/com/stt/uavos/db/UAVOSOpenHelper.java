package com.stt.uavos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stt.uavos.model.Mission;

/**
 * @ description:建表等操作
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class UAVOSOpenHelper extends SQLiteOpenHelper {

    /**
     * 任务数据表
     */
    //TODO====完善建表语句
    public static final String CREATE_MISSION = "create table Mission("
            + "id integer primary key autoincrement"
            + "mission_data text"
            + ")";

    public UAVOSOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_MISSION);//创建任务表
    }

    // 数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 创建任务表
     * @param db 数据库
     * @param tableName 表名
     */
    public void createMission(SQLiteDatabase db, String tableName) {
        String mission_table = "create table if not exists" + tableName + "( id integer primary key autoincrement, data text, time text， lat text, lng text, height text, xspeed text, yspeed text. zspeed text)";
        db.execSQL(mission_table);
    }

    /**
     * 插入数据
     * @param db 数据库
     * @param tableName 表名
     * @param mission 数据
     */
    public void insertMission(SQLiteDatabase db, String tableName, Mission mission) {
        ContentValues values = new ContentValues();
        values.put("data", mission.getData());
        //TODO===
        db.insert(tableName, null, values);
    }

    /**
     * 删除某一任务
     * @param db
     * @param tableName 需要删除的表
     */
    public void deleteTable(SQLiteDatabase db, String tableName) {

    }

    /**
     * 查询某一任务的数据
     * @param db
     * @param tableName 表名
     */
    public void queryMission(SQLiteDatabase db, String tableName) {

    }
}
