package com.stt.uavos.mode;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dji.common.mission.waypoint.Waypoint;

/**
 * Created by 111112 on 2017/8/3.
 */

public class SetPoint {
    //--基点
    public double BasicLat,BasicLng;
    public LatLng pos;
    //--航点列表
    public List<Waypoint> waypointList = new ArrayList<>();

    public void setBasicPoint(double lat,double lng){
        BasicLat = lat;
        BasicLng = lng;
    }


    protected void addPoint(Waypoint mywaypoint){
        waypointList.add(mywaypoint);
    }

    protected void clearPoint(){
        waypointList.clear();
    }

}
