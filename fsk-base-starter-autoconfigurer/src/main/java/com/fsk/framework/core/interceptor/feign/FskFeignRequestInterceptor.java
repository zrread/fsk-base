package com.fsk.framework.core.interceptor.feign;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.core.mvc.FskRequestContextHelper;
import com.fsk.framework.extend.constant.FskConstants;
import com.fsk.framework.extend.utils.encrypt.DesCrypto;
import com.fsk.framework.extend.utils.encrypt.ICrypto;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Openfeign request interceptor
 * update by 2022-08-04 13:39:00
 */
@Slf4j
public class FskFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = null;
        try {
            request = FskRequestContextHelper.obtain();
        } catch (Exception e) {
            log.info("The current request is not an HTTP protocol request");
        }
        if (request != null) {
            Enumeration<String> headerNames = request.getHeaderNames();
            //遍历请求头，添加所有请求头数据
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (StringUtils.equals("content-length", name)) {
                        continue;
                    }
                    if (StringUtils.equals("application/x-www-form-urlencoded", values)) {
                        requestTemplate.header(name, "application/json");
                    } else {
                        requestTemplate.header(name, values);
                    }
                }
            }
            if (request.getHeader(FskConstants.TOKEN_KEY) == null && request.getHeader(FskConstants.SOURCE_KEY) == null) {
                tempNoTokenSign(requestTemplate);
            }
        } else {
            tempNoTokenSign(requestTemplate);
        }
        // custom
        this.custom(requestTemplate);

    }

    /**
     * 一个临时的无token的调用签名方案，用于新框架调用老框架
     */
    private void tempNoTokenSign(RequestTemplate requestTemplate) {
        long currentTimeMillis = System.currentTimeMillis();
        ICrypto crypto = new DesCrypto(currentTimeMillis);

        requestTemplate.header(FskConstants.SOURCE_KEY, FskConstants.SOURCE_VALUE_TIMED_TASK);
        requestTemplate.header("timestamp", String.valueOf(currentTimeMillis));
        requestTemplate.header("sign", crypto.encrypt(FskConstants.SIGN_CONTENT));
    }

    /**
     * Developer defined request interceptor processing logic
     */
    private void custom(RequestTemplate requestTemplate) {
        try {
            FeignRequestInterceptorCustomizer bean = FskSpringContextHolder.getBeanByClass(FeignRequestInterceptorCustomizer.class);
            bean.custom(requestTemplate);
        } catch (NoSuchBeanDefinitionException nsbde) {
            log.info("User does not customize feign interceptor");
        } catch (Exception e) {
            log.warn("Custom feign interceptor execution exception");
        }
    }

    @Deprecated
    private void parameterDeal(HttpServletRequest request, RequestTemplate requestTemplate) {
        Enumeration<String> bodyNames = request.getParameterNames();
        StringBuffer body = new StringBuffer();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String values = request.getParameter(name);
                body.append(name).append("=").append(values).append("&");
            }
        }
        if (body.length() != 0) {
            body.deleteCharAt(body.length() - 1);
            requestTemplate.body(body.toString());
            log.info("FskFeignRequestInterceptor interceptor body:{}", body.toString());
        }
    }

}
