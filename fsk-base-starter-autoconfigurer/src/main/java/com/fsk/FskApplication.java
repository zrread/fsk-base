package com.fsk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * BelongName Fsk App Boot Starter
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022-01-11 10:52:00
 * Describe: This class is a startup class. You need to call this class in the springboot boot class.
 */
@Slf4j
public final class FskApplication {

    public final static String FRAMEWORK_VERSION = "1.11.2";

    public static void run(Class<? extends AbstractApp> clazz, String[] args) {
        SpringApplication springApplication = new SpringApplication(clazz);
        FskApplication.proxyProperties(clazz);
        springApplication.setBanner(new FskBanner());
        System.setProperty("fsk.app.type","FSK_SERVICE");
        ConfigurableApplicationContext context = springApplication.run(args);
        FskSpringContextHolder.setApplicationContext(context);
    }

    private static void proxyProperties(Class<? extends AbstractApp> clazz) {
        try {
            Method decorate = clazz.getSuperclass().getMethod("initProxy");
            AbstractApp abstractApp = clazz.newInstance();
            FskDecorateProxy fskDecorateProxy = (FskDecorateProxy) decorate.invoke(abstractApp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.info("You don't have custom proxy properties,you can ignore this tips");
            //throw new RuntimeException("Failed to proxy properties");
        }
    }

}
