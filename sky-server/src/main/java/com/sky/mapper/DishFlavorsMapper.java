package com.sky.mapper;

import com.sky.annotation.Autofill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {
    /**
     * 批量插入
     *
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味
     *
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);

    void deleteByDishIds(List<Long> ids);
    /**
     * 根据菜品id查询口味口味
     *
     * @param
     */
    @Select("select * from dish_flavor where dish_id=#{DishId}")
    List<DishFlavor> getByDishId(Long DishId);
}
