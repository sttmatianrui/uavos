package com.stt.uavos.legend;

import java.util.List;

/**
 * @ description: 图例对应的实体类
 * @ time: 2017/9/5.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class BandSetting {
    // 属性名跟legenddata.json中一致
    private String name;
    private String display;
    private String unit;
    private List<BandRangeSetting> range;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<BandRangeSetting> getRange() {
        return range;
    }

    public void setRange(List<BandRangeSetting> range) {
        this.range = range;
    }

}
