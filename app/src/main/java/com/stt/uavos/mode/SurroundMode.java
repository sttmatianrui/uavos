package com.stt.uavos.mode;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.stt.uavos.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.mission.hotpoint.HotpointHeading;
import dji.common.mission.hotpoint.HotpointMission;
import dji.common.mission.hotpoint.HotpointStartPoint;
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
import dji.sdk.mission.timeline.actions.HotpointAction;
import dji.sdk.mission.timeline.actions.TakeOffAction;

/**
 * Created by 111112 on 2017/8/3.
 */

public class SurroundMode {

    private static final double PI = 3.1415926535897932384626;
    private static final double C_EARTH = 6378137.0;
    public double droneLocationLat = 181, droneLocationLng = 181;

    public float HoverHigh;
    public float HoverNumbers;
    public float HoverRadius;
    public float HoverTime;

    public float MoveHigh;
    public float MoveRadius;
    public float MoveSpeed;

    protected List<Waypoint> waypointList = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder;
    private MissionControl missionControl;

    public void setHoverHigh(float ptr){
        HoverHigh = ptr;
    }
    public void setHoverNumbers(float ptr){
        HoverNumbers = ptr;
    }
    public void setHoverRadius(float ptr){
        HoverRadius = ptr;
    }
    public void setHoverTime(float ptr){
        HoverTime = ptr;
    }

    public void setMoveHigh(float ptr){
        MoveHigh = ptr;
    }
    public void setMoveRadius(float ptr){
        MoveRadius = ptr;
    }
    public void setMoveSpeed(float ptr){
        MoveSpeed = ptr;
    }



    public void setHoverMode(double lat,double lng) {

        double lati,longi,optAngle;

        waypointList.clear();
        waypointMissionBuilder = new WaypointMission.Builder();

        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);

        optAngle = Math.atan2((pos.longitude - lat),(pos.latitude - lng));

        for(int i=0; i <HoverNumbers; i++){

            lati = lat + Math.toDegrees(HoverRadius * Math.cos(i * 2 *PI / HoverNumbers + optAngle)/C_EARTH);
            longi = lng + Math.toDegrees(HoverRadius * Math.sin(i * 2 *PI / HoverNumbers + optAngle)/C_EARTH);

            final Waypoint eachWaypoint = new Waypoint(lati,longi,
                    HoverHigh);
            eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, (int)HoverTime * 1000));
            waypointList.add(eachWaypoint);
            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());

        }

        waypointMissionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .autoFlightSpeed(5f)
                .maxFlightSpeed(10f)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL);
    }


    public void setMoveMode(double lat,double lng) {

        List<TimelineElement> elements = new ArrayList<>();

        missionControl = MissionControl.getInstance();
        final TimelineEvent preEvent = null;
        MissionControl.Listener listener = new MissionControl.Listener() {
            @Override
            public void onEvent(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {
                updateTimelineStatus(element, event, error);
            }
        };

        //Step 1: takeoff from the ground
        //setResultToToast("Step 1: takeoff from the ground");
        elements.add(new TakeOffAction());


        //Step 2: start a hotpoint mission
        //setResultToToast("Step 2: start a hotpoint mission to surround 360 degree");
        HotpointMission hotpointMission = new HotpointMission();
        hotpointMission.setHotpoint(new LocationCoordinate2D(lat,lng));
        hotpointMission.setAltitude(MoveHigh);
        hotpointMission.setRadius(MoveRadius);
        hotpointMission.setAngularVelocity(MoveSpeed);
        HotpointStartPoint startPoint = HotpointStartPoint.NEAREST;
        hotpointMission.setStartPoint(startPoint);
        HotpointHeading heading = HotpointHeading.TOWARDS_HOT_POINT;
        hotpointMission.setHeading(heading);
        elements.add(new HotpointAction(hotpointMission, 362));

        //Step 3: Go 10 meters from home point
        //    setResultToToast("Step 3: Go 10 meters from home point");
        //    elements.add(new GoToAction(new LocationCoordinate2D(homeLatitude, homeLongitude), 20));
        //Step 4: go back home
        //setResultToToast("Step 3: go back home");
        elements.add(new GoHomeAction());

        if (missionControl.scheduledCount() > 0) {
            missionControl.unscheduleEverything();
            missionControl.removeAllListeners();
        }

        missionControl.scheduleElements(elements);
        missionControl.addListener(listener);
    }

    private void updateTimelineStatus(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {
/*
        if (element != null) {
            if (element instanceof Mission) {
                setResultToToast((((Mission) element).getMissionObject().getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription())));
            } else {
                setResultToToast((element.getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription())));
            }
        } else {
            setResultToToast(("Timeline Event is " + event.toString() + " " + (error == null
                    ? ""
                    : "Failed:"
                    + error.getDescription())));
        }
*/
    }

    public void startTimeline() {
        if (MissionControl.getInstance().scheduledCount() > 0) {
            MissionControl.getInstance().startTimeline();
        } else {
           // ToastUtils.setResultToToast("Init the timeline first by clicking the Init button");
        }
    }

    public void stopTimeline() {
        MissionControl.getInstance().stopTimeline();
    }

    public void pauseTimeline() {
        MissionControl.getInstance().pauseTimeline();
    }


    public void resumeTimeline() {
        MissionControl.getInstance().resumeTimeline();
    }

}
