package com.stt.uavos.mode;

/**
 * Created by 111112 on 2017/8/3.
 */

public class VerticalMode {

    public float HoverHigh;
    public float HoverInterval;
    public float HoverNumbers;
    public float HoverTime;

    public float MoveHigh;
    public float MoveInterval;
    public float MoveSpeed;

    protected void setHoverHigh(float ptr){
        HoverHigh = ptr;
    }
    protected void setHoverInterval(float ptr){
        HoverInterval = ptr;
    }
    protected void setHoverNumbers(float ptr){
        HoverNumbers = ptr;
    }
    protected void setHoverTime(float ptr){
        HoverTime = ptr;
    }

    protected void setMoveHigh(float ptr){
        MoveHigh = ptr;
    }
    protected void setMoveInterval(float ptr){
        MoveInterval = ptr;
    }
    protected void setMoveSpeed(float ptr){
        MoveSpeed = ptr;
    }

}
