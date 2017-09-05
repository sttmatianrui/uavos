package com.stt.uavos.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.stt.uavos.R;
import com.stt.uavos.UAVOSApplication;
import com.stt.uavos.mode.SetMission;
import com.stt.uavos.mode.SetPoint;
import com.stt.uavos.mode.SurroundMode;
import com.stt.uavos.mode.VerticalMode;
import com.stt.uavos.mode.WaypointMode;
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

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AmapFragment.ICallBack{
    private static String TAG = "HomeActivity";

    private FragmentManager fm;
    private AmapFragment amapFragment;
    private VideoFragment videoFragment;
    private DataFragment dataFragment;
    private static final int POSITION_AMAP_FRAGMENT = 1;
    private static final int POSITION_VIDEO_FRAGMENT = 2;
    private static final int POSITION_DATA_FRAGMENT = 3;
    private int position = 1;//记录当前是第几个Fragment，默认为1
    private LinearLayout llMapFragment,llVideoFragment,llDataFragment;

    private TextView mTabMapTV,mTabVideoTV,mTabDataTV;
    private ImageView mTabMapIV,mTabVideoIV,mTabDataIV;

    public static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    public double droneLocationLat = 181, droneLocationLng = 181;

    /**
     * 地图界面
     */
    private TextView mDisplayDataTV;

    /** 新建任务页 */
    private LinearLayout mTaskCreateLayout;
    private Button mCreateTaskBtn;//新建任务按钮
    private EditText mNewTaskNameET;//任务名称输入框
    /** 模式选择页 */
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

    private RelativeLayout mModeVerticalHoverLayout;
    private RelativeLayout mModeVerticalMoveLayout;
    private RelativeLayout mModeSurroundHoverLayout;
    private RelativeLayout mModeSurroundMoveLayout;
    private RelativeLayout mModeWaypointLayout;
    /** 垂直悬停模式 */
    private Button mVHBackBtn;//垂直悬停返回按钮
    private Button mVHSetPointBtn;
    private Button mVHGenerateRouteBtn;
    private Button mVHTakeOff;
    private EditText mVHStartHeightET;
    private EditText mVHHighInterval;
    private EditText mVHMonitorPoints;
    private EditText mVHSingleMonitorTime;

    private Button mVHGetPlaneBtn;
    private EditText mVHLatET;
    private EditText mVHLngET;
    /** 垂直移动模式 */
    private Button mVMBackBtn;//垂直移动返回按钮
    private Button mVMSetPointBtn;
    private Button mVMGenerateRouteBtn;
    private Button mVMTakeOff;

    private  EditText mVMStartHeightET;
    private  EditText mVMMonitorHeight;
    private  EditText mVMMonitorSpeed;

    private Button mVMGetPlaneBtn;
    private EditText mVMLatET;
    private EditText mVMLngET;

    /** 环绕悬停模式 */
    private Button mSHBackBtn;//环绕移动返回按钮
    private Button mSHSetPointBtn;
    private Button mSHGenerateRouteBtn;
    private Button mSHTakeOff;
    private EditText mSHStartHeightET;
    private EditText mSHSurroundRadius;
    private EditText mSHMonitorPoints;
    private EditText mSHSingleMonitorTime;

    private Button mSHGetPlaneBtn;
    private EditText mSHLatET;
    private EditText mSHLngET;
    /** 环绕移动模式 */
    private Button mSMBackBtn;//环绕移动返回按钮
    private Button mSMSetPointBtn;
    private Button mSMGenerateRouteBtn;
    private Button mSMTakeOff;
    private EditText mSMStartHeightET;
    private EditText mSMSurroundRadius;
    private EditText mSMMonitorSpeed;

    private Button mSMGetPlaneBtn;
    private EditText mSMLatET;
    private EditText mSMLngET;
    /** 航点模式 */
    private Button mWPBackBtn;//环绕移动返回按钮
    private Button mWPSetPointBtn;
    private Button mWPGenerateRouteBtn;
    private Button mWPTakeOff;
    private EditText mWPStartHeightET;
    private EditText mWPTime;

    private Button mWPGetPlaneBtn;
    private EditText mWPLatET;
    private EditText mWPLngET;



    //-----
    private SetPoint mSetPoint = new SetPoint();
    private SetMission mSetMission = SetMission.FREE_MODE;
    private VerticalMode mVerticalMode = new VerticalMode(this);
    private SurroundMode mSurroundMode = new SurroundMode(this);
    private WaypointMode mWaypointMode = new WaypointMode(this);


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

        initUI();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFlightController();
        initDataTransmission();
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
        mModeWaypointLayout = (RelativeLayout) findViewById(R.id.layout_mode_waypoint);
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

        // 获取无人机位置
        mVHGetPlaneBtn = (Button) findViewById(R.id.btn_vh_get_plane);
        mVHGetPlaneBtn.setOnClickListener(this);
        mVHLatET = (EditText) findViewById(R.id.et_vh_lat);
        mVHLngET = (EditText) findViewById(R.id.et_vh_lng);

        mVMGetPlaneBtn = (Button) findViewById(R.id.btn_vm_get_plane);
        mVMGetPlaneBtn.setOnClickListener(this);
        mVMLatET = (EditText) findViewById(R.id.et_vm_lat);
        mVMLngET = (EditText) findViewById(R.id.et_vm_lng);

        mSHGetPlaneBtn = (Button) findViewById(R.id.btn_sh_get_plane);
        mSHGetPlaneBtn.setOnClickListener(this);
        mSHLatET = (EditText) findViewById(R.id.et_sh_lat);
        mSHLngET = (EditText) findViewById(R.id.et_sh_lng);

        mSMGetPlaneBtn = (Button) findViewById(R.id.btn_sm_get_plane);
        mSMGetPlaneBtn.setOnClickListener(this);
        mSMLatET = (EditText) findViewById(R.id.et_sm_lat);
        mSMLngET = (EditText) findViewById(R.id.et_sm_lng);

        mWPGetPlaneBtn = (Button) findViewById(R.id.btn_wp_get_plane);
        mWPGetPlaneBtn.setOnClickListener(this);
        mWPLatET = (EditText) findViewById(R.id.et_wp_lat);
        mWPLngET = (EditText) findViewById(R.id.et_wp_lng);
        //--垂直分布模式悬停
        mVHBackBtn = (Button) findViewById(R.id.btn_vh_back);
        mVHBackBtn.setOnClickListener(this);
        mVHSetPointBtn = (Button) findViewById(R.id.btn_vh_set_point);
        mVHSetPointBtn.setOnClickListener(this);
        mVHGenerateRouteBtn = (Button) findViewById(R.id.btn_vh_generate_route);
        mVHGenerateRouteBtn.setOnClickListener(this);
        mVHStartHeightET = (EditText) findViewById(R.id.et_vh_start_height);
        mVHHighInterval = (EditText) findViewById(R.id.et_vh_height_interval);
        mVHMonitorPoints = (EditText) findViewById(R.id.et_vh_monitor_points);
        mVHSingleMonitorTime = (EditText) findViewById(R.id.et_vh_single_monitor_time);
        mVHTakeOff = (Button) findViewById(R.id.btn_vh_take_off);
        mVHTakeOff.setOnClickListener(this);
        //--

        //--垂直分布模式移动
        mVMBackBtn = (Button) findViewById(R.id.btn_vm_back);
        mVMBackBtn.setOnClickListener(this);
        mVMSetPointBtn = (Button) findViewById(R.id.btn_vm_set_point);
        mVMSetPointBtn.setOnClickListener(this);
        mVMGenerateRouteBtn = (Button) findViewById(R.id.btn_vm_generate_route);
        mVMGenerateRouteBtn.setOnClickListener(this);

        mVMStartHeightET = (EditText)findViewById(R.id.et_vm_start_height);
        mVMMonitorHeight = (EditText) findViewById(R.id.et_vm_monitor_height);
        mVMMonitorSpeed = (EditText) findViewById(R.id.et_vm_monitor_speed);

        mVMTakeOff = (Button) findViewById(R.id.btn_vm_take_off);
        mVMTakeOff.setOnClickListener(this);
        //--

        //--定点环绕模式悬停
        mSHBackBtn = (Button) findViewById(R.id.btn_sh_back);
        mSHBackBtn.setOnClickListener(this);
        mSHSetPointBtn = (Button) findViewById(R.id.btn_sh_set_point);
        mSHSetPointBtn.setOnClickListener(this);
        mSHGenerateRouteBtn = (Button) findViewById(R.id.btn_sh_generate_route);
        mSHGenerateRouteBtn.setOnClickListener(this);

        mSHStartHeightET = (EditText) findViewById(R.id.et_sh_start_height);
        mSHSurroundRadius = (EditText) findViewById(R.id.et_sh_surround_radius);
        mSHMonitorPoints = (EditText) findViewById(R.id.btn_sh_monitor_points);
        mSHSingleMonitorTime = (EditText) findViewById(R.id.et_sh_single_monitor_time);

        mSHTakeOff = (Button) findViewById(R.id.btn_sh_take_off);
        mSHTakeOff.setOnClickListener(this);

        //--

        //-定点环绕模式移动
        mSMBackBtn = (Button) findViewById(R.id.btn_sm_back);
        mSMBackBtn.setOnClickListener(this);
        mSMSetPointBtn = (Button) findViewById(R.id.btn_sm_set_point);
        mSMSetPointBtn.setOnClickListener(this);
        mSMGenerateRouteBtn = (Button) findViewById(R.id.btn_sm_generate_route);
        mSMGenerateRouteBtn.setOnClickListener(this);

        mSMStartHeightET = (EditText)findViewById(R.id.et_sm_start_height);
        mSMSurroundRadius = (EditText) findViewById(R.id.et_sm_surround_radius);
        mSMMonitorSpeed = (EditText) findViewById(R.id.btn_sm_monitor_speed);

        mSMTakeOff = (Button) findViewById(R.id.btn_sm_take_off);
        mSMTakeOff.setOnClickListener(this);
        //--
        //--航点模式
        mWPBackBtn = (Button) findViewById(R.id.btn_wp_back);
        mWPBackBtn.setOnClickListener(this);
        mWPSetPointBtn = (Button) findViewById(R.id.btn_wp_set_point);
        mWPSetPointBtn.setOnClickListener(this);
        mWPGenerateRouteBtn = (Button) findViewById(R.id.btn_wp_generate_route);
        mWPGenerateRouteBtn.setOnClickListener(this);

        mWPStartHeightET = (EditText) findViewById(R.id.et_wp_start_height);
        mWPTime = (EditText) findViewById(R.id.et_wp_time);

        //mWPMonitorPoints = (EditText) findViewById(R.id.btn_wp_monitor_points);
        //mWPSingleMonitorTime = (EditText) findViewById(R.id.et_wp_single_monitor_time);

        mWPTakeOff = (Button) findViewById(R.id.btn_wp_take_off);
        mWPTakeOff.setOnClickListener(this);

        //--

//        mSetMission = new SetMission();

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

    private void onProductConnectionChange()
    {
        initFlightController();
        initDataTransmission();
    }

    public void initFlightController() {
        BaseProduct product = UAVOSApplication.getProductInstance();
        if(product != null && product.isConnected()) {
            if(product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if(mFlightController != null) {
            mFlightController.setStateCallback(new FlightControllerState.Callback() {
                @Override
                public void onUpdate(@NonNull FlightControllerState djiFlightControllerCurrentState) {
                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                    mSurroundMode.droneLocationLat = droneLocationLat;
                    mSurroundMode.droneLocationLng = droneLocationLng;
                    //TODO=== 坐标转换
                    /*GCJ02_pos = GCJ2WGS.getGCJ02Location(new LatLng(droneLocationLatW,droneLocationLngW));
                    droneLocationLat = GCJ02_pos.latitude;
                    droneLocationLng = GCJ02_pos.longitude;*/

                    amapFragment.updateDroneLocation(droneLocationLat, droneLocationLng);
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
                if(!"".equals(newTaskName)) {
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
                mSetMission = SetMission.VERTICAL_HOVER_MODE;
                resetRightUI();
                mModeVerticalHoverLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_vertical_move://模式二：垂直移动
                mSetMission = SetMission.VERTICAL_MOVE_MODE;
                resetRightUI();
                mModeVerticalMoveLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_surround_hover://模式三：定点环绕悬停
                mSetMission = SetMission.SURROUND_HOVER_MODE;
                resetRightUI();
                mModeSurroundHoverLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_surround_move://模式四：定点环绕移动
                mSetMission = SetMission.SURROUND_MOVE_MODE;
                resetRightUI();
                mModeSurroundMoveLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_mode_way_point://模式五：航点飞行
                mSetMission = SetMission.WAYPOINT_MODE;
                resetRightUI();
                mModeWaypointLayout.setVisibility(View.VISIBLE);
                break;
            /*
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
                resetRightUI();
                mTaskModeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_vm_back://从参数设置界面返回飞行模式选择界面
                resetRightUI();
                mTaskModeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_sh_back://从参数设置界面返回飞行模式选择界面
                resetRightUI();
                mTaskModeLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_sm_back://从参数设置界面返回飞行模式选择界面
                resetRightUI();
                mTaskModeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_wp_back://从参数设置界面返回飞行模式选择界面
                resetRightUI();
                mTaskModeLayout.setVisibility(View.VISIBLE);
                break;
            //垂直分布模式悬停----------------------------------------------------------------------
            case R.id.btn_vh_set_point:
                //TODO===  把基点保存  并在地图做标注
                mSetPoint.setBasicPoint(Double.parseDouble(mVHLatET.getText().toString()),Double.parseDouble(mVHLngET.getText().toString()));
                Log.e(TAG, "保存基点");
                ToastUtils.setResultToToast(this,"基点设置完毕");
                break;
            case R.id.btn_vh_generate_route:
                //TODO===  保存飞行参数信息;生成航线并在地图展示
                switch (mSetMission) {
                    case VERTICAL_HOVER_MODE:
                        if(mVHStartHeightET.getText().toString()!= null)
                         mVerticalMode.setHoverHigh(Float.parseFloat(mVHStartHeightET.getText().toString()));
                        mVerticalMode.setHoverInterval(Float.parseFloat(mVHHighInterval.getText().toString()));
                        mVerticalMode.setHoverNumbers(Float.parseFloat(mVHMonitorPoints.getText().toString()));
                        mVerticalMode.setHoverTime(Float.parseFloat(mVHSingleMonitorTime.getText().toString()));

                        mVerticalMode.setHoverMode(mSetPoint.BasicLat,mSetPoint.BasicLng);
                        Log.e(TAG, "生成航线");
                        loadWayPointMission(mVerticalMode.waypointMissionBuilder);
                        uploadWayPointMission();
                        break;
                }

                break;
            case R.id.btn_vh_take_off:
                startWaypointMission();
                break;
            //垂直分布模式移动----------------------------------------------------------------------
            case R.id.btn_vm_set_point:
                //TODO===  把基点保存  并在地图做标注
                mSetPoint.setBasicPoint(Double.parseDouble(mVHLatET.getText().toString()),Double.parseDouble(mVHLngET.getText().toString()));
                Log.e(TAG, "保存基点");
                ToastUtils.setResultToToast(this,"基点设置完毕");
                break;
            case R.id.btn_vm_generate_route:
                //TODO===  保存飞行参数信息;生成航线并在地图展示
                switch (mSetMission) {
                    case VERTICAL_MOVE_MODE:
                        if(mVHStartHeightET.getText().toString()!= null)
                            mVerticalMode.setMoveHigh(Float.parseFloat(mVMStartHeightET.getText().toString()));
                        mVerticalMode.setMoveInterval(Float.parseFloat(mVMMonitorHeight.getText().toString()));
                        mVerticalMode.setMoveSpeed(Float.parseFloat(mVMMonitorSpeed.getText().toString()));


                        mVerticalMode.setMoveMode(mSetPoint.BasicLat,mSetPoint.BasicLng);
                        Log.e(TAG, "生成航线");

                        break;
                }

                break;
            case R.id.btn_vm_take_off:
                mVerticalMode.startTimeline();
                break;
            //定点环绕分布模式悬停------------------------------------------------------------------
            case R.id.btn_sh_set_point:
                //TODO===  把基点保存  并在地图做标注
                mSetPoint.setBasicPoint(Double.parseDouble(mSHLatET.getText().toString()),Double.parseDouble(mSHLngET.getText().toString()));
                Log.e(TAG, "保存基点");
                ToastUtils.setResultToToast(this,"基点设置完毕");
                break;
            case R.id.btn_sh_generate_route:
                //TODO===  保存飞行参数信息;生成航线并在地图展示
                ToastUtils.setResultToToast(this,"生成航线");
                switch (mSetMission) {
                    case SURROUND_HOVER_MODE:
                        //ToastUtils.setResultToToast(this,"设置");
                        if(mSHStartHeightET.getText().toString()!= null)
                            mSurroundMode.setHoverHigh(Float.parseFloat(mSHStartHeightET.getText().toString()));
                        if(mSHSurroundRadius.getText().toString()!= null)
                            mSurroundMode.setHoverRadius(Float.parseFloat(mSHSurroundRadius.getText().toString()));
                        if(mSHMonitorPoints.getText().toString()!= null)
                            mSurroundMode.setHoverNumbers(Float.parseFloat(mSHMonitorPoints.getText().toString()));
                        if(mSHSingleMonitorTime.getText().toString()!= null)
                            mSurroundMode.setHoverTime(Float.parseFloat(mSHSingleMonitorTime.getText().toString()));

                        mSurroundMode.setHoverMode(mSetPoint.BasicLat,mSetPoint.BasicLng);
                        Log.e(TAG, "生成航线");
                        loadWayPointMission(mSurroundMode.waypointMissionBuilder);
                        uploadWayPointMission();
                        break;
                }

                break;
            case R.id.btn_sh_take_off:
               startWaypointMission();
                break;
            //--------------------------------------------------------------------------------------
            //定点环绕分布模式移动------------------------------------------------------------------
            case R.id.btn_sm_set_point:
                //TODO===  把基点保存  并在地图做标注
                mSetPoint.setBasicPoint(Double.parseDouble(mSMLatET.getText().toString()),Double.parseDouble(mSMLngET.getText().toString()));
                Log.e(TAG, "保存基点");
                ToastUtils.setResultToToast(this,"基点设置完毕");
                //aMap.addMarker(new MarkerOptions().position().title(task_name));
                break;
            case R.id.btn_sm_generate_route:
                //TODO===  保存飞行参数信息;生成航线并在地图展示
                ToastUtils.setResultToToast(this,"btn_sm_generate_route");
                switch (mSetMission) {
                    case SURROUND_MOVE_MODE:
                        if(mSMStartHeightET.getText().toString()!= null)
                            mSurroundMode.setMoveHigh(Float.parseFloat(mSMStartHeightET.getText().toString()));
                        if(mSMSurroundRadius.getText().toString()!= null)
                            mSurroundMode.setMoveRadius(Float.parseFloat(mSMSurroundRadius.getText().toString()));
                        if(mSMMonitorSpeed.getText().toString()!= null)
                            mSurroundMode.setMoveSpeed(Float.parseFloat(mSMMonitorSpeed.getText().toString()));

                        mSurroundMode.setMoveMode(mSetPoint.BasicLat,mSetPoint.BasicLng);
                        Log.e(TAG, "生成航线");

                        //loadWayPointMission(mSurroundMode.waypointMissionBuilder);
                        //uploadWayPointMission();
                        break;
                }

                break;
            case R.id.btn_sm_take_off:
                ToastUtils.setResultToToast(this,"起飞");
                mSurroundMode.startTimeline();
                break;
            //航点模式------------------------------------------------------------------
            case R.id.btn_wp_set_point:
                //TODO===  把基点保存  并在地图做标注
                //mSetPoint.setBasicPoint(Double.parseDouble(mSHLatET.getText().toString()),Double.parseDouble(mSHLngET.getText().toString()));
                mWaypointMode.addPoint(Double.parseDouble(mSHLatET.getText().toString()),Double.parseDouble(mSHLngET.getText().toString()));
                Log.e(TAG, "保存航点");
                //ToastUtils.setResultToToast(this,"航点设置完毕");
                break;
            case R.id.btn_wp_generate_route:
                //TODO===  保存飞行参数信息;生成航线并在地图展示
                ToastUtils.setResultToToast(this,"生成航线");
                switch (mSetMission) {
                    case WAYPOINT_MODE:
                        //ToastUtils.setResultToToast(this,"设置");
                        if(mWPStartHeightET.getText().toString()!= null)
                            mWaypointMode.setHigh(Float.parseFloat(mWPStartHeightET.getText().toString()));
                        //ToastUtils.setResultToToast(this,"飞行高度"+mWPStartHeightET.getText());
                        if(mWPTime.getText().toString()!= null)
                            mWaypointMode.setTime(Float.parseFloat(mWPTime.getText().toString()));
                        //ToastUtils.setResultToToast(this,"飞行时间"+mWPTime.getText());

                        mWaypointMode.setMode();
                        Log.e(TAG, "生成航线");
                        loadWayPointMission(mWaypointMode.waypointMissionBuilder);
                        uploadWayPointMission();
                        break;
                }

                break;
            case R.id.btn_wp_take_off:
                ToastUtils.setResultToToast(this,"起飞");
                startWaypointMission();
                break;
            //--------------------------------------------------------------------------------------
            case R.id.btn_vh_get_plane://地图功能示例：获取无人机位置
                if(amapFragment == null) {
                    AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                //amapFragment.
                mVHLatET.setText(droneLocationLat + "");
                mVHLngET.setText(droneLocationLng + "");
                break;
            case R.id.btn_vm_get_plane://地图功能示例：获取无人机位置
                if(amapFragment == null) {
                    AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                //amapFragment.
                mVMLatET.setText(droneLocationLat + "");
                mVMLngET.setText(droneLocationLng + "");
                break;
            case R.id.btn_sh_get_plane://地图功能示例：获取无人机位置
                if(amapFragment == null) {
                    AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                //amapFragment.
                mSHLatET.setText(droneLocationLat + "");
                mSHLngET.setText(droneLocationLng + "");
                break;
            case R.id.btn_sm_get_plane://地图功能示例：获取无人机位置
                if(amapFragment == null) {
                    AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                //amapFragment.
                mSMLatET.setText(droneLocationLat + "");
                mSMLngET.setText(droneLocationLng + "");
                break;
            case R.id.btn_wp_get_plane://地图功能示例：获取无人机位置
                if(amapFragment == null) {
                    AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                }
                //TODO=== 通过amapFragment可调用其中定义的方法
                //amapFragment.
                mWPLatET.setText(droneLocationLat + "");
                mWPLngET.setText(droneLocationLng + "");
                break;
        }
    }

    /**
     * 隐藏所有设置信息界面
     */
    public void resetRightUI() {
        mTaskCreateLayout.setVisibility(View.INVISIBLE);
        mTaskModeLayout.setVisibility(View.INVISIBLE);
        mModeVerticalHoverLayout.setVisibility(View.INVISIBLE);
        mModeVerticalMoveLayout.setVisibility(View.INVISIBLE);
        mModeSurroundHoverLayout.setVisibility(View.INVISIBLE);
        mModeSurroundMoveLayout.setVisibility(View.INVISIBLE);
        mModeWaypointLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getPointFromAmapFragment(LatLng point) {
        mVHLatET.setText(point.latitude + "");
        mVHLngET.setText(point.longitude + "");
        mVMLatET.setText(point.latitude + "");
        mVMLngET.setText(point.longitude + "");
        mSHLatET.setText(point.latitude + "");
        mSHLngET.setText(point.longitude + "");
        mSMLatET.setText(point.latitude + "");
        mSMLngET.setText(point.longitude + "");
        mWPLatET.setText(point.latitude + "");
        mWPLngET.setText(point.longitude + "");
    }

    //----------------------------------------------------------------------------------------------
    private void setResultToToast(final String string){
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HomeActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadWayPointMission(WaypointMission.Builder builder){
        DJIError error = getWaypointMissionOperator().loadMission(builder.build());
        if (error == null) {
            setResultToToast("loadWaypoint succeeded");
        } else {
            setResultToToast("loadWaypoint failed " + error.getDescription());
        }
    }
    //----------------------------------------------------------------------------------------------

    public void uploadWayPointMission(){

        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    ToastUtils.setResultToToast(HomeActivity.this,"Mission upload successfully!");
                } else {
                    ToastUtils.setResultToToast(HomeActivity.this,"Mission upload failed, error: " + error.getDescription() + " retrying...");
                    getWaypointMissionOperator().retryUploadMission(null);
                }
            }
        });

    }

    public void startWaypointMission(){

        getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                ToastUtils.setResultToToast(HomeActivity.this,"Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

    public void stopWaypointMission(){

        getWaypointMissionOperator().stopMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                ToastUtils.setResultToToast(HomeActivity.this,"Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
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

    public Double getDroneLocationLat() {
        return droneLocationLat;
    }

    public Double getDroneLocationLng() {
        return droneLocationLng;
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

    //----------------------------------------------------------------------------------------------
    //--
    private void initDataTransmission(){
        BaseProduct product = UAVOSApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        //setResultToToast("DataTransmission init");

        if (mFlightController != null) {
            mFlightController.setOnboardSDKDeviceDataCallback(new FlightController.OnboardSDKDeviceDataCallback() {
                @Override
                public void onReceive(byte[] bytes) {
                    String str = new String(bytes);
                    //--数据处理回调接口，bytes中是传回来的数据
                    //--这里做好数据解析，存储及显示
                    String aa[] = str.split("\\|");
                    if(amapFragment == null) {
                        AmapFragment amapFragment = (AmapFragment) getFragmentManager().findFragmentById(R.id.layout_mode_vertical_hover);
                    }
                    TextView mDisplayDataTV = (TextView)amapFragment.getView().findViewById(R.id.tv_display_data);
                    //TODO=== 添加要显示的实时数据
                    mDisplayDataTV.setText(aa[0].substring(7));
                }
            });
        }
    }
    //----------------------------------------------------------------------------------------------
}
