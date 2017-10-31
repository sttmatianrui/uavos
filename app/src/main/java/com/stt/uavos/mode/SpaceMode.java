package com.stt.uavos.mode;

import android.content.Context;
import android.support.annotation.Nullable;

import com.amap.api.maps2d.model.LatLng;
import com.stt.uavos.utils.ToastUtils;

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
import dji.common.model.LocationCoordinate2D;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.timeline.Mission;
import dji.sdk.mission.timeline.TimelineElement;
import dji.sdk.mission.timeline.TimelineEvent;
import dji.sdk.mission.timeline.actions.GoHomeAction;
import dji.sdk.mission.timeline.actions.GoToAction;
import dji.sdk.mission.timeline.actions.TakeOffAction;

/**
 * Created by 111112 on 2017/8/3.
 */

public class SpaceMode {

    public float High;
    public float Interval;
    public float Numbers;
    public float Time;
    private Context context;
    public  SpaceMode(Context context) {
        this.context = context;
    }

    protected List<Waypoint> waypointList = new ArrayList<>();
    protected List<Waypoint> waypoints = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder =  new WaypointMission.Builder();
    private MissionControl missionControl;

    public void setHigh(float ptr){
        High = ptr;
    }
    public void setInterval(float ptr){
        Interval = ptr;
    }
    public void setNumbers(float ptr){
        Numbers = ptr;
    }
    public void setTime(float ptr){
        Time = ptr;
    }

    public void  addPoint(double lat,double lng){
        Waypoint mWaypoint = new Waypoint(lat, lng, 10);
        waypointList.add(mWaypoint);
        //waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        ToastUtils.setResultToToast(context,"航点" + waypointList.size());
    }
    public void  addPoint(LatLng pos){
        Waypoint mWaypoint = new Waypoint(pos.latitude, pos.longitude, 10);
        waypointList.add(mWaypoint);
        //waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
        ToastUtils.setResultToToast(context,"航点" + waypointList.size());
    }

    public void delatePoint(double lat,double lng){
        Waypoint mWaypoint = new Waypoint(lat, lng, High);
        waypointList.remove(mWaypoint);
    }

    public void setMode() {

        waypoints.clear();

        waypointMissionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .autoFlightSpeed(5f)
                .maxFlightSpeed(10f)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

        for (int i = 0;i < waypointList.size();i++){
            for (int j = 0;j < Numbers; j++){
                final Waypoint eachWaypoint =new Waypoint(waypointList.get(i).coordinate.getLatitude(),
                                                        waypointList.get(i).coordinate.getLongitude() ,
                                                        High - Interval * j);
                //eachWaypoint.altitude = High - Interval * j;
                eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, (int) Time * 1000));
                waypoints.add(eachWaypoint);
                waypointMissionBuilder.waypointList(waypoints).waypointCount(waypoints.size());
                //waypointMissionBuilder.getWaypointList().get(i).altitude = High - Interval * j;
            }
            final Waypoint eachWaypoint =new Waypoint(waypointList.get(i).coordinate.getLatitude(),
                    waypointList.get(i).coordinate.getLongitude() ,
                    High);
            waypoints.add(eachWaypoint);
            waypointMissionBuilder.waypointList(waypoints).waypointCount(waypoints.size());
        }

        //waypoints.clear();

    }


}
