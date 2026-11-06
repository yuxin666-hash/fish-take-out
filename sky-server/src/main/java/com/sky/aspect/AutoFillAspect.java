package com.sky.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

// 自动填充切面:实现公共字段填充
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

  // 切入点
  @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.annotation.AutoFill)")
  public void autoFillAspect() {
  }

  // 通知(前置),在通知中进行公共字段赋值
  @Before("autoFillAspect()")
  public void before(JoinPoint joinPoint) {
    log.info("开始进行公共字段的填充...");

    // 获取注解中的数据库操作类型
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    AutoFill autoFill = method.getAnnotation(AutoFill.class);

    OperationType operationType = autoFill.value();
    // 获取当前执行的方法--实体对象
    Object[] args = joinPoint.getArgs();
    if (args.length == 0) {
      return;
    }
    Object entity = args[0];
    // 准备赋值的数据
    LocalDateTime now = LocalDateTime.now();
    Long currentId = BaseContext.getCurrentId();

    // 根据不同的操作类型,为对应的属性通过反射赋值
    if (operationType == OperationType.INSERT) {
      // 插入操作,为create_time,update_time,create_user,update_user赋值
      Method setCreateTime;
      try {
        setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);  
        Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        // 通过反射赋值create_time,update_time,create_user,update_user
        setCreateTime.invoke(entity, now);
        setUpdateTime.invoke(entity, now);
        setCreateUser.invoke(entity, currentId);
        setUpdateUser.invoke(entity, currentId);
      } catch (Exception e) {
        e.printStackTrace();
      } 

    } else if (operationType == OperationType.UPDATE) {
      // 更新操作,为update_time,update_user赋值
      Method setUpdateTime;
      try {
        setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
          Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
      // 通过反射赋值update_time,update_user
      setUpdateTime.invoke(entity, now);
      setUpdateUser.invoke(entity, currentId);
      } catch (Exception e) {
        e.printStackTrace();
      }
    

    }

  }

}
