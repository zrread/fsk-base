package com.fsk.framework.core.interceptor.feign;

import feign.RequestTemplate;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/3
 * Describe: You can implement a custom interceptor by inheriting this class.
 */
public interface FeignRequestInterceptorCustomizer {

    /**
     * Customized processing logic is here.
     */
    void custom(RequestTemplate requestTemplate);
}
