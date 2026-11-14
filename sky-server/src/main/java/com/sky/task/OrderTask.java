package com.sky.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {

  @Autowired
  private OrderMapper orderMapper;

  /* 处理超时订单 */
  @Scheduled (cron = "0 * * * * ?")
  //@Scheduled(cron = "1/5 * * * * ?")
  public void processTimeOrder() {
    log.info("处理超时订单,{}", LocalDateTime.now());

    // select * from order where status = 0 and order_time < now() - interval 15
    // minute
    List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,
        LocalDateTime.now().minusMinutes(15));
    if (ordersList != null && ordersList.size() > 0) {
      for (Orders orders : ordersList) {
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("订单超时,订单取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
      }
    }

  }

  /* 处理一直在派送中状态的订单 */
  @Scheduled (cron = "0 0 1 * * ?")
  // @Scheduled (cron = "0/5 * * * * ?")
  public void processDeliveryOrder() {
    log.info("处理派送中订单,{}", LocalDateTime.now());
    List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,
        LocalDateTime.now().minusMinutes(60));
    if (ordersList != null && ordersList.size() > 0) {
      for (Orders orders : ordersList) {
        orders.setStatus(Orders.COMPLETED);
        orderMapper.update(orders);
      }
    }
  }
}
