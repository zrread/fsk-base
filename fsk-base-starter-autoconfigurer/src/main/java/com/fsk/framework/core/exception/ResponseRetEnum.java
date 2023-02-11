package com.fsk.framework.core.exception;

public enum ResponseRetEnum{
    /**
     * codes with length of 3 are for base exception
     */
    SUCCESS("200", "请求成功"),
    FAILED("-200", "请求失败"),
    BIZ_LOCAL_USER_NOT_LOGGED_IN("606", "用户未登录或token过期"),

    /**
     *  starting with '1' for common exception
     */
    NET_ERROR("100001", "网络异常，请稍后重试"),
    BIZ_EXCEPTION_PARAMS_ILLEGAL("100002", "请求参数不合法"),
    BIZ_EXCEPTION_PARAMS_PATTERN_ILLEGAL("100003", "请求参数格式不正确"),
    BIZ_EXCEPTION_DATA_NOT_FOUND("100004", "数据不存在"),
    SYSTEM_EXCEPTION("100005", "系统异常"),
    EXCEPTION_OTHER("100006", "其他异常"),
    EXCEPTION_NULL_POINT("100007", "空指针异常"),
    EXCEPTION_VALID("100008", "参数校验异常"),
    BIZ_EXCEPTION_UPLOAD_FILE_IS_NULL("100008", "必须上传一个文件"),
    EXCEPTION_UPLOAD_FILE("100009", "上传异常"),
    UN_DECLARED_EXCEPTION("100010", "未定义的异常"),

    /**
     *  starting with '20' for Manufacturer exception
     */
    DATA_ACCESS_EXCEPTION("200000", "DataAccessException异常"),
    DATA_ACCESS_EXCEPTION_HAVE_NO_DEFAULT_VALUE("200001", "SQL语句参数缺失默认值"),
    DATA_ACCESS_EXCEPTION_UNIQUE_INDEX("200002", "违反唯一索引约束"),
    DATA_ACCESS_EXCEPTION_BAD_SQL_GRAMMAR("200003", "SQL语法错误,请检查你的SQL语句"),
    DATA_ACCESS_EXCEPTION_JDBC_CONNECTION("200004", "数据库连接异常"),
    DATA_ACCESS_EXCEPTION_CANNOT_ACQUIRE_LOCK("200005", "不能获取锁异常"),
    DATA_ACCESS_EXCEPTION_DEAD_LOCK("200006", "死锁异常"),
    DATA_ACCESS_EXCEPTION_Duplicate_KeyException("200007", "索引Key重复"),
    DATA_ACCESS_EXCEPTION_QUERY_TIMEOUT("200008", "查询超时"),
    DATA_ACCESS_EXCEPTION_CONCURRENCY_FAILURE("200009", "数据操作失败"),

    /**
     *  starting with '21' for SQL exception
     */
    SQL_EXCEPTION("210000", "SQL操作异常"),
    SQL_EXCEPTION_BATCH("210001", "批量操作异常"),
    SQL_EXCEPTION_CONNECTION_NOT_AVAILABLE("210002", "数据库连接不可用"),
    SQL_EXCEPTION_CONNECTION_INVALID_AUTH("210003", "DB连接未授权"),
    SQL_EXCEPTION_MYSQl_TIMEOUT("210004", "MYSQL超时"),

    /**
     *  starting with '22' for circuit exception
     */
    FEIGN_EXCEPTION("220000","fegin调用异常"),
    FEIGN_FALL_BACK_EXCEPTION("220001","fegin调用slow熔断"),
    FEIGN_RETRY_EXCEPTION("220002","feign调用重试异常"),

    ;

    /** 返回码 */
    private String code;

    /** 描述 */
    private String message;

    ResponseRetEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
