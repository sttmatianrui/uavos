package com.stt.uavos.coordinate;

import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.model.LatLng;

import java.util.HashMap;

/**
 * Created by matianrui on 2017/7/10.
 * 中国大陆区域坐标转换类 提供WGS84与GCJ02坐标转换
 * 该类下提供两个静态函数 getGCJ02Location() getWGS84Location()
 *  注意：地图中使用GCJ02坐标系 无人机使用WGS84坐标系
 */

public class ChineseCoordinate {

    public static LatLng getGCJ02Location(LatLng pos){
        //--GPS转换为高德坐标系
        CoordinateConverter converter  = new CoordinateConverter();
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 DPoint类型
        converter.coord(pos);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return  desLatLng;
    }
    public static LatLng getWGS84Location(LatLng pos){
        ChineseCoordinate wg = new ChineseCoordinate();

        HashMap<String, Double> hm = wg.delta(pos.latitude,pos.longitude);
        LatLng latlng = new LatLng(hm.get("lat"),hm.get("lon"));

        return latlng;
    }

    //----------------------------------------------------------------------------------------------
    private static final double PI = 3.1415926535897932384626;
    //private static final double XPI = PI * 3000 / 180;
    private static double A = 6378245.0;
    private static double EE = 0.00669342162296594323;
    public static LatLng GCJ022GPS84(LatLng latLng) {
        LatLng latLngTransformed;


        double dLat = transformLat(latLng.longitude - 105.0, latLng.latitude - 35.0);
        double dLon = transformLon(latLng.longitude - 105.0, latLng.latitude - 35.0);
        double radLat = latLng.latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = latLng.latitude + dLat;
        double mgLon = latLng.longitude + dLon;
        latLngTransformed = new LatLng(mgLat, mgLon);


        double longitude = latLng.longitude * 2 - latLngTransformed.longitude;
        double latitude = latLng.latitude * 2 - latLngTransformed.latitude;
        return new LatLng(latitude, longitude);
    }
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
    //----------------------------------------------------------------------------------------------

    public HashMap<String, Double> delta(double lat,double lon) {
        double a = 6378245.0;
        double ee = 0.00669342162296594323;
        double dLat = this.transformLat(lon - 105.0, lat - 35.0);
        double dLon = this.transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * this.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * this.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * this.PI);

        HashMap<String, Double> hm = new HashMap<String, Double>();
        hm.put("lat",lat - dLat);
        hm.put("lon",lon - dLon);

        return hm;
    }
}
