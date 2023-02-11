package com.fsk.common.response;

import com.fsk.common.enums.ResponseStatusEnum;
import com.fsk.framework.bean.response.BaseApiResponse;

/**
 * 兼容老项目距
 *
 * since 1.12.0: This class would be removed
 */
@Deprecated // 请使用BaseApiResponse代替
public class BaseResponse<T> extends BaseApiResponse<T> {

    private String responseId;

    public BaseResponse() {
    }

    public BaseResponse(T responseData) {
        super(responseData);
    }

    public BaseResponse(T responseData, long totalCount) {
        this.statusCode = ResponseStatusEnum.SUCCESS.getCode();
        this.statusMsg = ResponseStatusEnum.SUCCESS.getMsg();
        super.setResponseData(responseData);
        super.setTotalCount(totalCount);
    }

    public static <T> BaseResponse<T> error(ResponseStatusEnum status) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setStatusCode(status.getCode());
        baseResponse.setStatusMsg(status.getMsg());
        return baseResponse;
    }

    public static <T> BaseResponse<T> error(String statusCode, String statusMsg) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setStatusCode(statusCode);
        baseResponse.setStatusMsg(statusMsg);
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(T data){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setResponseData(data);
        return baseResponse;
    }

    /**
     * 设置状态值
     *
     * Param status
     */
    public void setStatus(ResponseStatusEnum status) {
        this.statusCode = status.getCode();
        this.statusMsg = status.getMsg();
    }

    public void setStatus(String statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

}
