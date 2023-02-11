package com.fsk.framework.core.async;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.service.FskBaseService;
import lombok.Getter;

@Getter
public class AsyncServiceParam {

    private String id;
    private Class<? extends FskBaseService> tClass;
    private AsyncServiceMethod method;
    private IAsyncServiceRequest request;
    private IAsyncServiceResponse response;
    private int timeoutMills = 5000;//超时时间，毫秒
    private boolean terminalWhenException = true;//遇到异常是否终止，默认终止

    public AsyncServiceParam() {
    }

    private AsyncServiceParam(String id,
                              Class<? extends FskBaseService> tClass,
                              AsyncServiceMethod method,
                              IAsyncServiceRequest request,
                              IAsyncServiceResponse response,
                              int timeoutMills,
                              boolean terminalWhenException) {
        this.id = id;
        this.tClass = tClass;
        this.request = request;
        this.response = response;
        this.timeoutMills = timeoutMills;
        this.method = method;
        this.terminalWhenException = terminalWhenException;
    }

    public AsyncServiceParam appendId(String id){
        this.id = id;
        return this;
    }

    public AsyncServiceParam appendService(Class<? extends FskBaseService> tClass) {
        this.tClass = tClass;
        return this;
    }

    public AsyncServiceParam appendMethod(AsyncServiceMethod methodName) {
        this.method = methodName;
        return this;
    }

    public AsyncServiceParam appendRequest(IAsyncServiceRequest request) {
        this.request = request;
        return this;
    }

    public AsyncServiceParam appendResponse(IAsyncServiceResponse response) {
        this.response = response;
        return this;
    }

    public AsyncServiceParam appendTimeoutMills(int timeoutMills) {
        this.timeoutMills = timeoutMills;
        return this;
    }

    public AsyncServiceParam appendTerminalWhenException(boolean terminalWhenException) {
        this.terminalWhenException = terminalWhenException;
        return this;
    }

    public AsyncServiceParam build(){
        try{
            FskBaseService beanByClass = FskSpringContextHolder.getBeanByClass(tClass);
            return new AsyncServiceParam(id,beanByClass.getClass(), method,request,response,timeoutMills,terminalWhenException);
        }catch (Exception e){
            System.out.println(e);
        }
        return new AsyncServiceParam();
    }
}
