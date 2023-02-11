package com.fsk.framework.core.async;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.service.FskBaseService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class AsyncTaskCallable implements Callable<Object> {

    private AsyncServiceParam param;

    public AsyncTaskCallable(AsyncServiceParam param) {
        this.param = param;
    }

    @Override
    public Object call() throws Exception {
        return deal();
    }

    private Object deal(){
        return invoke(param);
    }

    private Object invoke(AsyncServiceParam param) {
        Class<? extends FskBaseService> tClass = param.getTClass();
        AsyncServiceMethod method = param.getMethod();
        /*IAsyncServiceRequest request = param.getRequest();
        IAsyncServiceResponse response = param.getResponse();*/
        Object invokeResponse = new Object();
        try {
            Method proxyMethod = tClass.getMethod(method.getMethod());
            invokeResponse = proxyMethod.invoke(FskSpringContextHolder.getBeanByClass(tClass));
        } catch (Exception e) {
            log.error("AbstractAsyncServiceInvoker.invoke.Exception=",e);
        }
        return invokeResponse;
    }
}
