package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterAbortException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequestMapping("/admin/dish")
@RestController
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    /**
     * 新增菜品
     *
     * @param dto
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = "dish_",allEntries = true)
    public Result save(@RequestBody DishDTO dto) {
        log.info("新增菜品：{}", dto);
        dishService.saveWithFlavor(dto);

        //清理缓存数据
        String key = "dish_" + dto.getCategoryId();
        template.delete(key);
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
        //所有菜品缓存数据删除，所有以dish开头的key

        Set keys = template.keys("dish_*");
        template.delete(keys);
        return Result.success();
    }

    /**
     * 启用或停用菜品
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        // 1. 启用 0. 停用
        log.info("启用或停用菜品：{}", id);
        dishService.startOrStop(status, id);
        clearRedis("dish_*");
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
        clearRedis("dish_*");
        return Result.success();
    }

    private void clearRedis(String patten){
        Set keys = template.keys(patten);
        template.delete(keys);
    }
}
