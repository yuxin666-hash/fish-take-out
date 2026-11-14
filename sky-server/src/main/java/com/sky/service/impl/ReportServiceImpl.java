package com.sky.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
  @Autowired
  private OrderMapper orderMapper;

  /*
   * 营业额统计
   * 
   * @param begin 开始日期
   * 
   * @param end 结束日期
   * 
   * @return
   */
  public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
    // 当前集合用于存放从begin到End范围内的每天的日期
    List<LocalDate> dateList = new ArrayList<>();
    dateList.add(begin);
    while (!begin.equals(end)) {
      // 日期计算,计算指定日期的后一天对应的日期
      begin = begin.plusDays(1);
      dateList.add(begin);
    }
    // 存放每天的营业额
    List<Double> turnoverList = new ArrayList<>();
    for (LocalDate date : dateList) { 
      // 从数据库中查询该日期的营业额(订单状态显示为已完成的订单金额合计)
      LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
      LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
      //select sum(amount) from orders where order_status = 5 and order_time > beginTime and order_time < endTime
      Map map = new  HashMap();
      map.put("begin",beginTime);
      map.put("end",endTime);
      map.put("orderStatus",Orders.COMPLETED);
      Double turnover = orderMapper.sumByMap(map);
      turnover = turnover == null ? 0.0 : turnover;
      turnoverList.add(turnover);
    }

    return TurnoverReportVO
    .builder()
    .dateList(StringUtils.join(dateList, ","))
    .turnoverList(StringUtils.join(turnoverList,","))
    .build();
  }

}
