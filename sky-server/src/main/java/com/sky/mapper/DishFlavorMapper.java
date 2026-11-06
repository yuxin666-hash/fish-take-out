package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.type.MappedJdbcTypes;

import com.sky.entity.DishFlavor;

@Mapper 
public interface DishFlavorMapper {
  /*批量插入口味数据
   * @param flavors
   * @return
  */
  void insertBatch(List<DishFlavor> flavors);

  
} 
  

