package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void add(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 根据id查询订单
     * @param Id
     * @return
     */
    @Select("select * from orders where id = #{Id}")
    Orders getById(Long Id);
    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据订单状态和时间查询订单
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time < #{time}")
    List<Orders> getBystatusAndOrderTimeLT(@Param("status") Integer status, @Param("time") LocalDateTime time);
}
