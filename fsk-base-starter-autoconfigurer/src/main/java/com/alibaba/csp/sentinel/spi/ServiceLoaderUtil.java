package com.alibaba.csp.sentinel.spi;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import java.util.ServiceLoader;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/8
 * Describe: XXXXXXX.
 */
public class ServiceLoaderUtil {
    private static final String CLASSLOADER_DEFAULT = "default";
    private static final String CLASSLOADER_CONTEXT = "context";

    public static <S> ServiceLoader<S> getServiceLoader(Class<S> clazz) {
        return shouldUseContextClassloader() ? ServiceLoader.load(clazz) : ServiceLoader.load(clazz, clazz.getClassLoader());
    }

    public static boolean shouldUseContextClassloader() {
        String classloaderConf = SentinelConfig.getConfig("csp.sentinel.spi.classloader");
        return "context".equalsIgnoreCase(classloaderConf);
    }

    private ServiceLoaderUtil() {
    }
}

