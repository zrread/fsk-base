package com.fsk.framework.core.exception;

import com.fsk.common.response.BaseResponse;
import com.fsk.framework.bean.response.BaseApiResponse;
import com.fsk.framework.constants.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/3/23
 * Describe: ResRewrite.
 */
@Slf4j
@ConditionalOnProperty(prefix = "fsk.global.controller-advice", value = "enable", havingValue = "true", matchIfMissing = true)
@ControllerAdvice(value = {"com.fsk"})
public class ResRewrite implements ResponseBodyAdvice<Object>, ExceptionConstant {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (methodParameter.getParameterType().equals(BaseApiResponse.class) ||
                methodParameter.getParameterType().equals(BaseResponse.class)
        ) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object res, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (res instanceof BaseApiResponse) {
            BaseApiResponse<?> response = (BaseApiResponse<?>) res;
            return MDCUtils.BaseApi_TID(response);
        } else if (res instanceof BaseResponse) {
            BaseResponse<?> response = (BaseResponse<?>) res;
            return MDCUtils.Base_TID(response);
//        } else if (res instanceof Map) {
//            try {
//                return MDCUtils.JSON_MAP_TID(JSONObject.parseObject(JSONObject.toJSONString(res)));
//            } catch (Exception e) {
//                log.info(EXCE_MSG_8);
//                return res;
//            }
//        } else if (res instanceof Collection) {
//            try {
//                return MDCUtils.JSON_ARRAY_TID(JSONObject.parseArray(JSONObject.toJSONString(res)));
//            } catch (Exception e) {
//                log.info(EXCE_MSG_8);
//                return res;
//            }
//        } else if (!(res instanceof String) && !(res instanceof HttpResponse)) {
//            try {
//                return MDCUtils.JSON_TID(JSONObject.parseObject(JSONObject.toJSONString(res)));
//            } catch (Exception e) {
//                log.info(EXCE_MSG_8);
//                return res;
//            }
        } else {
            return res;
        }
    }
}
