package com.fsk.framework.core.http;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/10
 * Describe: RestTemplateSentinelExceptionUtil.
 */
@Slf4j
public class RestTemplateSentinelExceptionUtil {
    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) throws IOException {
        log.error(">>>>>> Resttemplate request is degraded by sentinel fuse.");
        return execution.execute(request, body);
    }
}
