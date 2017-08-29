package com.stt.uavos.mode;

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

public class VerticalMode {

    public float HoverHigh;
    public float HoverInterval;
    public float HoverNumbers;
    public float HoverTime;

    public float MoveHigh;
    public float MoveInterval;
    public float MoveSpeed;


    protected List<Waypoint> waypointList = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder;


    public void setHoverHigh(float ptr){
        HoverHigh = ptr;
    }
    public void setHoverInterval(float ptr){
        HoverInterval = ptr;
    }
    public void setHoverNumbers(float ptr){
        HoverNumbers = ptr;
    }
    public void setHoverTime(float ptr){
        HoverTime = ptr;
    }

    public void setMoveHigh(float ptr){
        MoveHigh = ptr;
    }
    public void setMoveInterval(float ptr){
        MoveInterval = ptr;
    }
    public void setMoveSpeed(float ptr){
        MoveSpeed = ptr;
    }

    public void setHoverMode(double lat,double lng) {

        waypointList.clear();
        waypointMissionBuilder = new WaypointMission.Builder();

        for (int i = 0; i < HoverNumbers; i++) {
            final Waypoint eachWaypoint = new Waypoint(lat, lng, HoverHigh + HoverInterval * i);
            eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, (int) HoverTime * 1000));
            waypointList.add(eachWaypoint);
            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        }

        waypointMissionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .autoFlightSpeed(5f)
                .maxFlightSpeed(10f)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

    }
}
