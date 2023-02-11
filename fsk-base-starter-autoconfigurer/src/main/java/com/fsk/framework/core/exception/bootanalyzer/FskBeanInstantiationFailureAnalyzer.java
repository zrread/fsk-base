package com.fsk.framework.core.exception.bootanalyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/11
 * Describe: FskBeanInstantiationFailureAnalyzer.
 */
@Slf4j
public class FskBeanInstantiationFailureAnalyzer extends AbstractFailureAnalyzer<BeanInstantiationException> implements BeanFactoryAware, EnvironmentAware {

    private static BeanFactory FACTORY;
    private static Environment ENV;

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, BeanInstantiationException cause) {
        log.warn("----------------------------------------------------------------------------");
        String localizedMessage = cause.getLocalizedMessage();
        log.warn(localizedMessage);
        log.warn("----------------------------------------------------------------------------");
        Method constructingMethod = cause.getConstructingMethod();
        log.warn("{}", constructingMethod);
        log.warn("----------------------------------------------------------------------------");
        String message = cause.getMessage();
        log.warn(message);
        log.warn("----------------------------------------------------------------------------");
        Throwable causeCause = cause.getCause();
        log.warn(" ", causeCause);
        log.warn("----------------------------------------------------------------------------");

        return new FailureAnalysis(
                AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Fsk服务启动失败！原因是Bean实例化失败：" + cause.getConstructingMethod().getName() + " ---> 排查指南：(1)nacos上的环境变量(如fsk.dev)配置不正确 (2)检查Bean依赖注入关系", AnsiColor.DEFAULT),
                AnsiOutput.toString(AnsiColor.RED, "最终导致: " + cause.getCause() + "", AnsiColor.DEFAULT),
                cause);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        FskBeanInstantiationFailureAnalyzer.FACTORY = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        FskBeanInstantiationFailureAnalyzer.ENV = environment;
    }
}
