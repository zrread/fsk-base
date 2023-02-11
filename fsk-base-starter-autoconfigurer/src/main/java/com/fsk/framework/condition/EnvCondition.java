package com.fsk.framework.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/21
 * Describe: EnvCondition.
 */
public class EnvCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String testValue = (conditionContext.getEnvironment().getProperty("spring.profiles.active"));
        return !"office".equalsIgnoreCase(testValue);
    }
}
