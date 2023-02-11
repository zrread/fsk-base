package com.fsk.framework.core.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fsk.common.response.BaseResponse;
import com.fsk.framework.bean.response.BaseApiResponse;
import com.fsk.framework.core.mvc.FskRequestContextHelper;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

public class MDCUtils extends BaseSystemRet {

    private final static String RESPONSE_DATA = "responseData";
    private final static String MSG = "Unknown Request Ip";

    protected static void MDC() {
        HttpServletRequest request = null;
        try {
            request = FskRequestContextHelper.obtain();
            MDC.put(BaseSystemRet.REQUEST_IP, request.getRemoteAddr());
        } catch (Exception e) {
            MDC.put(BaseSystemRet.REQUEST_IP, MSG);
        }
    }

    public static BaseApiResponse<?> BaseApi_TID(BaseApiResponse<?> response) {
        response.setTraceId(TIDHolder.getTraceId());
        response.setSpanId(TIDHolder.getSpanId());
        response.setSegmentId(TIDHolder.segmentId());
        response.setRequestIp(MDC.get(REQUEST_IP));
        MDC.remove(REQUEST_IP);
        MDC.clear();
        return response;
    }

    public static BaseResponse<?> Base_TID(BaseResponse<?> response) {
        response.setTraceId(TIDHolder.getTraceId());
        response.setSpanId(TIDHolder.getSpanId());
        response.setSegmentId(TIDHolder.segmentId());
        response.setRequestIp(MDC.get(REQUEST_IP));
        MDC.remove(REQUEST_IP);
        MDC.clear();
        return response;
    }

    public static JSONObject JSON_TID(JSONObject res) {
        res.put(TRACE_ID, TIDHolder.getTraceId());
        res.put(REQUEST_IP, MDC.get(REQUEST_IP));
        MDC.remove(REQUEST_IP);
        MDC.clear();
        return res;
    }

    public static JSONObject JSON_MAP_TID(JSONObject res) {
        res.put(TRACE_ID, TIDHolder.getTraceId());
        res.put(REQUEST_IP, MDC.get(REQUEST_IP));
        MDC.remove(REQUEST_IP);
        MDC.clear();
        return res;
    }

    public static JSONObject JSON_ARRAY_TID(JSONArray res) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TRACE_ID, TIDHolder.getTraceId());
        jsonObject.put(REQUEST_IP, MDC.get(REQUEST_IP));
        jsonObject.put(RESPONSE_DATA, res);
        MDC.remove(REQUEST_IP);
        MDC.clear();
        return jsonObject;
    }

}
