package com.stt.uavos.mode;

import com.amap.api.maps2d.model.LatLng;
import com.stt.uavos.MainActivity;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.sdk.mission.waypoint.WaypointMissionOperator;

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
    private MainActivity mainActivity;

    public VerticalMode(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    protected List<Waypoint> waypointList = new ArrayList<>();
    protected WaypointMission.Builder waypointMissionBuilder;
    private WaypointMissionOperator waypointMissionOperator;

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

    public void setHoverMode(double lat,double lng){

        waypointList.clear();
        waypointMissionBuilder = new WaypointMission.Builder();

        for(int i=0; i < HoverNumbers; i++){
            final Waypoint eachWaypoint = new Waypoint(lat,lng, HoverHigh + HoverInterval * i);
            eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, (int)HoverTime * 1000));
            waypointList.add(eachWaypoint);
            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        }

        waypointMissionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .autoFlightSpeed(5f)
                .maxFlightSpeed(10f)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

        //DJIError error = mainActivity.getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
        //if (error == null) {
        //    setResultToToast("loadWaypoint succeeded");
        //} else {
        //    setResultToToast("loadWaypoint failed " + error.getDescription());
        //}
    }
}
