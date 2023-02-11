package com.fsk.framework.extend.utils.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/3/1
 * Describe: Custom Redis Template.
 */
public class CustomRedisTemplate {

    private final static int timeout = 5000;
    private final Integer maxActive = 8;
    private final Integer maxWait = -1;
    private final Integer maxIdle = 8;
    private final Integer minIdle = 0;

    public static Template Builder(){
        return new Template();
    }

    public static class Template{

        private RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        private JedisConnectionFactory factory = new JedisConnectionFactory();
        private String host = "localhost";
        private int port = 6379;
        private String password;
        private int database = 0;

        public Template host(String host){
            this.host = host;
            return this;
        }

        public Template port(int port){
            this.port = port;
            return this;
        }

        public Template password(String password){
            this.password = password;
            return this;
        }

        public Template database(int database){
            this.database = database;
            return this;
        }

        public RedisTemplate<String, Object> build(){
            this.initRedisConnectionFactory();
            this.initTemplate();
            return template;
        }

        private void initTemplate() {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(this.factory);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.afterPropertiesSet();
            template = redisTemplate;
        }

        private void initRedisConnectionFactory() {
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
            standaloneConfig.setHostName(host);
            standaloneConfig.setPort(port);
            if (StringUtils.isNotBlank(password)){
                standaloneConfig.setPassword(password);
            }
            standaloneConfig.setDatabase(database);
            factory = new JedisConnectionFactory(standaloneConfig);
            factory.afterPropertiesSet();
        }
    }

}
