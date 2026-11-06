package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {

  /* 
   * 新增菜品并保存菜品口味
   * @param dishDTO
   * @return
   */
  public void saveWithFlavor(DishDTO dishDTO);
  
}
