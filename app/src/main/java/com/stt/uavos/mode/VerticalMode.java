package com.stt.uavos.mode;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.stt.uavos.MainActivity;
import com.stt.uavos.ui.HomeActivity;
import com.stt.uavos.utils.ToastUtils;

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
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.timeline.Mission;
import dji.sdk.mission.timeline.TimelineElement;
import dji.sdk.mission.timeline.TimelineEvent;
import dji.sdk.mission.timeline.actions.GoHomeAction;
import dji.sdk.mission.timeline.actions.GoToAction;
import dji.sdk.mission.timeline.actions.HotpointAction;
import dji.sdk.mission.timeline.actions.TakeOffAction;
import dji.sdk.mission.waypoint.WaypointMissionOperator;

/**
 * Created by 111112 on 2017/8/3.
 */

public class VerticalMode {

    public double droneLocationLat = 181, droneLocationLng = 181;

    public float HoverHigh;
    public float HoverInterval;
    public float HoverNumbers;
    public float HoverTime;

    public float MoveHigh;
    public float MoveInterval;
    public float MoveSpeed ;
    private Context context;

    public  VerticalMode(Context context) {
        this.context = context;
    }

    protected List<Waypoint> waypointList = new ArrayList<>();
    public WaypointMission.Builder waypointMissionBuilder;
    private MissionControl missionControl;

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
        ToastUtils.setResultToToast(context,"Step 1: takeoff from the ground");
        elements.add(new TakeOffAction());

        //Step 3: Go 10 meters from home point
        ToastUtils.setResultToToast(context,"Step 2: Go 10 meters from home point");

        elements.add(new GoToAction(MoveHigh));

        ToastUtils.setResultToToast(context,"Step 3: Go to target point");
        elements.add(new GoToAction(new LocationCoordinate2D(lat, lng), MoveHigh));

        ToastUtils.setResultToToast(context,"Step 4: Go 10 meters from target point with 2m/s speed");

        GoToAction goToAction = new GoToAction(new LocationCoordinate2D(lat, lng), MoveHigh + MoveInterval);
        goToAction.setFlightSpeed(2);
        //ToastUtils.setResultToToast(context,MoveSpeed+"");
        elements.add(goToAction);

        //Step 4: go back home
        ToastUtils.setResultToToast(context,"Step 3: go back home");
        elements.add(new GoHomeAction());

        if (missionControl.scheduledCount() > 0) {
            missionControl.unscheduleEverything();
            missionControl.removeAllListeners();
        }

        missionControl.scheduleElements(elements);
        missionControl.addListener(listener);
    }

    private void updateTimelineStatus(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {

        if (element != null) {
            if (element instanceof Mission) {
                ToastUtils.setResultToToast(context,(((Mission) element).getMissionObject().getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription())));
            } else {

                ToastUtils.setResultToToast(context, (element.getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription())));
                /*setResultToToast((element.getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription())));*/
            }
        } else {
            ToastUtils.setResultToToast(context, ("Timeline Event is " + event.toString() + " " + (error == null
                    ? ""
                    : "Failed:"
                    + error.getDescription())));
            /*setResultToToast(("Timeline Event is " + event.toString() + " " + (error == null
                    ? ""
                    : "Failed:"
                    + error.getDescription())));*/
        }

    }

    public void startTimeline() {
        if (MissionControl.getInstance().scheduledCount() > 0) {
            MissionControl.getInstance().startTimeline();
        } else {
            ToastUtils.setResultToToast(context,"Init the timeline first by clicking the Init button");
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
