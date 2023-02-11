package com.fsk.framework.bean.response;

import com.alibaba.fastjson.JSONObject;
import com.fsk.framework.core.exception.BaseSystemRet;
import com.fsk.framework.core.exception.ResponseRetEnum;

import java.io.Serializable;

public class BaseApiResponse<T> extends BaseSystemRet implements Serializable {

    /**
     * 响应代码
     */
    protected String statusCode;

    /**
     * 响应消息
     */
    protected String statusMsg;
    private int pageNum;
    private int pageSize;
    private Long totalCount;

    /**
     * 响应结果
     */
    private T responseData;

    public BaseApiResponse() {
        this.statusCode = ResponseRetEnum.SUCCESS.getCode();
        this.statusMsg = ResponseRetEnum.SUCCESS.getMessage();
    }

    public BaseApiResponse(T responseData) {
        this.statusCode = ResponseRetEnum.SUCCESS.getCode();
        this.statusMsg = ResponseRetEnum.SUCCESS.getMessage();
        this.responseData = responseData;
        if(responseData instanceof PageBody){
            this.pageNum = ((PageBody<?>) responseData).getPageNum();
            this.pageSize = ((PageBody<?>) responseData).getPageSize();
            this.totalCount = ((PageBody<?>) responseData).getTotalCount();
        }
    }

    public BaseApiResponse(String code, String message) {
        this.statusCode = code;
        this.statusMsg = message;
    }

    public BaseApiResponse(ResponseRetEnum responseRetEnum) {
        this.statusCode = responseRetEnum.getCode();
        this.statusMsg = responseRetEnum.getMessage();
    }

    public static <T> BaseApiResponse<T> error(String statusCode, String statusMsg) {
        BaseApiResponse<T> baseResponse = new BaseApiResponse<>();
        baseResponse.setStatusCode(statusCode);
        baseResponse.setStatusMsg(statusMsg);
        return baseResponse;
    }

    public static <T> BaseApiResponse<T> success(T data){
        BaseApiResponse<T> baseResponse = new BaseApiResponse<>();
        baseResponse.setResponseData(data);
        return baseResponse;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }


    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Number totalCount) {
        if (totalCount == null) {
            this.totalCount = null;
            return;
        }
        this.totalCount = totalCount.longValue();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
