package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
    private DishFlavorsMapper flavorsMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

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
            flavorsMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        //分页查询插件工具
        PageHelper.startPage(dto.getPage(),dto.getPageSize());

        Page<DishVO> page=mapper.pageQuery(dto);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 菜品的删除
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除（是否存在起售中的菜品？）
        for (Long id : ids) {
          Dish dish=  mapper.getById(id);
          if (dish.getStatus()== StatusConstant.ENABLE){
              //当前菜品起售中
              throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
          }
        }
        //判断是否能够删除，是否被套餐关联了
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds!=null &&setmealIds.size()>0){
            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据
        for (Long id : ids) {
            mapper.deleteById(id);
            flavorsMapper.deleteByDishId(id);
        }
        //删除口味数据

    }


}
