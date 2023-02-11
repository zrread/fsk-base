package com.fsk.framework.core.interceptor.login;

import com.alibaba.fastjson.JSONObject;
import com.fsk.framework.bean.biz.system.LoginUserInfo;
import com.fsk.framework.constants.ExceptionConstant;
import com.fsk.framework.core.exception.BizException;
import com.fsk.framework.core.exception.ResponseRetEnum;
import com.fsk.framework.extend.utils.redis.CustomRedisTemplate;
import com.fsk.framework.pubconfig.entity.KeyEntities;
import com.fsk.framework.pubconfig.entity.SessionSourceEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/25
 * Describe: SessionTokenHolder.
 */
@Slf4j
@ConditionalOnProperty(prefix = "fsk.global.local-user", value = "enable", havingValue = "true")
@Component
public final class SessionTokenHolder implements ApplicationContextAware, InitializingBean, ExceptionConstant {

    private static RedisTemplate<String, Object> ssoTemplate = null;
    private static RedisTemplate<String, Object> currentTemplate = null;
//    private static FskRedisClusterProperties currentRedisClusterProperties = null;
//    private static FskRedisSingletonProperties currentRedisSingletonProperties = null;
    private static RedisProperties currentRedisProperties;
    private static String CUSTOM_TOKEN = "";
    private static String NULL = "null";
    @Value("${spring.application.name}")
    private String APP_NAME;
    @Value("${spring.profiles.active}")
    private String ACTIVE;
    private static SessionSourceEntity sessionSource;
    private static ApplicationContext context;

    public static LoginUserInfo getToken(String sessionKey) {
        Object principal = null;
        try {
            principal = get(sessionKey);
            if (null == principal) {
                log.warn(EXCE_MSG_0);
                throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getCode(), EXCE_MSG_0);
            }
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error(EXCE_MSG_1);
            throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getCode(), EXCE_MSG_1);
        }
        return JSONObject.parseObject(JSONObject.toJSONString(principal), LoginUserInfo.class);
    }

    public static Object get(String key) {
        if (StringUtils.isNotBlank(CUSTOM_TOKEN) && !CUSTOM_TOKEN.equals("NULL")) {
            return key == null ? null : currentTemplate.opsForValue().get(key);
        }
        return key == null ? null : ssoTemplate.opsForValue().get(key);
    }

    public SessionTokenHolder(@Autowired RedisProperties redisProperties,
                              @Value("${fsk.global.redis.cluster:false}") boolean enabled,
                              @Value("${fsk.global.local-user.custom-token.name:NULL}") String customToken) {
        currentRedisProperties = redisProperties;
        CUSTOM_TOKEN = customToken;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sessionSource = context.getBean(KeyEntities.class).getSessionSource();
        ssoTemplate = CustomRedisTemplate
                .Builder()
                .host(sessionSource.getHost())
                .port(sessionSource.getPort())
                .password(sessionSource.getPassword())
                .database(sessionSource.getDatabase())
                .build();
        currentTemplate = CustomRedisTemplate
                .Builder()
                .host(currentRedisProperties.getHost())
                .port(currentRedisProperties.getPort())
                .password(currentRedisProperties.getPassword())
                .database(currentRedisProperties.getDatabase())
                .build();
        log.info(">>>>>>>>>>> SessionToken Holder Init Success.");
    }

    /*private static synchronized Jedis getResource() {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.select(FskSpringContextHolder.getBeanByClass(SessionTokenHolder.class).getDatabase());
            return jedis;
        }catch (NullPointerException npe) {
            log.error(EXCE_MSG_2, npe);
            throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getErrorCode(), EXCE_MSG_2);
        } catch (NoSuchBeanDefinitionException nbe) {
            log.error(EXCE_MSG_2, nbe);
            throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getErrorCode(), EXCE_MSG_2);
        } catch (Exception e) {
            log.error(EXCE_MSG_3, e);
            throw new BizException(ResponseRetEnum.BIZ_LOCAL_USER_NOT_LOGGED_IN.getErrorCode(), EXCE_MSG_3);
        }
    }*/

}
