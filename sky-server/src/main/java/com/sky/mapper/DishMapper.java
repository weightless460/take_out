package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 添加菜品
     * @param dish
     */
    @Autofill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 获取菜品
     * @param id
     * @return
     */
    Dish getById(Long id);

    /**
     * 删除菜品表中的菜品数据
     * @param id
     */
    void deleteById(Long id);
    /**
     * 批量删除
     * @param dishIds
     */
    void deleteBatch(List<Long> dishIds);

    /**
     * 修改菜品基本信息
     * @param dish
     */
    @Autofill(OperationType.UPDATE)
    void updata(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    List<Dish> getBySetmealId(Long id);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
