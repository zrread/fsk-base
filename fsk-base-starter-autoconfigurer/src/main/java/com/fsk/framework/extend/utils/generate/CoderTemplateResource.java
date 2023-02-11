package com.fsk.framework.extend.utils.generate;

import com.fsk.framework.extend.utils.redis.CustomRedisTemplate;
import com.fsk.framework.pubconfig.entity.BizCodeSourceEntity;
import com.fsk.framework.pubconfig.entity.KeyEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/28
 * Describe: CoderTemplateResource.
 */
@Slf4j
@ConditionalOnProperty(
        prefix = "fsk.global.code",
        value = "enable",
        havingValue = "true"
)
@Component
public class CoderTemplateResource implements ApplicationContextAware, InitializingBean {

    @Value("${spring.application.name}")
    private String APP_NAME;
    private static RedisTemplate<String, Object> template = null;
    private static BizCodeSourceEntity bizCodeSource;
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bizCodeSource = context.getBean(KeyEntities.class).getBizCodeSource();
        template = CustomRedisTemplate
                .Builder()
                .host(bizCodeSource.getHost())
                .port(bizCodeSource.getPort())
                .password(bizCodeSource.getPassword())
                .database(bizCodeSource.getDatabase())
                .build();
        log.info(">>>>>>>>>>> Coder Template Init Success.");
    }

    public static Object get(String key) {
        return key == null ? null : template.opsForValue().get(key);
    }

    public static Long increment(String key) {
        return template.opsForValue().increment(key, 1L);
    }

    public String getAPP_NAME() {
        return APP_NAME;
    }

    public void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

}
