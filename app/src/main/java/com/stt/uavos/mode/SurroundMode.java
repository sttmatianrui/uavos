package com.stt.uavos.mode;

/**
 * Created by 111112 on 2017/8/3.
 */

public class SurroundMode {
    public float HoverHigh;
    public float HoverNumbers;
    public float HoverRadius;
    public float HoverTime;

    public float MoveHigh;
    public float MoveRadius;
    public float MoveSpeed;

    protected void setHoverHigh(float ptr){
        HoverHigh = ptr;
    }
    protected void setHoverNumbers(float ptr){
        HoverNumbers = ptr;
    }
    protected void setHoverRadius(float ptr){
        HoverRadius = ptr;
    }
    protected void setHoverTime(float ptr){
        HoverTime = ptr;
    }

    protected void setMoveHigh(float ptr){
        MoveHigh = ptr;
    }
    protected void setMoveRadius(float ptr){
        MoveRadius = ptr;
    }
    protected void setMoveSpeed(float ptr){
        MoveSpeed = ptr;
    }

}
