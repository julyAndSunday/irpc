package com.starter.irpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 15:20
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface IRpcAutoWired {
    String value() default "";
}
