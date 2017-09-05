package com.stt.uavos.mode;

import android.content.Context;

import com.stt.uavos.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;

/**
 * Created by 111112 on 2017/8/3.
 */

public class WaypointMode {

    public float High;
    public  float Time;
    private Context context;

    public  WaypointMode(Context context) {
        this.context = context;
    }

    public List<Waypoint> waypointList = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder =  new WaypointMission.Builder();

    public void setHigh(float ptr){
        High = ptr;
    }
    public void setTime(float ptr){
        Time = ptr;
    }

    public void  addPoint(double lat,double lng){
        Waypoint mWaypoint = new Waypoint(lat, lng, 10);
        waypointList.add(mWaypoint);
        waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        ToastUtils.setResultToToast(context,"航点" + waypointList.size());
    }
    public void delatePoint(double lat,double lng){
        Waypoint mWaypoint = new Waypoint(lat, lng, High);
        waypointList.remove(mWaypoint);
    }


    public void setMode() {

        waypointMissionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .autoFlightSpeed(5f)
                .maxFlightSpeed(10f)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

        if (waypointMissionBuilder.getWaypointList().size() > 0){
            ToastUtils.setResultToToast(context,waypointMissionBuilder.getWaypointList().size()+"");
            for (int i=0; i< waypointMissionBuilder.getWaypointList().size(); i++){
                waypointMissionBuilder.getWaypointList().get(i).altitude = High;
            }

            //ToastUtils.setResultToToast("Set Waypoint attitude successfully");
        }

        //waypointList.clear();

    }


}
