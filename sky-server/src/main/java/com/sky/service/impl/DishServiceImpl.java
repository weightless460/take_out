package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private SetmealMapper setmealMapper;

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
    public void deleteBaktch(List<Long> ids) {
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
        // //删除菜品表中的菜品数据
        // for (Long id : ids) {
        //     mapper.deleteById(id);
        //     flavorsMapper.deleteByDishId(id);
        // }
        //优化
        mapper.deleteBatch(ids);
        flavorsMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish byId = mapper.getById(id);
        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavors=flavorsMapper.getByDishId(id);

        DishVO vo = new DishVO();
        BeanUtils.copyProperties(byId,vo);
        vo.setFlavors(dishFlavors);

        return vo;
    }

    @Transactional
    public void updataWithFlavor(DishDTO dto) {
        //修改菜品基础信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto,dish);
        mapper.updata(dish);
        //删除原有的口味数据
        flavorsMapper.deleteByDishId(dto.getId());
        //重新插入口味数据

        Long dishId = dish.getId();
        List<DishFlavor> flavors = dto.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

        flavorsMapper.insertBatch(flavors);
        }
    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = mapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = flavorsMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
    /**
     * 启用或禁用菜品
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        mapper.updata(dish);

        // 如果是禁用，需要将套餐中的菜品也禁用
        if (status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }
}

