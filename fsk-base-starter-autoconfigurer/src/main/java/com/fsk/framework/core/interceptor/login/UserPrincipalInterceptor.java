package com.fsk.framework.core.interceptor.login;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.autoconfigure.login.LocalLoginProperties;
import com.fsk.framework.constants.CommonContanst;
import com.fsk.framework.constants.ExceptionConstant;
import com.fsk.framework.core.exception.BizException;
import com.fsk.framework.core.exception.ResponseRetEnum;
import com.fsk.framework.core.interceptor.mybatis.AutoOTC;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录token拦截
 */
@Slf4j
@ConditionalOnProperty(prefix = "fsk.global.local-user", value = "enable", havingValue = "true")
@RefreshScope
@Component
public class UserPrincipalInterceptor implements HandlerInterceptor, ExceptionConstant, CommonContanst {

    @Value("${fsk.global.local-user.apiWhiteList:}")
    private List<String> apiWhiteList;

    private static final String NULL = "NULL";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean var = false;
        for (String api : apiWhiteList) {
            if (request.getRequestURI().contains(api)) {
                var = true;
                break;
            }
        }
        if (var) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } else if (!AutoOTC.check(request)) {
            String token = request.getHeader(APP_TOKEN);
            String customName = FskSpringContextHolder.getBeanByClass(LocalLoginProperties.class).getCustomToken().getName();
            String customToken = request.getHeader(customName);

            if (StringUtils.isNotBlank(customToken) && !StringUtils.equals(customName, NULL)) {
                FskUserPrincipalProcessor.set(customToken);
            } else {
                if (!StringUtils.isNotBlank(token)) {
                    log.warn(EXCE_MSG_4);
                    throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getCode(), EXCE_MSG_4);
                }
                FskUserPrincipalProcessor.set(token);
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        FskUserPrincipalProcessor.destroy();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public List<String> getApiWhiteList() {
        return this.apiWhiteList;
    }
}
