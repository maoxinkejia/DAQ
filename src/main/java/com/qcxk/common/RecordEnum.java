package com.qcxk.common;

import java.util.Objects;

public enum RecordEnum implements BaseEnum<RecordEnum, Integer> {
    CH4_GAS_VOLUME_ALARM_ZERO(3, "甲烷浓度零点标定(LEL)", "%"),
    CH4_GAS_VOLUME_ALARM_RANGE(4, "甲烷浓度量程标定(LEL)", "%"),
    TEMPERATURE_CORRECTION(6, "温度校准", "℃"),
    WELL_LID_OPEN_ALARM(7, "是否启动倾斜移位报警", ""),
    WATER_DEPTH_ALARM_THRESHOLD(8, "液位报警值", "cm"),
    CH4_GAS_VOLUME_THRESHOLD(10, "甲烷气体浓度报警阈值(LEL)", "%"),
    TEMPERATURE_THRESHOLD(11, "温度报警阈值", "℃"),
    DEVICE_BAT_VOL_THRESHOLD(12, "设备电池电量报警阈值", "V"),
    WELL_LID_BAT_VOL_THRESHOLD(14, "井盖电池电量报警阈值", "V"),
    UPLOAD_DATA_PERIOD(13, "数据上传周期", "min");


    private final Integer type;
    private final String name;
    private final String unit;

    RecordEnum(Integer type, String name, String unit) {
        this.type = type;
        this.name = name;
        this.unit = unit;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public static RecordEnum of(Integer type) {
        RecordEnum[] values = RecordEnum.values();

        for (RecordEnum value : values) {
            if (Objects.equals(value.getType(), type)) {
                return value;
            }
        }

        return null;
    }

    @Override
    public Integer getValue() {
        return type;
    }
}
