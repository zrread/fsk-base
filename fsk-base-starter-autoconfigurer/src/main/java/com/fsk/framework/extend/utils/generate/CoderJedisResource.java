package com.fsk.framework.extend.utils.generate;

import com.fsk.framework.bean.redis.EnvRedisConfig;
import com.fsk.framework.constants.CommonContanst;
import com.fsk.framework.pubconfig.entity.BizCodeSourceEntity;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/28
 * Describe: CoderJedisResource.
 */
/*@ConditionalOnProperty(
        prefix = "fsk.global.code",
        value = "enable",
        havingValue = "true"
)
@Configuration*/
public class CoderJedisResource implements ApplicationContextAware, InitializingBean, CommonContanst {
    @Value("${spring.application.name}")
    private String APP_NAME;
    @Value("${spring.profiles.active}")
    private String ACTIVE;
    private final static int timeout = 3000;
    private final Integer maxActive = 10;
    private final Integer maxWait = 1000;
    private final Integer maxIdle = 8;
    private final Integer minIdle = 0;
    private static EnvRedisConfig envRedisConfig = null;
    private static JedisPool jedisPool = null;
    private static BizCodeSourceEntity bizCodeSource;
    private static ApplicationContext context;

    private JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMaxTotal(maxActive);
        return config;
    }

    protected static synchronized Jedis getResource(){
        Jedis jedis = jedisPool.getResource();
        jedis.select(bizCodeSource.getDatabase());
        return jedis;
    }

    private void initEnvRedisConfig() {
        envRedisConfig = new EnvRedisConfig(
                bizCodeSource.getHost(),
                bizCodeSource.getPort(),
                bizCodeSource.getDatabase(),
                bizCodeSource.getPassword(),
                bizCodeSource.getTimeout(),
                getJedisPoolConfig()
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initEnvRedisConfig();
        CoderJedisResource.jedisPool = new JedisPool(
                envRedisConfig.getJedisPoolConfig(),
                envRedisConfig.getHost(),
                envRedisConfig.getPort(),
                envRedisConfig.getTimeout(),
                envRedisConfig.getPassword(),
                envRedisConfig.getDatabase()
        );
    }

    protected static String zeroFill(Long var, int length) {
        assert var != null;
        int len = String.valueOf(var).length();
        int var1 = length - len;
        StringBuilder var2 = new StringBuilder();
        for (int i = 0; i < var1; i++) {
            var2.append(ZERO);
        }
        return var2.append(var).toString();
    }

    protected String getAPP_NAME() {
        return APP_NAME;
    }

    private void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

    protected String getACTIVE() {
        return ACTIVE;
    }

    private void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
}
