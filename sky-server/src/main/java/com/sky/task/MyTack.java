package com.sky.task;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@Component 
@Slf4j 
public class MyTack {
  /* 定时任务 */
  //@Scheduled (cron = "0/5 * * * * ?")
  public void executetask() {
    log.info("定时任务开始执行...",new Date());
  }
  
}
