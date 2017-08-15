package com.stt.uavos.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @ description: 与Toast有关的工具类
 * @ time: 2017/8/9.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class ToastUtils {

    public static void setResultToToast(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
