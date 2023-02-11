package com.fsk.framework.core.http;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/10
 * Describe: CustomRestTemplate.
 */
//@Component
public class CustomRestTemplate implements RestTemplateCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {
        ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                // ...
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                // ...
            }
        };
        ClientHttpRequestFactory requestFactory = new ClientHttpRequestFactory() {
            @Override
            public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
                return null;
            }
        };
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(errorHandler);
    }
}
