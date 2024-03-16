package com.khandras.bot.fw.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Send email to developer about method parameters, its content and processing time
 */
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface NotifyMethodCall {
    String to() default "arturkhandras@gmail.com";
}
