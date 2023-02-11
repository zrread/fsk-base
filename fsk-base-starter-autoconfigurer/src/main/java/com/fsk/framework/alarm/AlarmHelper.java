package com.fsk.framework.alarm;

import com.fsk.framework.constants.CommonContanst;
import com.fsk.framework.constants.ExceptionConstant;
import com.fsk.framework.core.mvc.FskRequestContextHelper;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/21
 * Describe: AlarmHelper.
 */
public final class AlarmHelper implements CommonContanst, ExceptionConstant {

    public static String toAlarmMsg(AlarmTypeStandard standard, String msg) {
        String requestURI = null;
        try {
            requestURI = FskRequestContextHelper.obtain().getRequestURI();
        } catch (Exception e) {
            requestURI = EXCE_MSG_7;
        }
        return standard.getCode() + msg + REQ + requestURI;
    }
}
