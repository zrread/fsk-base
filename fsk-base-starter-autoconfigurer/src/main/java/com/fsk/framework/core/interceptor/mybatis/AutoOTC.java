package com.fsk.framework.core.interceptor.mybatis;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.constants.CommonContanst;
import com.fsk.framework.core.interceptor.login.UserPrincipalHolder;
import com.fsk.framework.core.interceptor.login.UserPrincipalInterceptor;
import com.fsk.framework.core.mvc.FskRequestContextHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class AutoOTC implements CommonContanst {

    private static boolean CHECK;

    @Autowired
    public AutoOTC(@Value("${fsk.global.local-user.enable}") boolean check) {
        AutoOTC.CHECK = check;
    }

    protected static String getOperatorCode() {
        return check() ? "SYSTEM" : UserPrincipalHolder.get().getUserCode();
    }

    protected static String getOperatorName() {
        return check() ? "SYSTEM" : UserPrincipalHolder.get().getUserFullName();
    }

    public static boolean check() {
        if (!AutoOTC.CHECK) {
            return true;
        }
        HttpServletRequest request = null;
        try {
            request = FskRequestContextHelper.obtain();
        } catch (Exception e) {
            return true;
        }
        return checkPass(request);
    }

    public static boolean check(HttpServletRequest request) {
        return checkPass(request);
    }

    private static boolean checkPass(HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getHeader(AUTO_FILL_WHITE_LIST_PASS))) {
            return true;
        }

        UserPrincipalInterceptor userPrincipalInterceptor = FskSpringContextHolder.getBeanByClass(UserPrincipalInterceptor.class);
        List<String> apiWhiteList = userPrincipalInterceptor.getApiWhiteList();
        for (String api : apiWhiteList) {
            if (request.getRequestURI().contains(api)) {
                return true;
            }
        }

        return false;
    }

}

