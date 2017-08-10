package com.stt.uavos.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.stt.uavos.R;
import com.stt.uavos.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dji.common.mission.waypoint.Waypoint;

/**
 * Created by Administrator on 2017/8/7.
 * 地图界面
 */

public class AmapFragment extends Fragment implements View.OnClickListener, AMap.OnMapClickListener{
    private MapView mapView;
    private AMap  aMap;

    private List<Waypoint> waypointList = new ArrayList<>();
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private Marker droneMarker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_amap_fragment, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.setOnClickListener(this);

        initMapView();
        return view;
    }

    private void initMapView() {
        if(aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
        }
        LatLng beijing = new LatLng(40.112104, 116.370924);
        aMap.addMarker(new MarkerOptions().position(beijing).title("Marker inBeijing"));
        aMap.moveCamera(CameraUpdateFactory.newLatLng(beijing));
    }

    @Override
    public void onMapClick(LatLng point) {
        if (HomeActivity.getIsAdded() == true){
            markWaypoint(point);
            /*Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (HomeActivity.waypointMissionBuilder != null) {
                waypointList.add(mWaypoint);
                HomeActivity.waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
            }else
            {
                HomeActivity.waypointMissionBuilder = new WaypointMission.Builder();
                waypointList.add(mWaypoint);
                HomeActivity.waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
            }*/
        }else{
            ToastUtils.setResultToToast(getActivity(), "Cannot Add Waypoint");
        }
    }

    public void markWaypoint(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = aMap.addMarker(markerOptions);
        mMarkers.put(mMarkers.size(), marker);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    // Update the drone location based on states from MCU.
    public void updateDroneLocation(final double droneLocationLat, final double droneLocationLng) {
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(droneMarker != null) {
                    droneMarker.remove();
                }

                if(checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    droneMarker = aMap.addMarker(markerOptions);
                }
            }
        });
    }

    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }
}