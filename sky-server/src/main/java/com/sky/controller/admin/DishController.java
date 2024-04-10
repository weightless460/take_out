package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/dish")
@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dto
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dto) {
        log.info("新增菜品：{}", dto);
        dishService.saveWithFlavor(dto);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dto) {
        log.info("菜品分页查询：{}", dto);
        PageResult result = dishService.pageQuery(dto);
        return Result.success(result);
    }

    /**
     * 菜品的删除
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBaktch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO vo = dishService.getByIdWithFlavor(id);
        return Result.success(vo);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    public Result updata(@RequestBody DishDTO dto) {
        log.info("修改菜品：{}", dto);
        dishService.updataWithFlavor(dto);
        return Result.success();
    }
}
