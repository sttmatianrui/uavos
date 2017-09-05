package com.stt.uavos.utils;

import android.text.TextUtils;

import com.stt.uavos.model.Mission;

/**
 * @ description:解析数据
 * @ time: 2017/8/15.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class AnalyzeUtil {

    public static Mission analyzeMission(String datas) {
        Mission mission = new Mission();
        if(!TextUtils.isEmpty(datas)) {
            String[] array = datas.split("\\|");
            //TODO===解析数据由Mission实体来接收
            /**
             * 飞机传到平板O2M
             * UAVDTA：实时值|年月日|时分秒|经度|纬度|高度|速度x|速度y|速度z
             * UAVDTA:0.352|170815|133000|0.11111|0.11111|10.0|0.1|0.1|0.1
             */
            mission.setData(array[0].substring(7));
            mission.setDate(array[1]);
            mission.setTime(array[2]);
            mission.setLat(array[3]);
            mission.setLng(array[4]);
            mission.setHeight(array[5]);
            mission.setXspeed(array[6]);
            mission.setYspeed(array[7]);
            mission.setZspeed(array[8]);
        }
        return mission;
    }

}
