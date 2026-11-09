package com.sky.controller.admin;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

//菜品管理
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {
  @Autowired
  private DishService dishService;
  @Autowired
  private RedisTemplate redisTemplate;

  /**
   * 新增菜品
   * 
   * @param dishDTO
   * @return
   */
  @PostMapping
  @ApiOperation(value = "新增菜品")
  public Result save(@RequestBody DishDTO dishDTO) {
    log.info("新增菜品：{}", dishDTO);
    dishService.saveWithFlavor(dishDTO);
    // 新增菜品后，需要删除Redis中的缓存
    String key = "dish:" + dishDTO.getCategoryId();
    cleanCache(key);
    return Result.success();
  }

  /*
   * 分页查询菜品
   * 
   * @param dishPageQueryDTO
   * 
   * @return
   */
  @GetMapping("/page")
  @ApiOperation("分页查询菜品")
  public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
    log.info("分页查询菜品:{}", dishPageQueryDTO);
    PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  /*
   * 删除菜品
   * 
   * @param ids
   * 
   * @return
   */
  @DeleteMapping
  @ApiOperation("删除菜品")
  public Result delete(@RequestParam List<Long> ids) {
    log.info("删除菜品：{}", ids);
    dishService.deleteBatch(ids);
    // 将所有菜品缓存数据清理掉，所有以 dish: 开头的 key
    cleanCache("dish:*");
    return Result.success();
  }

  /*
   * 根据菜品id查询菜品
   * 
   * @param id
   * 
   * @return
   */
  @GetMapping("/{id}")
  @ApiOperation("根据菜品id查询菜品")
  public Result<DishVO> getById(@PathVariable Long id) {
    log.info("根据菜品id查询菜品:{}", id);
    DishVO dishVO = dishService.getByIdWithFlavor(id);
    return Result.success(dishVO);
  }

  /*
   * 修改菜品
   * 
   * @param dishDTO
   * 
   * @return
   */
  @PutMapping
  @ApiOperation("修改新菜品")
  public Result update(@RequestBody DishDTO dishDTO) {
    log.info("修改新菜品：{}", dishDTO);
    dishService.updateWithFlavor(dishDTO);
    // 将所有菜品缓存数据清理掉，所有以 dish: 开头的 key
    cleanCache("dish:*");
    return Result.success();
  }

  /*
   * 菜品起售停售
   * 
   * @param status
   * 
   * @param ids
   * 
   * @return
   */
  @PostMapping("/status/{status}")
  @ApiOperation("菜品起售停售")
  public Result<String> startOrStop(@PathVariable Integer status, Long id) {
    dishService.startOrStop(id, status);
    // 将所有菜品缓存数据清理掉，所有以 dish: 开头的 key
    cleanCache("dish:*");
    return Result.success("操作成功");

  }

  /**
   * 根据分类id查询菜品
   * 
   * @param categoryId
   * @return
   */
  @GetMapping("/list")
  @ApiOperation("根据分类id查询菜品")
  public Result<List<Dish>> list(Long categoryId) {
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
  }

  /*
   * 清理Redis缓存
   * 
   * @param pattern
   * 
   * @return
   */
  private void cleanCache(String pattern) {
    Set keys = redisTemplate.keys(pattern);
    if (keys != null && !keys.isEmpty()) {
      redisTemplate.delete(keys);
    }
  }
}
