package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于表示某个方法需要进行功能字段自动填充处理
 */
@Target(ElementType.METHOD)//注解的的作用目标，可标识在方法上
@Retention(RetentionPolicy.RUNTIME)//注解的保留策略，运行时可以保留
public @interface Autofill {
    //数据库操作类型:UPDATE、INSET
    OperationType value();
}
