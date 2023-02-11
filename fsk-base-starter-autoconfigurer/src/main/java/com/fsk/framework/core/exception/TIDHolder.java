package com.fsk.framework.core.exception;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Component;

public class TIDHolder {

    public static String getTraceId(){
        return TraceContext.traceId();
    }

    public static int getSpanId(){
        return TraceContext.spanId();
    }

    public static String segmentId(){
        return TraceContext.segmentId();
    }


}
