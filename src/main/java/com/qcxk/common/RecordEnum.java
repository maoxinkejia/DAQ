package com.qcxk.common;

import java.util.Objects;

public enum RecordEnum implements BaseEnum<RecordEnum, Integer> {
    CH4_GAS_ALARM_ZERO(1, "甲烷气体浓度报警值零点(VOL)"),
    CH4_GAS_ALARM_RANGE(2, "甲烷气体浓度报警值量程(VOL)"),
    CH4_GAS_VOLUME_ALARM_ZERO(3, "甲烷气体体积浓度报警值零点(LEL)"),
    CH4_GAS_VOLUME_ALARM_RANGE(4, "甲烷气体体积浓度报警值量程(LEL)"),
    TEMPERATURE_ALARM_ZERO(5, "温度报警值零点"),
    TEMPERATURE_ALARM_RANGE(6, "温度报警值量程"),
    ENABLE_TILT_ALARM(7, "是否启动倾斜移位报警"),
    LIQUID_ALARM_THRESHOLD(8, "液位报警值"),
    CH4_GAS_THRESHOLD(9, "甲烷气体报警阈值(VOL)"),
    CH4_GAS_VOLUME_THRESHOLD(10, "甲烷气体体积报警阈值(LEL)"),
    TEMPERATURE_THRESHOLD(11, "温度报警阈值"),
    BAT_VOL_THRESHOLD(12, "电池电量报警阈值"),
    UPLOAD_DATA_PERIOD(13, "数据上传周期");


    private final Integer type;
    private final String name;

    RecordEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public RecordEnum of(Integer type) {
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
