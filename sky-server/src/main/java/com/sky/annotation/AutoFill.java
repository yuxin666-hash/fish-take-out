package com.sky.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

// 自动填充注解,用于标识某个方法需要自动填充某些字段
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
  //数据库操作类型:UPDATE,INSERT
  OperationType value();
}
