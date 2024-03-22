package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper mapper;
    @Autowired
    private DishFlavorsMapper flavorsMapperMapper;


    /**
     * 新增菜品和对应的口味数据
     * @param dto
     */
    @Transactional
    public void saveWithFlavor(DishDTO dto){
        Dish dish=new Dish();

        BeanUtils.copyProperties(dto,dish);
        //向菜品表插入一条数据
        mapper.insert(dish);
        Long id = dish.getId();

        //向口味表插入
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            //向口味表插入
            flavorsMapperMapper.insertBatch(flavors);
        }
    };
}
