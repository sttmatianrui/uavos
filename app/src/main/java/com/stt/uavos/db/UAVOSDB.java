package com.stt.uavos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stt.uavos.model.Mission;
import com.stt.uavos.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @ description:用来封装数据库操作
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class UAVOSDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "uavos_db";
    public static final int VERSION = 1;
    private static UAVOSDB uavosDB;
    private SQLiteDatabase db;
    UAVOSOpenHelper dbHelper;

    public UAVOSDB(Context context) {
        dbHelper = new UAVOSOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static UAVOSDB getInstance(Context context) {
        if(uavosDB == null) {
            uavosDB = new UAVOSDB(context);
        }
        return uavosDB;
    }

    /**
     * 任务存储
     */
    public void saveTask(Task task) {

        if(task != null) {
            ContentValues values = new ContentValues();
            values.put("task_id", task.getTaskId());
            values.put("task_name", task.getTaskName());
            values.put("task_mode", task.getTaskMode());
            values.put("probe_type", task.getProbeType());
            values.put("start_time", task.getStartTime());
            values.put("end_time", task.getEndTime());
            db.insert("task" , null, values);
        }

    }

    public void updateTaskTab(String taskName, String endTime) {
        ContentValues values = new ContentValues();
        values.put("end_time", endTime);
        db.update("task", values, "task_name = ?", new String[]{taskName});
    }

    /**
     * 查询所有数据
     */
    public List<Task> loadAllTask() {
        List<Task> taskList = new ArrayList<Task>();
        Cursor cursor = db.query("task", null,null,null,null,null,"id desc");
        if(cursor.moveToNext()) {
            do{
                Task task = new Task();
                task.setTaskId(cursor.getString(cursor.getColumnIndex("task_id")));
                task.setTaskName(cursor.getString(cursor.getColumnIndex("task_name")));
                task.setTaskMode(cursor.getString(cursor.getColumnIndex("task_mode")));
                task.setProbeType(cursor.getString(cursor.getColumnIndex("probe_type")));
                task.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
                task.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
                taskList.add(task);
            }while(cursor.moveToNext());
        }
        if(cursor != null) {
            cursor.close();
        }
        return taskList;
    }

    /**
     * 删除全部任务
     */
    public void deleteAllTask() {
        db.delete("task", null,null);
    }

    public void deleteOneTask(String taskName) {
        db.delete("task", "task_name = ?", new String[]{taskName});
    }

    /**
     * 创建当前任务的数据表
     * @param taskId
     */
    public void createTaskDataTab(String taskId) {
        String sqlTaskDataTab = dbHelper.getTaskDetailTabName(taskId);
        db.execSQL(sqlTaskDataTab);
    }

    /**
     * 任务数据存储
     * 考虑返回值为boolean，来判断数据存储时候成功！！！！
     * dataTableName就是任务列表中的任务对应的UUID
     */
    public void saveMission(String dataTableName, Mission mission) {

        if(mission != null) {

            ContentValues values = new ContentValues();
            values.put("data", mission.getData());
            values.put("date", mission.getDate());
            values.put("time", mission.getTime());
            values.put("lat", mission.getLat());
            values.put("lng", mission.getLng());
            values.put("height", mission.getHeight());
            values.put("xspeed", mission.getXspeed());
            values.put("yspeed", mission.getYspeed());
            values.put("zspeed", mission.getZspeed());

            db.insert(dataTableName, null, values);
        }

    }

    /**
     * 根据taskId查询对应的数据
     * @param taskId
     * @return
     */
    public String getTaskDataTabNameByTaskId(String taskId) {
        String taskDataName = "";
        String sqlQuery = "select task_name from task where task_id = '" + taskId + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if(cursor.moveToNext()) {
            do{
                taskDataName = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return taskDataName;
    }

    /**
     * 读取数据库数据
     */
    public List<Mission> loadMission(String tableName) {
        List<Mission> missionLists = new ArrayList<Mission>();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                Mission mission = new Mission();
                mission.setData(cursor.getString(cursor.getColumnIndex("data")));
                mission.setDate(cursor.getString(cursor.getColumnIndex("date")));
                mission.setTime(cursor.getString(cursor.getColumnIndex("time")));
                mission.setLat(cursor.getString(cursor.getColumnIndex("lat")));
                mission.setLng(cursor.getString(cursor.getColumnIndex("lng")));
                mission.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                mission.setXspeed(cursor.getString(cursor.getColumnIndex("xspeed")));
                mission.setYspeed(cursor.getString(cursor.getColumnIndex("yspeed")));
                mission.setZspeed(cursor.getString(cursor.getColumnIndex("zspeed")));

                missionLists.add(mission);
            } while (cursor.moveToNext());
        }
        if(cursor != null) {
            cursor.close();
        }
        return missionLists;
    }
}
