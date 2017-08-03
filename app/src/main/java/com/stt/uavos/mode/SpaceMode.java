package com.stt.uavos.mode;

/**
 * Created by 111112 on 2017/8/3.
 */

public class SpaceMode {

    public float High;
    public float Interval;
    public float Numbers;
    public float Time;

    protected void setsp_high(float ptr){
        High = ptr;
    }
    protected void setsp_interval(float ptr){
        Interval = ptr;
    }
    protected void setsp_numbers(float ptr){
        Numbers = ptr;
    }
    protected void setsp_time(float ptr){
        Time = ptr;
    }
}
