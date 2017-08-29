package com.stt.uavos.mode;

import java.util.ArrayList;
import java.util.List;

import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;

/**
 * Created by 111112 on 2017/8/3.
 */

public class WaypointMode {

    public float High;
    public  float Time;

    protected List<Waypoint> waypointList = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder;

    protected void setHigh(float ptr){
        High = ptr;
    }
    protected void setTime(float ptr){
        Time = ptr;
    }




    public void setMode() {


        waypointMissionBuilder = new WaypointMission.Builder();
/*
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

        waypointList.clear();
*/
    }




}
