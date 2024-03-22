package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import sun.reflect.generics.tree.VoidDescriptor;

public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     * @param dto
     */
    public void saveWithFlavor(DishDTO dto);
}
