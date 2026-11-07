package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;

@Mapper
public interface SetmealDishMapper {

  /*
   * 根据套餐id查询套餐下的菜品id集合
   * 
   * @param setmealId
   * 
   * @return
   */
  // select setmeal_id from setmeal_dish where dish_id in (1,2,3,4)
  List<Long> getSetmealIdsByDishIds(List<Long> ids);

  /*
   * 新增套餐菜品关联关系
   * 
   * @param setmealDishes
   * 
   * @return
   */
  void insertBatch(List<SetmealDish> setmealDishes);

  /*
   * 根据套餐id查询套餐下的菜品关联关系
   * 
   * @param setmealId
   * 
   * @return
   */
  @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
  Setmeal getById(Long setmealId);

  // 根据套餐id删除套餐菜品关联关系
  @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
  void deleteBySetmealId(Long setmealId);
// 根据套餐id查询套餐和菜品的关联关系
  @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
  List<SetmealDish> getBySetmealId(Long id);


}
