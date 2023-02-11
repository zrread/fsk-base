package com.fsk;

import org.springframework.context.ApplicationContext;

public class FskSpringContextHolder {

    private static ApplicationContext applicationContext;

    private FskSpringContextHolder(ApplicationContext applicationContext) {
        FskSpringContextHolder.applicationContext = applicationContext;
    }

    protected static void setApplicationContext(ApplicationContext applicationContext) {
        FskSpringContextHolder.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBeanByClass(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }

}
