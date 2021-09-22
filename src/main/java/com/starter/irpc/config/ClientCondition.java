package com.starter.irpc.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-21 21:12
 **/
public class ClientCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("irpc.scanPath");
        return property != null;
    }
}
