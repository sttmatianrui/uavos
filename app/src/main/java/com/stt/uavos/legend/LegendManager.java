package com.stt.uavos.legend;

import android.content.Context;
import com.stt.uavos.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ description: 图例管理类
 * @ time: 2017/9/5.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class LegendManager {
    private static LegendManager instance;
    private List<BandSetting> _bandSettings;
    private Context _context;

    private LegendManager(Context context){
        _context = context;
        List<BandSetting> bands = loadBands();
        /*List<BandSetting> bandSettings = loadSettings();
        for(int i = 0; i < bands.size(); i++){
            BandSetting band = bands.get(i);
            BandSetting setting = getBandSetting(bandSettings, band.getName());
            if(setting != null){
                band.setDisplay(setting.getDisplay());
                band.setRange(setting.getRange());
            }
        }*/
        _bandSettings = bands;
    }

    public static LegendManager getInstance(Context context){
        if(instance == null){
            instance = new LegendManager(context);
        }
        return instance;
    }

    private List<BandSetting> loadBands(){
        InputStream inputStream = null;
        List<BandSetting> bands = null;
        try {
            inputStream = _context.getResources().openRawResource(R.raw.legenddata);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String json = new String(buffer);
            bands = com.alibaba.fastjson.JSON.parseArray(json, BandSetting.class);
        } catch (Exception e) {
            bands = new ArrayList<BandSetting>();
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bands;
    }

    /**
     * 加载当前对监测频段的设置
     */
    private List<BandSetting> loadSettings(){
        FileInputStream inputStream = null;
        List<BandSetting> bands = null;
        try {
            inputStream =  _context.openFileInput("legend");
//            File file = new File(Constant.DEFAULT_SETTING_PATH, "Vehicular.json");
//            inputStream = new FileInputStream(file);
//            inputStream = new FileInputStream(_context.openFileInput("legend"));
            //TODO== 执行此处时，没有该文件系统是否会创建？为什么没有呢？
//            inputStream = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory("/"), "Vehicular.json"));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String json = new String(buffer);
            bands = com.alibaba.fastjson.JSON.parseArray(json, BandSetting.class);
        } catch (Exception e) {
            bands = new ArrayList<BandSetting>();
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bands;
    }

    public List<BandSetting> getBandSettings(){
        return _bandSettings;
    }

    public BandSetting getBandSetting(String name){
        if(_bandSettings != null){
            for(int i = 0; i < _bandSettings.size(); i++){
                BandSetting setting = _bandSettings.get(i);
                if(setting.getName().equals(name)){
                    return setting;
                }
            }
        }
        return null;
    }

    private BandSetting getBandSetting(List<BandSetting> bandSettings, String name){
        if(bandSettings == null) return null;
        for(int i = 0; i < bandSettings.size(); i++){
            BandSetting setting = bandSettings.get(i);
            if(setting.getName().equals(name)){
                return setting;
            }
        }
        return null;
    }

}
