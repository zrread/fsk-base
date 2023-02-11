package com.fsk.framework.core.http;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/10
 * Describe: In order to maintain the uniform use of okhttp3.0,
 * we do not recommend using resttemplate for remote calls,
 * because resttemplate ultimately relies on httpclient for remote calls.
 * Multiple RestTemplate Objects,you can use them like this through injecting it:
 *
 *    - @Autowired
 *      private RestTemplate restTemplate;
 *
 *    - @Autowired
 *    - @LoadBalanced
 *      private RestTemplate loadBalanced;
 */
@Configuration
public class RestTemplateAutoConfiguration {

    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = RestTemplateSentinelExceptionUtil.class)
//    @ConditionalOnMissingBean(RestTemplate.class)
    @ConditionalOnClass({RestTemplate.class, RestTemplateBuilder.class})
    @Primary
    @Bean
    public RestTemplate createNotSLB(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = RestTemplateSentinelExceptionUtil.class)
//    @ConditionalOnMissingBean(RestTemplate.class)
    @ConditionalOnClass({RestTemplate.class, RestTemplateBuilder.class})
    @LoadBalanced
    @Bean
    public RestTemplate createSLB(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
