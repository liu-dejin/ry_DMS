package com.ruoyi.sky.service;

import java.util.List;
import com.ruoyi.sky.domain.Dish;

/**
 * 菜品管理Service接口
 * 
 * @author ruoyi
 * @date 2024-06-08
 */
public interface IDishService 
{
    /**
     * 查询菜品管理
     * 
     * @param id 菜品管理主键
     * @return 菜品管理
     */
    public Dish selectDishById(Long id);

    /**
     * 查询菜品管理列表
     * 
     * @param dish 菜品管理
     * @return 菜品管理集合
     */
    public List<Dish> selectDishList(Dish dish);

    /**
     * 新增菜品管理
     * 
     * @param dish 菜品管理
     * @return 结果
     */
    public int insertDish(Dish dish);

    /**
     * 修改菜品管理
     * 
     * @param dish 菜品管理
     * @return 结果
     */
    public int updateDish(Dish dish);

    /**
     * 批量删除菜品管理
     * 
     * @param ids 需要删除的菜品管理主键集合
     * @return 结果
     */
    public int deleteDishByIds(Long[] ids);

    /**
     * 删除菜品管理信息
     * 
     * @param id 菜品管理主键
     * @return 结果
     */
    public int deleteDishById(Long id);
}
