package com.fsk.common.utils.exception;

import com.fsk.common.enums.ResponseStatusEnum;
import com.fsk.framework.core.exception.BizException;

/**
 * 兼容老项目FskException
 *
 * since 1.12.0: This class would be removed
 */
@Deprecated // 请使用BizException代替
public class FskException extends BizException {

    public FskException(ResponseStatusEnum responseStatusEnum) {
        super(responseStatusEnum.getMsg());
        this.errorCode = responseStatusEnum.getCode();
    }


    public FskException(String code, String message) {
        super(code, message);
    }

    public String getCode() {
        return this.errorCode;
    }

    public void setCode(String code) {
        this.errorCode = code;
    }
}
