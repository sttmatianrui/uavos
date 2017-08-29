package com.stt.uavos.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by mtr on 2017/8/18.
 * 提供全局context
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate(){
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
