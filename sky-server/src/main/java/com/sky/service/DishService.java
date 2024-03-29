package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
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
    void deleteBatch(List<Long> ids);
}
