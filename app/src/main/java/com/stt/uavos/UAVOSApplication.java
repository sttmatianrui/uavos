package com.stt.uavos;import android.app.Application;import android.content.Context;import android.content.Intent;import android.os.Handler;import android.os.Looper;import android.widget.Toast;import dji.common.error.DJIError;import dji.common.error.DJISDKError;import dji.sdk.base.BaseComponent;import dji.sdk.base.BaseProduct;import dji.sdk.sdkmanager.DJISDKManager;/** * Created by Administrator on 2017/7/7. * */public class UAVOSApplication extends Application {    private static final String TAG = UAVOSApplication.class.getName();    public static final String FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change";    private static BaseProduct mProduct;    private Handler mHandler;    private static Context context;    public static synchronized BaseProduct getProductInstance() {        if (null == mProduct) {            mProduct = DJISDKManager.getInstance().getProduct();        }        return mProduct;    }    @Override    public void onCreate() {        super.onCreate();        context = getApplicationContext();//---        mHandler = new Handler(Looper.getMainLooper());        // 初始化SDK        DJISDKManager.getInstance().registerApp(this, mDJISDKManagerCallback);    }    public static Context getContext(){        return context;    }    private DJISDKManager.SDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.SDKManagerCallback() {        @Override        public void onRegister(DJIError error) {            if(error == DJISDKError.REGISTRATION_SUCCESS) {                DJISDKManager.getInstance().startConnectionToProduct();                Handler handler = new Handler(Looper.getMainLooper());                handler.post(new Runnable() {                    @Override                    public void run() {                        Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_LONG).show();                    }                });//                DJISDKManager.getInstance().enableBridgeModeWithBridgeAppIP("192.168.1.46");            } else {                Handler handler = new Handler(Looper.getMainLooper());                handler.post(new Runnable() {                    @Override                    public void run() {                        Toast.makeText(getApplicationContext(), "register sdk fails, check network is available", Toast.LENGTH_LONG).show();                    }                });            }        }        @Override        public void onProductChange(BaseProduct oldProduct, BaseProduct newProduct) {            mProduct = newProduct;            if(mProduct != null) {                mProduct.setBaseProductListener(mDJIBaseProductListener);            }            notifyStatusChange();        }    };    private BaseProduct.BaseProductListener mDJIBaseProductListener = new BaseProduct.BaseProductListener() {        @Override        public void onComponentChange(BaseProduct.ComponentKey key, BaseComponent oldComponent, BaseComponent newComponent) {            if(newComponent != null) {                newComponent.setComponentListener(mDJIComponentListener);            }            notifyStatusChange();        }        @Override        public void onConnectivityChange(boolean isConnected) {            notifyStatusChange();        }    };    private BaseComponent.ComponentListener mDJIComponentListener = new BaseComponent.ComponentListener() {        @Override        public void onConnectivityChange(boolean isConnected) {            notifyStatusChange();        }    };    private void notifyStatusChange() {        mHandler.removeCallbacks(updateRunnable);        mHandler.postDelayed(updateRunnable, 500);    }    private Runnable updateRunnable = new Runnable() {        @Override        public void run() {            Intent intent = new Intent(FLAG_CONNECTION_CHANGE);            sendBroadcast(intent);        }    };}