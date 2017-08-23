package com.stt.uavos.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.stt.uavos.R;
import com.stt.uavos.UAVOSApplication;
import com.stt.uavos.db.UAVOSDB;
import com.stt.uavos.model.Mission;
import com.stt.uavos.utils.AnalyzeUtil;
import com.stt.uavos.utils.ToastUtils;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by Administrator on 2017/7/31.
 * 主界面
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AmapFragment.ICallBack {

    private FragmentManager fm;
    private AmapFragment amapFragment;
    private VideoFragment videoFragment;
    private DataFragment dataFragment;
    private static final int POSITION_AMAP_FRAGMENT = 1;
    private static final int POSITION_VIDEO_FRAGMENT = 2;
    private static final int POSITION_DATA_FRAGMENT = 3;
    private int position = 1;//记录当前是第几个Fragment，默认为1
    private LinearLayout llMapFragment, llVideoFragment, llDataFragment;
    private TextView mTabMapTV,mTabVideoTV,mTabDataTV;
    private ImageView mTabMapIV,mTabVideoIV,mTabDataIV;

    public static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    public double droneLocationLat = 181, droneLocationLng = 181;

    private UAVOSDB uavosDB;

    /**
     * 地图界面
     */
    private TextView mDisplayDataTV;

    /**
     * 新建任务页
     */
    private LinearLayout mTaskCreateLayout;
    private Button mCreateTaskBtn;//新建任务按钮
    private EditText mNewTaskNameET;//任务名称输入框
    /**
     * 模式选择页
     */
    private RelativeLayout mTaskModeLayout;
    private Button mModeBackBtn;//模式选择页返回按钮
    private Button mVerHoverBtn;//模式：垂直悬停
    private Button mVerMoveBtn;//模式：垂直移动
    private Button mSurroundHoverBtn;//模式：定点环绕悬停
    private Button mSurroundMoveBtn;//模式：定点环绕移动
    private Button mWayPointBtn;//模式：航点飞行
    private Button mSpaceBtn;//模式：控件探测
    private Button mScanBtn;//模式：扫描模式
    private Button mFreeBtn;//模式：遥控模式
    /**
     * 垂直悬停模式参数设置
     */
    private RelativeLayout mModeVerticalHoverLayout;
    private RelativeLayout mModeVerticalMoveLayout;
    private RelativeLayout mModeSurroundHoverLayout;
    private RelativeLayout mModeSurroundMoveLayout;
    /**
     * 垂直悬停模式参数设置页返回按钮
     */
    private Button mVHBackBtn;

    /**
     * 从地图选基点，地图功能示例:添加点，获取无人机位置
     */
    /*private Button mSelectedFromMapBtn;*/
    private static boolean isAdd = false;
    private Button mGetPlaneBtn;
    private EditText mLatET;
    private EditText mLngET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }
        setContentView(R.layout.activity_home);
        // Register the broadcast receiver for receiving the device connection's changes.
        IntentFilter filter = new IntentFilter();
        filter.addAction(UAVOSApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        uavosDB = UAVOSDB.getInstance(this);
        initUI();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFlightController();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        removeListener();
        super.onDestroy();
    }

    private void initUI() {
        // Fragment
        llMapFragment = (LinearLayout) findViewById(R.id.ll_amap);
        llVideoFragment = (LinearLayout) findViewById(R.id.ll_video);
        llDataFragment = (LinearLayout) findViewById(R.id.ll_data);
        llMapFragment.setOnClickListener(this);
        llVideoFragment.setOnClickListener(this);
        llDataFragment.setOnClickListener(this);
        mTabMapTV = (TextView) findViewById(R.id.tv_tab_home_map);
        mTabVideoTV = (TextView) findViewById(R.id.tv_tab_home_video);
        mTabDataTV = (TextView) findViewById(R.id.tv_tab_home_data);
        mTabMapIV = (ImageView) findViewById(R.id.iv_tab_home_map);
        mTabVideoIV = (ImageView) findViewById(R.id.iv_tab_home_video);
        mTabDataIV = (ImageView) findViewById(R.id.iv_tab_home_data);
        // mission
        mTaskCreateLayout = (LinearLayout) findViewById(R.id.layout_task_create);
        mTaskModeLayout = (RelativeLayout) findViewById(R.id.layout_task_mode_select);
        mModeVerticalHoverLayout = (RelativeLayout) findViewById(R.id.layout_mode_vertical_hover);
        mModeVerticalMoveLayout = (RelativeLayout) findViewById(R.id.layout_mode_vertical_move);
        mModeSurroundHoverLayout = (RelativeLayout) findViewById(R.id.layout_mode_surround_hover);
        mModeSurroundMoveLayout = (RelativeLayout) findViewById(R.id.layout_mode_surround_move);
        // create task
        mCreateTaskBtn = (Button) findViewById(R.id.btn_create_task);
        mCreateTaskBtn.setOnClickListener(this);
        mNewTaskNameET = (EditText) findViewById(R.id.et_new_task_name);
        // pattern select
        mModeBackBtn = (Button) findViewById(R.id.btn_mode_back);
        mModeBackBtn.setOnClickListener(this);
        mVerHoverBtn = (Button) findViewById(R.id.btn_mode_vertical_hover);
        mVerHoverBtn.setOnClickListener(this);
        mVerMoveBtn = (Button) findViewById(R.id.btn_mode_vertical_move);
        mVerMoveBtn.setOnClickListener(this);
        mSurroundHoverBtn = (Button) findViewById(R.id.btn_mode_surround_hover);
        mSurroundHoverBtn.setOnClickListener(this);
        mSurroundMoveBtn = (Button) findViewById(R.id.btn_mode_surround_move);
        mSurroundMoveBtn.setOnClickListener(this);
        mWayPointBtn = (Button) findViewById(R.id.btn_mode_way_point);
        mWayPointBtn.setOnClickListener(this);
        mSpaceBtn = (Button) findViewById(R.id.btn_mode_space);
        mSpaceBtn.setOnClickListener(this);
        mScanBtn = (Button) findViewById(R.id.btn_mode_scan);
        mScanBtn.setOnClickListener(this);
        mFreeBtn = (Button) findViewById(R.id.btn_mode_free);
        mFreeBtn.setOnClickListener(this);

        mVHBackBtn = (Button) findViewById(R.id.btn_vh_back);
        mVHBackBtn.setOnClickListener(this);

        // 获取无人机位置
        mGetPlaneBtn = (Button) findViewById(R.id.btn_vh_get_plane);
        mGetPlaneBtn.setOnClickListener(this);
        mLatET = (EditText) findViewById(R.id.et_vh_lat);
        mLngET = (EditText) findViewById(R.id.et_vh_lng);

        mDisplayDataTV = (TextView) findViewById(R.id.tv_display_data);

        fm = getFragmentManager();
        showFragment(POSITION_AMAP_FRAGMENT);
        resetRightUI();
        mTaskCreateLayout.setVisibility(View.VISIBLE);

    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };

    private void onProductConnectionChange() {
        initFlightController();
    }

    public void initFlightController() {
        BaseProduct product = UAVOSApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {
                @Override
                public void onUpdate(@NonNull FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                    //TODO===坐标转换
                    /*GCJ02_pos = GCJ2WGS.getGCJ02Location(new LatLng(droneLocationLatW,droneLocationLngW));
                    droneLocationLat = GCJ02_pos.latitude;
                    droneLocationLng = GCJ02_pos.longitude;*/
                    amapFragment.updateDroneLocation(droneLocationLat, droneLocationLng);
                }
            });

            mFlightController.setOnboardSDKDeviceDataCallback(new FlightController.OnboardSDKDeviceDataCallback() {
                @Override
                public void onReceive(byte[] bytes) {
                    Mission mission = AnalyzeUtil.analyzeMission(new String(bytes));
                    if(mission != null) {
                        String realData = mission.getData();
                        if(!TextUtils.isEmpty(realData)) {
                            mDisplayDataTV.setText(realData);//实时数据显示
                        }
                        uavosDB.saveMission(mission);
                    }

                    //TODO===什么时候开始保存数据

                }
            });
        }
    }

    private void addListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().addListener(eventNotificationListener);
        }
    }

    private void removeListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().removeListener(eventNotificationListener);
        }
    }

    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
        @Override
        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {

        }

        @Override
        public void onUploadUpdate(WaypointMissionUploadEvent uploadEvent) {

        }

        @Override
        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {

        }

        @Override
        public void onExecutionStart() {

        }

        @Override
        public void onExecutionFinish(@Nullable final DJIError error) {
            ToastUtils.setResultToToast(HomeActivity.this, "Execution finished: " + (error == null ? "Success!" : error.getDescription()));
        }
    };

    public WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
        }
        return instance;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_amap:
                showFragment(POSITION_AMAP_FRAGMENT);
                position = POSITION_AMAP_FRAGMENT;
                break;
            case R.id.ll_video:
                showFragment(POSITION_VIDEO_FRAGMENT);
                position = POSITION_VIDEO_FRAGMENT;
                break;
            case R.id.ll_data:
                showFragment(POSITION_DATA_FRAGMENT);
                position = POSITION_DATA_FRAGMENT;
                break;

            //新建任务按钮
            case R.id.btn_create_task:
                String newTaskName = mNewTaskNameET.getText().toString();
                if (!"".equals(newTaskName)) {
                    resetRightUI();
                    mTaskModeLayout.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.setResultToToast(HomeActivity.this, "任务名称不可为空");
                    return;
                }
                break;

            //从飞行模式选择界面返回创建任务界面按钮
            case R.id.btn_mode_back:
                resetRightUI();
                mTaskCreateLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_vertical_hover://模式一：垂直悬停
                //TODO===需要变量保存当前选择的模式类型，并传递给Fragment
                resetRightUI();
                mModeVerticalHoverLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_vertical_move://模式二：垂直移动
                resetRightUI();
                mModeVerticalMoveLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_surround_hover://模式三：定点环绕悬停
                resetRightUI();
                mModeSurroundHoverLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_surround_move://模式四：定点环绕移动
                resetRightUI();
                mModeSurroundMoveLayout.setVisibility(View.VISIBLE);
                break;

            /*case R.id.btn_mode_way_point://模式五：航点飞行
                mTaskCreateLayout.setVisibility(View.GONE);
                mTaskModeLayout.setVisibility(View.GONE);
                break;

            case R.id.btn_mode_space://模式六：空间探测
                mTaskCreateLayout.setVisibility(View.GONE);
                mTaskModeLayout.setVisibility(View.GONE);
                break;

            case R.id.btn_mode_scan://模式七：扫描模式
                mTaskCreateLayout.setVisibility(View.GONE);
                mTaskModeLayout.setVisibility(View.GONE);
                break;

            case R.id.btn_mode_free://模式八：遥控模式
                mTaskCreateLayout.setVisibility(View.GONE);
                mTaskModeLayout.setVisibility(View.GONE);
                break;*/

            case R.id.btn_vh_back://从参数设置界面返回飞行模式选择界面
                mTaskCreateLayout.setVisibility(View.GONE);
                mTaskModeLayout.setVisibility(View.VISIBLE);
                mModeVerticalHoverLayout.setVisibility(View.GONE);
                break;

            case R.id.btn_vh_get_plane://地图功能示例：获取无人机位置
                if (amapFragment == null) {
                    amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                amapFragment.updateDroneLocation(droneLocationLat, droneLocationLng);
                amapFragment.cameraUpdate(droneLocationLat, droneLocationLng);
                mLatET.setText(droneLocationLat + "");
                mLngET.setText(droneLocationLng + "");
                break;
        }
    }

    public void resetRightUI() {
        mTaskCreateLayout.setVisibility(View.INVISIBLE);
        mTaskModeLayout.setVisibility(View.INVISIBLE);
        mModeVerticalHoverLayout.setVisibility(View.INVISIBLE);
        mModeVerticalMoveLayout.setVisibility(View.INVISIBLE);
        mModeSurroundHoverLayout.setVisibility(View.INVISIBLE);
        mModeSurroundMoveLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getPointFromAmapFragment(LatLng point) {
        mLatET.setText(point.latitude + "");
        mLngET.setText(point.longitude + "");
    }

    public void uploadWayPointMission() {

        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    ToastUtils.setResultToToast(HomeActivity.this, "Mission upload successfully!");
                } else {
                    ToastUtils.setResultToToast(HomeActivity.this, "Mission upload failed, error: " + error.getDescription() + " retrying...");
                    getWaypointMissionOperator().retryUploadMission(null);
                }
            }
        });

    }

    public void startWaypointMission() {

        getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                ToastUtils.setResultToToast(HomeActivity.this, "Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

    public void stopWaypointMission() {

        getWaypointMissionOperator().stopMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                ToastUtils.setResultToToast(HomeActivity.this, "Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("position", position);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        showFragment(position);
    }

    /**
     * 显示Fragment
     */
    public void showFragment(int indexFragment) {
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        resetTabFragmentBg();
        switch (indexFragment) {
            case POSITION_AMAP_FRAGMENT:
                if (amapFragment != null) {
                    ft.show(amapFragment);
                } else {
                    amapFragment = new AmapFragment();
                    ft.add(R.id.fragment_content, amapFragment);
                }
                llMapFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_click));
//                llMapFragment.setBackgroundResource(getResources().getResourceName(R.mipmap.home_fg_btn_click));
                mTabMapTV.setTextColor(getResources().getColor(R.color.home_fg_click));
                mTabMapIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_map_click));
                break;
            case POSITION_VIDEO_FRAGMENT:
                if (videoFragment != null) {
                    ft.show(videoFragment);
                } else {
                    videoFragment = new VideoFragment();
                    ft.add(R.id.fragment_content, videoFragment);
                }
                llVideoFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_click));
                mTabVideoTV.setTextColor(getResources().getColor(R.color.home_fg_click));
                mTabVideoIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_video_click));
                break;
            case POSITION_DATA_FRAGMENT:
                if (dataFragment != null) {
                    ft.show(dataFragment);
                } else {
                    dataFragment = new DataFragment();
                    ft.add(R.id.fragment_content, dataFragment);
                }
                llDataFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_click));
                mTabDataTV.setTextColor(getResources().getColor(R.color.home_fg_click));
                mTabDataIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_data_click));
                break;
        }
        ft.commit();
    }

    public void resetTabFragmentBg() {
        llMapFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_default));
        mTabMapTV.setTextColor(getResources().getColor(R.color.home_fg_default));
        mTabMapIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_map_default));
        llVideoFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_default));
        mTabVideoTV.setTextColor(getResources().getColor(R.color.home_fg_default));
        mTabVideoIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_video_default));
        llDataFragment.setBackground(getResources().getDrawable(R.mipmap.home_fg_btn_default));
        mTabDataTV.setTextColor(getResources().getColor(R.color.home_fg_default));
        mTabDataIV.setBackground(getResources().getDrawable(R.mipmap.home_fg_data_default));
    }

    /**
     * 隐藏Fragment
     */
    public void hideFragment(FragmentTransaction ft) {
        if (amapFragment != null) {
            ft.hide(amapFragment);
        }

        if (videoFragment != null) {
            ft.hide(videoFragment);
        }

        if (dataFragment != null) {
            ft.hide(dataFragment);
        }
    }

}
