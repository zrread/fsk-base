package com.fsk.framework.core.exception;

import com.alibaba.fastjson.JSONObject;
import com.fsk.framework.bean.response.BaseApiResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TIDAspect {

    @Pointcut(
            "execution(* com.fsk.*.controller.*.*(..)))"
    )
    public void trace() {
    }

    @Before(
            "trace()"
    )
    public void before() {
        MDCUtils.MDC();
    }

    @Around(
            "trace()"
    )
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
