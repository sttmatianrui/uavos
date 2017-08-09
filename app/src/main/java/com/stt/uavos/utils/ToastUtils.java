package com.stt.uavos.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ToastUtils {

    public static void setResultToToast(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
