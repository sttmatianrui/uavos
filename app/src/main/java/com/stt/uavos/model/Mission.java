package com.stt.uavos.model;

/**
 * @ description: 任务类
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class Mission {
    private int id;
    //TODO==完善任务类个字段，并添加setter、getter方法
    private String data;
    private String date;
    private String time;
    private String lat;
    private String lng;
    private String height;
    private String xspeed;
    private String yspeed;
    private String zspeed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getXspeed() {
        return xspeed;
    }

    public void setXspeed(String xspeed) {
        this.xspeed = xspeed;
    }

    public String getYspeed() {
        return yspeed;
    }

    public void setYspeed(String yspeed) {
        this.yspeed = yspeed;
    }

    public String getZspeed() {
        return zspeed;
    }

    public void setZspeed(String zspeed) {
        this.zspeed = zspeed;
    }
}
