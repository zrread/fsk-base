package com.fsk.framework.core.exception;

import com.fsk.framework.bean.response.BaseApiResponse;
import com.mysql.cj.jdbc.exceptions.ConnectionFeatureNotAvailableException;
import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.dao.*;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;

@Slf4j
@ControllerAdvice
@ConditionalOnProperty(prefix = "fsk.global.controller-advice", value = "enable", havingValue = "true", matchIfMissing = true)
public class GlobalExceptionHandler<T> {

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public BaseApiResponse<?> bizExceptionHandler(HttpServletRequest req, BizException e) {
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(e.getErrorCode(), e.getErrorMsg()));
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public BaseApiResponse<?> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error(ResponseRetEnum.EXCEPTION_NULL_POINT.getMessage(), e);
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.EXCEPTION_NULL_POINT));
    }

    @ExceptionHandler(value = NoFallbackAvailableException.class)
    @ResponseBody
    public BaseApiResponse<?> exceptionHandler(HttpServletRequest req, NoFallbackAvailableException e) {
        log.error(ResponseRetEnum.FEIGN_FALL_BACK_EXCEPTION.getMessage(), e);
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.FEIGN_FALL_BACK_EXCEPTION));
    }

    @ExceptionHandler(value = FeignException.class)
    @ResponseBody
    public BaseApiResponse<?> exceptionHandler(HttpServletRequest req, FeignException e) {
        if (e instanceof RetryableException) {
            log.error(ResponseRetEnum.FEIGN_RETRY_EXCEPTION.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.FEIGN_RETRY_EXCEPTION));
        }
        log.error(ResponseRetEnum.FEIGN_EXCEPTION.getMessage(), e);
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.FEIGN_EXCEPTION));
    }

    @ExceptionHandler(value = UndeclaredThrowableException.class)
    @ResponseBody
    public BaseApiResponse<?> exceptionHandler(HttpServletRequest req, UndeclaredThrowableException e) {
        log.error(ResponseRetEnum.UN_DECLARED_EXCEPTION.getMessage(), e);
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.UN_DECLARED_EXCEPTION));
    }

    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public BaseApiResponse<?> sqlExceptionHandler(HttpServletRequest req, SQLException e) {

        if (e instanceof SQLIntegrityConstraintViolationException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_UNIQUE_INDEX.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_UNIQUE_INDEX));
        }
        if (e instanceof BatchUpdateException) {
            log.error(ResponseRetEnum.SQL_EXCEPTION_BATCH.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SQL_EXCEPTION_BATCH));
        }
        if (e instanceof ConnectionFeatureNotAvailableException) {
            log.error(ResponseRetEnum.SQL_EXCEPTION_CONNECTION_NOT_AVAILABLE.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SQL_EXCEPTION_CONNECTION_NOT_AVAILABLE));
        }
        if (e instanceof SQLInvalidAuthorizationSpecException) {
            log.error(ResponseRetEnum.SQL_EXCEPTION_CONNECTION_INVALID_AUTH.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SQL_EXCEPTION_CONNECTION_INVALID_AUTH));
        }
        if (e instanceof MySQLTimeoutException) {
            log.error(ResponseRetEnum.SQL_EXCEPTION_MYSQl_TIMEOUT.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SQL_EXCEPTION_MYSQl_TIMEOUT));
        }

        log.error(ResponseRetEnum.SQL_EXCEPTION.getMessage(), e);
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SQL_EXCEPTION));
    }

    /**
     * DataAccessException for manufacturer
     * catch subclass of DataAccessException.class
     *
     * Param req
     * Param e
     * Return
     */
    @ExceptionHandler(value = DataAccessException.class)
    @ResponseBody
    public BaseApiResponse<?> dataAccessExceptionExceptionHandler(HttpServletRequest req, DataAccessException e) {
        if (e instanceof DuplicateKeyException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_Duplicate_KeyException.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_Duplicate_KeyException));
        }
        if (e instanceof DataIntegrityViolationException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_HAVE_NO_DEFAULT_VALUE.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_HAVE_NO_DEFAULT_VALUE));
        }
        if (e instanceof BadSqlGrammarException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_BAD_SQL_GRAMMAR.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_BAD_SQL_GRAMMAR));
        }
        if (e instanceof CannotGetJdbcConnectionException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_JDBC_CONNECTION.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_JDBC_CONNECTION));
        }
        if (e instanceof CannotAcquireLockException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_CANNOT_ACQUIRE_LOCK.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_CANNOT_ACQUIRE_LOCK));
        }
        if (e instanceof DeadlockLoserDataAccessException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_DEAD_LOCK.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_DEAD_LOCK));
        }
        if (e instanceof QueryTimeoutException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_QUERY_TIMEOUT.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_QUERY_TIMEOUT));
        }
        if (e instanceof ConcurrencyFailureException) {
            log.error(ResponseRetEnum.DATA_ACCESS_EXCEPTION_CONCURRENCY_FAILURE.getMessage(), e);
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION_CONCURRENCY_FAILURE));
        }
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.DATA_ACCESS_EXCEPTION));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseApiResponse<?> exceptionHandler(HttpServletRequest req, Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.EXCEPTION_VALID.getCode(),
                    ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage()));
        }
        log.error(">>> ", e);
        System.out.println("");
        System.out.println(AnsiOutput.toString(AnsiColor.DEFAULT, "***************************", AnsiColor.DEFAULT));
        System.out.println(AnsiOutput.toString(AnsiColor.DEFAULT, " REQUEST FAILED TO EXECUTE", AnsiColor.DEFAULT));
        System.out.println(AnsiOutput.toString(AnsiColor.DEFAULT, "***************************", AnsiColor.DEFAULT));
        System.out.println("");
        System.out.println(AnsiOutput.toString(AnsiColor.DEFAULT, "Description: ", AnsiColor.DEFAULT));
        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_RED, "异常原因是：" + e, AnsiColor.DEFAULT));
        System.out.println("");
        System.out.println(AnsiOutput.toString(AnsiColor.DEFAULT, "Action: ", AnsiColor.DEFAULT));
        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_RED, e.getMessage(), AnsiColor.DEFAULT));
        System.out.println("");
        return MDCUtils.BaseApi_TID(new BaseApiResponse<>(ResponseRetEnum.SYSTEM_EXCEPTION));
    }
}
