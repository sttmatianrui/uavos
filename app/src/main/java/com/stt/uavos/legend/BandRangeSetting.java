package com.stt.uavos.legend;

/**
 * @ description: 图例每段对应的类
 * @ time: 2017/9/5.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class BandRangeSetting {
    private Double min;
    private Double max;
    private String color;

    public BandRangeSetting(){
        super();
    }

    public BandRangeSetting(Double min, Double max, String color) {
        this.min = min;
        this.max = max;
        this.color = color;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIntColor(int color){
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String red16 = String.format("%X", red);
        String green16 = String.format("%X", green);
        String blue16 = String.format("%X", blue);
        String color16 = "#" + (red16.length() == 2 ? red16 : ("0" + red16)) + (green16.length() == 2 ? green16 : ("0" + green16)) + (blue16.length() == 2 ? blue16 : ("0" + blue16));

        this.color = color16;
    }

    public BandRangeSetting cloneSetting(){
        return new BandRangeSetting(this.min, this.max, this.color);
    }

    @Override
    public String toString() {
        String range = min+ "-" + max;
        return range;
    }
}
