package com.fsk.framework.bean.redis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/25
 * Describe: EnvRedisConfig.
 */
public class EnvRedisConfig {
    private String host;
    private int port;
    private int database;
    private String password;
    private int timeout;
    private JedisPoolConfig jedisPoolConfig;

    public EnvRedisConfig(String host, int port, int database, String password, int timeout, JedisPoolConfig jedisPoolConfig) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.password = password;
        this.timeout = timeout;
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }
}
