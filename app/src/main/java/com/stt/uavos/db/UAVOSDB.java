package com.stt.uavos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stt.uavos.model.Mission;

import java.util.ArrayList;
import java.util.List;

/**
 * @ description:用来封装数据库操作
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class UAVOSDB {
    /**数据库名
     *
     */
    public static final String DB_NAME = "uavos_db";
    public static final int VERSION = 1;
    private static UAVOSDB uavosDB;
    private SQLiteDatabase db;

    public UAVOSDB(Context context) {
        UAVOSOpenHelper dbHelper = new UAVOSOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static UAVOSDB getInstance(Context context) {
        if(uavosDB == null) {
            uavosDB = new UAVOSDB(context);
        }
        return uavosDB;
    }

    /**
     * 任务数据存储
     * 考虑返回值为boolean，来判断数据存储时候成功！！！！
     */
    public void saveMission(Mission mission) {
        //TODO==保存数据需要验证是否已经创建任务表？？？
        if(mission != null) {
            ContentValues values = new ContentValues();
            //TODO===保存任务数据
            values.put("mission_data", mission.getData());
            //values.put(""， mission.getName//属性值);

            db.insert("Missoin", null, values);
        }
    }

    /**
     * 读取数据库数据
     */
    public List<Mission> loadMission() {
        List<Mission> missionLists = new ArrayList<Mission>();
        Cursor cursor = db.query("Mission", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                Mission mission = new Mission();
                //TODO====获取查询的任务数据
                mission.setData(cursor.getString(cursor.getColumnIndex("mission_data")));//

                missionLists.add(mission);
            } while (cursor.moveToNext());
        }
        if(cursor != null) {
            cursor.close();
        }
        return missionLists;
    }
}
