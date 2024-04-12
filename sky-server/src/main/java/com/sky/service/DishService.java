package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Dish;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import sun.reflect.generics.tree.VoidDescriptor;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     * @param dto
     */
    public void saveWithFlavor(DishDTO dto);

    /**
     * 菜品分页查询
     * @param dto
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dto);
    /**
     * 菜品的删除
     */
    void deleteBaktch(List<Long> ids);
    /**
     * 根据id查询菜品和对应的口味数据
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品和关联的口味
     * @param dto
     */
    void updataWithFlavor(DishDTO dto);
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
