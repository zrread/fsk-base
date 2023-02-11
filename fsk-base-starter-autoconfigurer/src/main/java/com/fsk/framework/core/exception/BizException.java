package com.fsk.framework.core.exception;

public class BizException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(ResponseRetEnum retEnum) {
        super(retEnum.getCode());
        this.errorCode = retEnum.getCode();
        this.errorMsg = retEnum.getMessage();
    }

    public BizException(ResponseRetEnum retEnum, Throwable cause) {
        super(retEnum.getCode(), cause);
        this.errorCode = retEnum.getCode();
        this.errorMsg = retEnum.getMessage();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.errorCode = ResponseRetEnum.FAILED.getCode();
        this.errorMsg = errorMsg;
    }

    public BizException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BizException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
