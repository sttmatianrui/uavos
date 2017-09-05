package com.stt.uavos.model;

import java.io.Serializable;

/**
 * @ description: 任务详情类（任务数据）
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class Mission implements Serializable{
    /**
     * 实时数据
     */
    private String data;
    /**
     * 年月日
     */
    private String date;
    /**
     * 时分秒
     */
    private String time;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 经度
     */
    private String lng;
    /**
     * 高度
     */
    private String height;
    /**
     * 速度X
     */
    private String xspeed;
    /**
     * 速度Y
     */
    private String yspeed;
    /**
     * 速度Z
     */
    private String zspeed;

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
