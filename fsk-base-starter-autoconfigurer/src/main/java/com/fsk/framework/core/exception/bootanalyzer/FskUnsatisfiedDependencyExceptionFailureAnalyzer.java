package com.fsk.framework.core.exception.bootanalyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/11
 * Describe: FskUnsatisfiedDependencyExceptionFailureAnalyzer.
 */
@Slf4j
public class FskUnsatisfiedDependencyExceptionFailureAnalyzer extends AbstractFailureAnalyzer<UnsatisfiedDependencyException> implements BeanFactoryAware, EnvironmentAware {

    private static BeanFactory FACTORY;
    private static Environment ENV;

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, UnsatisfiedDependencyException cause) {
        log.warn("----------------------------------------------------------------------------");
        log.warn(" ", cause);
        log.warn("----------------------------------------------------------------------------");
        return new FailureAnalysis(
                AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Fsk服务启动失败！原因是发生了不满足的Bean依赖：" + cause.getBeanName() + " ---> 排查指南：(1)检查Bean依赖关系", AnsiColor.DEFAULT),
                AnsiOutput.toString(AnsiColor.RED, "最终导致: " + cause.getCause() + "", AnsiColor.DEFAULT),
                cause);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        FskUnsatisfiedDependencyExceptionFailureAnalyzer.FACTORY = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        FskUnsatisfiedDependencyExceptionFailureAnalyzer.ENV = environment;
    }
}
