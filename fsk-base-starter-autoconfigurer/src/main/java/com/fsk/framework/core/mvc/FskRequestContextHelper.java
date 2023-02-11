package com.fsk.framework.core.mvc;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/28
 * Describe: FskRequestContextHelper.
 */
public final class FskRequestContextHelper {

    public static HttpServletRequest obtain() throws Exception {
        return thisRequest();
    }

    private static HttpServletRequest thisRequest() throws Exception{
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
