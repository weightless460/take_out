package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.OutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类
 */
@Component
@Slf4j
public class OrderTask {
    /*
    * 处理超时
    * */
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?") //每分钟触发一次
    public void processTimeOutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(15);
        //查询超时订单

        List<Orders> orderTimeLT = orderMapper.getBystatusAndOrderTimeLT(Orders.PENDING_PAYMENT, localDateTime);

        if(orderTimeLT !=null && orderTimeLT.size()>0){
            for (Orders orders : orderTimeLT) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点触发
    public void processDeliveryOrder(){
        log.info("定时处理超时订单：{}",LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        //查询超时订单
        List<Orders> orderTimeLT = orderMapper.getBystatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, localDateTime);

        if(orderTimeLT !=null && orderTimeLT.size()>0){
            for (Orders orders : orderTimeLT) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }

}
