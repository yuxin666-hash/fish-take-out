package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.sky.entity.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

  /*
   * 动态条件查询购物车列表
   * 
   * @param shoppingCart
   * 
   * @return
   */
  List<ShoppingCart> list(ShoppingCart shoppingCart);

  /*
   * 根据id更新购物车数量
   * 
   * @param shoppingCart
   */
  @Update("update shopping_cart set number = #{number} where id = #{id}")
  void updateNumberById(ShoppingCart cart);

  /*
   * 新增购物车数据
   * 
   * @param shoppingCart
   */
  @Insert("insert into shopping_cart (user_id, dish_id, setmeal_id, name, image, amount, number, create_time) "+
  "values (#{userId}, #{dishId}, #{setmealId}, #{name}, #{image}, #{amount}, #{number}, #{createTime})")

  void insert(ShoppingCart shoppingCart);

  /*
   * 根据用户id删除购物车数据
   * 
   * @param shoppingCart
   */
  @Delete("delete from shopping_cart where user_id = #{userId}")
  void deleteByUserId(ShoppingCart shoppingCart);
}
