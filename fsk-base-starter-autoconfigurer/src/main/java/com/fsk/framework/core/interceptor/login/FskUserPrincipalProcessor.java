package com.fsk.framework.core.interceptor.login;

import com.fsk.framework.autoconfigure.login.LocalLoginProperties;
import com.fsk.framework.bean.biz.system.LoginUserInfo;
import com.fsk.framework.constants.ExceptionConstant;
import com.fsk.framework.core.exception.BizException;
import com.fsk.framework.core.exception.ResponseRetEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/25
 * Describe: FskUser Principal Processor.
 */
@Slf4j
@Component
public class FskUserPrincipalProcessor implements ApplicationContextAware, EnvironmentAware, ServletContextAware, ExceptionConstant {
    private final static ThreadLocal<String> threadLocalToken = new ThreadLocal<>();
    private final static ThreadLocal<LoginUserInfo> threadLocalUser = new ThreadLocal<>();
    private static ApplicationContext context;
    private static Environment env;
    private static ServletContext servletContext;

    protected static LoginUserInfo obtain() {
        return FskUserPrincipalProcessor.setGetPrincipal();
    }

    private static LoginUserInfo setGetPrincipal() {
        FskUserPrincipalProcessor.check();
        FskUserPrincipalProcessor.setPrincipal();
        return FskUserPrincipalProcessor.threadLocalUser.get();
    }

    protected static void set(String loginToken) {
        FskUserPrincipalProcessor.threadLocalToken.set(loginToken);
    }

    private static void set(LoginUserInfo loginUserInfo) {
        FskUserPrincipalProcessor.threadLocalUser.set(loginUserInfo);
    }

    private static void setPrincipal() {
        FskUserPrincipalProcessor.set(SessionTokenHolder.getToken(FskUserPrincipalProcessor.threadLocalToken.get()));
    }

    private static void check() {
        LocalLoginProperties localLoginProperties = FskUserPrincipalProcessor.context.getBean(LocalLoginProperties.class);
        String customToken = localLoginProperties.getCustomToken().getName();
        if (StringUtils.isBlank(customToken) && !localLoginProperties.isEnable()) {
            log.warn(EXCE_MSG_2);
            throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getCode(), EXCE_MSG_2);
        }
    }

    protected static void destroy() {
        FskUserPrincipalProcessor.threadLocalToken.remove();
        FskUserPrincipalProcessor.threadLocalUser.remove();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FskUserPrincipalProcessor.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        FskUserPrincipalProcessor.env = environment;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        FskUserPrincipalProcessor.servletContext = servletContext;
    }
}
