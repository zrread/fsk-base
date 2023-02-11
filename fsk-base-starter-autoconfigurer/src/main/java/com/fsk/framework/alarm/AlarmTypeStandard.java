package com.fsk.framework.alarm;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/21
 * Describe: Alarm monitor type.
 */
public enum AlarmTypeStandard {

    ALARM_TYPE_STANDARD_INFO("COM_FSK_ALARM_MONITOR_$INFO_","常规"),
    ALARM_TYPE_STANDARD_WARN("COM_FSK_ALARM_MONITOR_$WARN_","警告"),
    ALARM_TYPE_STANDARD_SUCCESS("COM_FSK_ALARM_MONITOR_$SUCCESS_","200成功"),
    ALARM_TYPE_STANDARD_FAILED("COM_FSK_ALARM_MONITOR_$FAILED_","-200失败"),
    ALARM_TYPE_STANDARD_ERROR("COM_FSK_ALARM_MONITOR_$ERROR_","错误"),
    ALARM_TYPE_STANDARD_SLOW_REQUEST("COM_FSK_ALARM_MONITOR_$SLOW_REQUEST_","慢请求"),
    ALARM_TYPE_STANDARD_TIMEOUT("COM_FSK_ALARM_MONITOR_$TIMEOUT_","请求超时"),
    ;

    AlarmTypeStandard(String code, String type) {
        this.code = code;
        this.type = type;
    }

    private String code;
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
