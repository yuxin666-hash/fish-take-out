package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {

  /*
   * 新增菜品并保存菜品口味
   * 
   * @param dishDTO
   * 
   * @return
   */
  public void saveWithFlavor(DishDTO dishDTO);

  /*
   * 分页查询菜品
   * 
   * @param dishPageQueryDTO
   * 
   * @return
   */
  public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

  /*
   * 删除菜品
   * 
   * @param ids
   * 
   * @return
   */
  public void deleteBatch(List<Long> ids);

  /*
   * 根据菜品id查询菜品并包含菜品口味
   * 
   * @param id
   * 
   * @return
   */
  public DishVO getByIdWithFlavor(Long id);

  /*
   * 修改菜品和口味
   * 
   * @param dishDTO
   * 
   * @return
   */
  public void updateWithFlavor(DishDTO dishDTO);

  /**
   * 根据分类id查询菜品
   * 
   * @param categoryId
   * @return
   */
  public List<Dish> list(Long categoryId);

}
