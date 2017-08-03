package com.stt.uavos.mode;

/**
 * Created by 111112 on 2017/8/3.
 */

public enum SetMission {
    VERTICAL_HOVER_MODE(0),
    VERTICAL_MOVE_MODE(1),
    SURROUND_HOVER_MODE(2),
    SURROUND_MOVE_MODE(3),
    WAYPOINT_MODE(4),
    SPACE_MODE(5),
    SCAN_MODE(6),

    FREE_MODE(8);


    private int value;

    private SetMission(int var3) {
        this.value = var3;
    }
    public int value() {
        return this.value;
    }
    public boolean _equals(int var1) {
        return this.value == var1;
    }

    public static SetMission find(int var0) {
        SetMission var1 = FREE_MODE;

        for(int var2 = 0; var2 < values().length; ++var2) {
            if(values()[var2]._equals(var0)) {
                var1 = values()[var2];
                break;
            }
        }

        return var1;
    }

}
